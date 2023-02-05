package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.completeComposer;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions.*;

import org.junit.jupiter.api.Test;

public class CompositionBuilderTest {

    private final static Map<EnumSet<ExampleSubject>, Integer> SUBJECT_SET_TO_EXPECTED_NODE_NUMBER = new HashMap<EnumSet<ExampleSubject>, Integer>() {{
        put(EnumSet.of(ExampleSubject.A), 1);
        put(EnumSet.of(ExampleSubject.B), 1);
        put(EnumSet.of(ExampleSubject.C), 1);
        put(EnumSet.of(ExampleSubject.D), 2);
        put(EnumSet.of(ExampleSubject.E), 3);
        put(EnumSet.of(ExampleSubject.F), 6);

        put(EnumSet.of(ExampleSubject.A, ExampleSubject.E), 4);
        put(EnumSet.of(ExampleSubject.F, ExampleSubject.B, ExampleSubject.D), 6);
    }};

    private CalculationComposer<ExampleSubject, Integer> freshComposer = new CalculationComposer<>(ExampleSubject.class);
    private CalculationComposer<ExampleSubject, Integer> composerBrokenByCD = new CalculationComposer<ExampleSubject, Integer>(ExampleSubject.class) {{
        register(new AIndependentProvider());
        register(new BIndependentProvider());
        register(new CDangerouslyDependentOnFProvider());
        register(new DDependentOnAProvider());
        register(new EDependentOnBCProvider());
        register(new FDependentOnADEProvider());
    }};
    private CompositionBuilder<ExampleSubject, Integer> builder;

    @Test
    public void shouldBuildCompositionWithProperlyOrderedNodesInMinimalNumber() {
        for (EnumSet<ExampleSubject> subjectSet : SUBJECT_SET_TO_EXPECTED_NODE_NUMBER.keySet()) {
            builder = new CompositionBuilder<>(completeComposer, subjectSet);
            Composition<ExampleSubject, Integer> composition = subjectSet.size() > 1 ? builder.buildMultiOutputComposition() : builder.buildSingleOutputComposition();
            Collection<CompositionNode<ExampleSubject, Integer>> nodes = composition.unmodifableOrderedNodes;
            assertProperOrderAndNoDuplicates(nodes);
            assertEquals(SUBJECT_SET_TO_EXPECTED_NODE_NUMBER.get(subjectSet), nodes.size());
        }
    }

    @Test
    public void shouldBuildCompositionWithProviderInstancesReturnedByRegisteredInstances() {
        Collection<CompositionNode<ExampleSubject, Integer>> accumulatedCompositionNodes = new ArrayList<>(ExampleSubject.values().length * 2);
        builder = new CompositionBuilder<>(completeComposer, EnumSet.of(ExampleSubject.F));
        accumulatedCompositionNodes.addAll(builder.buildSingleOutputComposition().unmodifableOrderedNodes);
        builder = new CompositionBuilder<>(completeComposer, EnumSet.allOf(ExampleSubject.class));
        accumulatedCompositionNodes.addAll(builder.buildMultiOutputComposition().unmodifableOrderedNodes);

        for (CompositionNode<ExampleSubject, Integer> node : accumulatedCompositionNodes) {
            Provider<ExampleSubject, Integer> compositionProvider = node.calculator;
            Provider<ExampleSubject, Integer> registeredProvider = completeComposer.register.getRequired(compositionProvider.provides()).calculator;
            assertNotEquals(registeredProvider, compositionProvider);
        }
    }

    @Test
    public void shouldThrowCircularDependencyException() {
        EnumSet<ExampleSubject> expectedCircularDependencySubjects = EnumSet.of(ExampleSubject.C, ExampleSubject.E, ExampleSubject.F);

        builder = new CompositionBuilder<>(composerBrokenByCD, EnumSet.of(ExampleSubject.F));
        CircularDependencyException exception = assertThrows(CircularDependencyException.class, () -> builder.buildSingleOutputComposition());
        assertEquals(expectedCircularDependencySubjects, new HashSet<>(exception.getSubjectDependencyPath()));

        builder = new CompositionBuilder<>(composerBrokenByCD, EnumSet.of(ExampleSubject.D, ExampleSubject.C));
        exception = assertThrows(CircularDependencyException.class, () -> builder.buildMultiOutputComposition());
        assertEquals(expectedCircularDependencySubjects, new HashSet<>(exception.getSubjectDependencyPath()));

        freshComposer.register(new ADangerouslyDependentOnItselfProvider());
        builder = new CompositionBuilder<>(freshComposer, EnumSet.of(ExampleSubject.A));
        exception = assertThrows(CircularDependencyException.class, () -> builder.buildSingleOutputComposition());
        assertEquals(ExampleSubject.A, exception.getSubjectDependencyPath().iterator().next());
    }

    @Test
    public void shouldThrowProviderLackException() {
        builder = new CompositionBuilder<>(freshComposer, EnumSet.of(ExampleSubject.A));
        ProviderLackException exception = assertThrows(ProviderLackException.class, () -> builder.buildSingleOutputComposition());
        assertEquals(ExampleSubject.A, exception.getMissingSubjects().iterator().next());

        freshComposer.register(new EDependentOnBCProvider());
        builder = new CompositionBuilder<>(freshComposer, EnumSet.of(ExampleSubject.E));
        exception = assertThrows(ProviderLackException.class, () -> builder.buildSingleOutputComposition());
        assertEquals(EnumSet.of(ExampleSubject.B, ExampleSubject.C), new HashSet<>(exception.getMissingSubjects()));

        freshComposer.register(new DDependentOnAProvider());
        builder = new CompositionBuilder<>(freshComposer, EnumSet.of(ExampleSubject.D));
        exception = assertThrows(ProviderLackException.class, () -> builder.buildSingleOutputComposition());
        assertEquals(ExampleSubject.A, exception.getMissingSubjects().iterator().next());

        freshComposer.register(new AIndependentProvider());
        freshComposer.register(new FDependentOnADEProvider());
        builder = new CompositionBuilder<>(freshComposer, EnumSet.of(ExampleSubject.A, ExampleSubject.D, ExampleSubject.F));
        exception = assertThrows(ProviderLackException.class, () -> builder.buildMultiOutputComposition());
        assertEquals(EnumSet.of(ExampleSubject.B, ExampleSubject.C), new HashSet<>(exception.getMissingSubjects()));

        freshComposer.register(new CIndependentProvider());
        builder = new CompositionBuilder<>(freshComposer, EnumSet.of(ExampleSubject.A, ExampleSubject.D, ExampleSubject.F));
        exception = assertThrows(ProviderLackException.class, () -> builder.buildMultiOutputComposition());
        assertEquals(ExampleSubject.B, exception.getMissingSubjects().iterator().next());
    }

    private void assertProperOrderAndNoDuplicates(Collection<CompositionNode<ExampleSubject, Integer>> nodes) {
        int nodePosition = 0;
        for (CompositionNode<ExampleSubject, Integer> node : nodes) {
            int otherNodePosition = 0;
            for (CompositionNode<ExampleSubject, Integer> otherNode : nodes) {
                if (node.calculator.requires().contains(otherNode.calculator.provides())) {
                    assertTrue(
                        nodePosition > otherNodePosition,
                        node.calculator.getClass().getName() + " requires " + otherNode.calculator.provides().name()
                            + " so its node is expected to be after the one of " + otherNode.calculator.getClass().getName() + ","
                            + " but their positions are " + nodePosition + " and " + otherNodePosition + " respectively."
                    );
                }
                if (node != otherNode) {
                    assertNotEquals(node.calculator.provides(), otherNode.calculator.provides());
                }
                otherNodePosition++;
            }
            nodePosition++;
        }
    }
}
