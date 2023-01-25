package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * A composed calculation yielding values of one quantity
 * represented by its "target subject".
 * It is made of multiple {@link Provider providers}.
 *
 * @param <SubjectT>    {@link Enum enumeration type} of the quantities under calculation,
 *                      containing all available "subjects"
 * @param <InT>         type of an input argument
 * @see                 CalculationComposer
 */
public class SingleOutputComposition<SubjectT extends Enum<SubjectT>, InT> extends Composition<SubjectT, InT> {

    SingleOutputComposition(Collection<CompositionNode<SubjectT, InT>> orderedNodes, Class<SubjectT> subjectEnumClass) {
        super(orderedNodes, subjectEnumClass);
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public SingleOutputComposition(SingleOutputComposition<SubjectT, InT> composition) {
        super(composition);
    }

    @Override
    public SingleOutputComposition<SubjectT, InT> replicate() {
        return new SingleOutputComposition<>(this);
    }

    /**
     * Calculates a value of the quantity represented by this composition's target subject.
     *
     * Returned object may require casting; it will be the same as
     * {@link Provider#calculate(Object, Map) calculated} by the provider of the target subject.
     *
     * @param inputArgument     input argument (will be passed as a root input
     *                          to every {@link Provider provider} belonging to this composition)
     * @return                  value of the quantity represented by this composition's target subject
     *                          (may require casting; it will be the same as
     *                          {@link Provider#calculate(Object, Map) calculated} by the provider
     *                          of the target subject)
     */
    public Object calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return unmodifableOrderedNodes.stream()
            .filter(n -> n.isTarget)
            .map(n -> unmodifableValues.get(n.calculator.provides()))
            .findFirst().get();
    }
}
