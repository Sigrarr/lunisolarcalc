package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A composed calculation yielding values of one quantity or multiple quantities
 * represented by its "target subject(s)".
 *
 * It is made of multiple {@linkplain Provider providers}.
 * Predefined conrete subclasses: {@link SingleOutputComposition}, {@link MultiOutputComposition}.
 *
 * @param <SubjectT>    {@linkplain Enum enumeration type} of the quantities under calculation,
 *                      containing all available "subjects"
 * @param <InT>         type of an input argument
 * @see                 CalculationComposer
 */
public abstract class Composition<SubjectT extends Enum<SubjectT>, InT> {

    protected final Collection<CompositionNode<SubjectT, InT>> unmodifableOrderedNodes;
    protected final Class<SubjectT> subjectEnumClass;
    private final Map<SubjectT, Object> values;
    protected final Map<SubjectT, Object> unmodifableValues;

    Composition(Collection<CompositionNode<SubjectT, InT>> orderedNodes, Class<SubjectT> subjectEnumClass) {
        unmodifableOrderedNodes = Collections.unmodifiableCollection(orderedNodes);
        this.subjectEnumClass = subjectEnumClass;
        values = new EnumMap<>(subjectEnumClass);
        unmodifableValues = Collections.unmodifiableMap(values);
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public Composition(Composition<SubjectT, InT> composition) {
        this(
            composition.unmodifableOrderedNodes.stream()
                .map(node -> node.replicate())
                .collect(Collectors.toCollection(() -> new ArrayList<>(composition.unmodifableOrderedNodes.size()))),
            composition.subjectEnumClass
        );
    }

    /**
     * Replicates the composition,
     * i.e. prepares a composition instance like this one.
     *
     * @return  composition instance like this one
     */
    public abstract Composition<SubjectT, InT> replicate();

    protected void processCalculations(InT inputArgument) {
        values.clear();
        unmodifableOrderedNodes.stream().forEach(n -> values.put(
            n.calculator.provides(),
            n.calculator.calculate(inputArgument, unmodifableValues)
        ));
    }
}
