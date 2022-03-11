package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import static com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.completeComposer;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.*;

import org.junit.jupiter.api.Test;

public class CompositionTest {

    private Composition<ExampleSubject, Integer> composition;

    @Test
    public void shouldProcessCalculationVisitingEachNodeOnce() {
        composition = completeComposer.compose(ExampleSubject.F);
        composition.processCalculations(1);
        assertEquals(2, composition.unmodifableValues.get(ExampleSubject.A));
        assertEquals(3, composition.unmodifableValues.get(ExampleSubject.B));
        assertEquals(5, composition.unmodifableValues.get(ExampleSubject.C));
        assertEquals(2 * 7, composition.unmodifableValues.get(ExampleSubject.D));
        assertEquals(3 * 5 * 11, composition.unmodifableValues.get(ExampleSubject.E));
        assertEquals(2 * (2 * 7) * (3 * 5 * 11) * 13, composition.unmodifableValues.get(ExampleSubject.F));
        composition.unmodifableOrderedNodes.stream()
            .forEach(n -> assertEquals(1, ((ExampleProvider) n.calculator).getCalcCount()));

        composition = completeComposer.compose(EnumSet.allOf(ExampleSubject.class));
        composition.processCalculations(17);
        assertEquals(17 * 2, composition.unmodifableValues.get(ExampleSubject.A));
        assertEquals(17 * 3, composition.unmodifableValues.get(ExampleSubject.B));
        assertEquals(17 * 5, composition.unmodifableValues.get(ExampleSubject.C));
        assertEquals(17 * 2 * 7, composition.unmodifableValues.get(ExampleSubject.D));
        assertEquals((17 * 3) * (17 * 5) * 11, composition.unmodifableValues.get(ExampleSubject.E));
        assertEquals((17 * 2) * (17 * 2 * 7) * ((17 * 3) * (17 * 5) * 11) * 13, composition.unmodifableValues.get(ExampleSubject.F));
        composition.unmodifableOrderedNodes.stream()
            .forEach(n -> assertEquals(1, ((ExampleProvider) n.calculator).getCalcCount()));
    }

    @Test
    public void shouldNewCompositionInstanceHaveNewNodes() {
        List<Collection<CompositionNode<ExampleSubject, Integer>>> nodeCollections = new LinkedList<>();
        
        composition = completeComposer.compose(ExampleSubject.F);
        nodeCollections.add(composition.unmodifableOrderedNodes);
        composition = new ExampleCompositionClass(composition);
        nodeCollections.add(composition.unmodifableOrderedNodes);
        composition = completeComposer.compose(EnumSet.allOf(ExampleSubject.class));
        nodeCollections.add(composition.unmodifableOrderedNodes);
        composition = new ExampleCompositionClass(composition);
        nodeCollections.add(composition.unmodifableOrderedNodes);

        for (Collection<CompositionNode<ExampleSubject, Integer>> nodeCollection : nodeCollections)
            for (Collection<CompositionNode<ExampleSubject, Integer>> otherNodeCollection : nodeCollections)
                if (nodeCollection != otherNodeCollection)
                    assertTrue(Collections.disjoint(nodeCollection, otherNodeCollection));
    }
}
