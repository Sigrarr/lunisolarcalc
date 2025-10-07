package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * A composed calculation yielding values of one quantity ("target").
 * It is made of multiple {@linkplain Provider providers}.
 *
 * @param <KeyT>    type of key identifying the quantity under calculation
 * @param <InT>     type of an input argument
 * @see             CalculationComposer
 */
public class CalcComposition<KeyT, InT> extends CalcCompositionAbstract<KeyT, InT> {

    CalcComposition(List<CompositionNode<KeyT, InT>> orderedNodes) {
        super(orderedNodes);
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public CalcComposition(CalcComposition<KeyT, InT> composition) {
        super(composition);
    }

    @Override
    public CalcComposition<KeyT, InT> replicate() {
        return new CalcComposition<>(this);
    }

    /**
     * Calculates a value of the target quantity.
     *
     * Returned object may require casting; it will be the same as
     * {@linkplain Provider#calculate(Object, Map) calculated} by the provider of the target quantity.
     *
     * @param inputArgument     input argument (will be passed as a root input
     *                          to every {@linkplain Provider provider} belonging to this composition)
     * @return                  value of the target quantity (may require casting; it will be
     *                          the same as {@linkplain Provider#calculate(Object, Map) calculated}
     *                          by the provider of the target subject)
     */
    public Object calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return unmodifableOrderedNodes.stream()
            .filter(n -> n.isTarget)
            .map(n -> unmodifableValues.get(n.calculator.provides()))
            .findFirst().get();
    }
}
