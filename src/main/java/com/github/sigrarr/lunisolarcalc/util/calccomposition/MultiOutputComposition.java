package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A composed calculation yielding values of the quantities
 * represented by its "target subjects".
 * It is made of multiple {@linkplain Provider providers}.
 *
 * @param <SubjectT>    {@linkplain Enum enumeration type} of the quantities under calculation,
 *                      containing all available "subjects"
 * @param <InT>         type of an input argument
 * @see                 CalculationComposer
 */
public class MultiOutputComposition<SubjectT extends Enum<SubjectT>, InT> extends Composition<SubjectT, InT> {

    MultiOutputComposition(Collection<CompositionNode<SubjectT, InT>> orderedNodes, Class<SubjectT> subjectEnumClass) {
        super(orderedNodes, subjectEnumClass);
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public MultiOutputComposition(MultiOutputComposition<SubjectT, InT> composition) {
        super(composition);
    }

    @Override
    public MultiOutputComposition<SubjectT, InT> replicate() {
        return new MultiOutputComposition<>(this);
    }

    /**
     * Calculates values of the quantities represented by this composition's target subjects
     * and returns them in the form of a subject-value map.
     *
     * Objects stored in the map may require casting; each will be the same as
     * {@linkplain Provider#calculate(Object, Map) calculated} by the provider of its key subject.
     *
     * @param inputArgument     input argument (will be passed as a root input
     *                          to every {@linkplain Provider provider} belonging to this composition)
     * @return                  values of the quantities represented by this composition's target subjects,
     *                          in the form of a subject-value map (objects stored in the map may require
     *                          casting; each will be the same as
     *                          {@linkplain Provider#calculate(Object, Map) calculated} by the provider
     *                          of its key subject)
     */
    public Map<SubjectT, Object> calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return unmodifableOrderedNodes.stream()
            .filter(n -> n.isTarget)
            .collect(Collectors.toMap(
                n -> n.calculator.provides(),
                n -> unmodifableValues.get(n.calculator.provides()),
                (u, v) -> { throw new UnsupportedOperationException(); },
                () -> new EnumMap<>(subjectEnumClass)
            ));
    }
}
