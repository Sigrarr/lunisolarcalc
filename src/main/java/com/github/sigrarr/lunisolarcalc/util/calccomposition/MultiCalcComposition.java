package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A composed calculation yielding values of the set of quantities ("targets").
 * It is made of multiple {@linkplain Provider providers}.
 *
 * @param <KeyT>    type of keys identifying the quantities under calculation
 * @param <InT>     type of an input argument
 * @see             CalculationComposer
 */
public class MultiCalcComposition<KeyT, InT> extends CalcCompositionAbstract<KeyT, InT> {

    MultiCalcComposition(List<CompositionNode<KeyT, InT>> orderedNodes) {
        super(orderedNodes);
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public MultiCalcComposition(MultiCalcComposition<KeyT, InT> composition) {
        super(composition);
    }

    @Override
    public MultiCalcComposition<KeyT, InT> replicate() {
        return new MultiCalcComposition<>(this);
    }

    /**
     * Calculates values of the target quantities
     * and returns them in the form of a map.
     *
     * Objects stored in the map may require casting; each will be the same as
     * {@linkplain Provider#calculate(Object, Map) calculated} by the provider of its key quantity.
     *
     * @param inputArgument     input argument (will be passed as a root input
     *                          to every {@linkplain Provider provider} belonging to this composition)
     * @return                  values of the target quantities, in the form of a map
     *                          (objects stored in the map may require casting; each will be the same
     *                          as {@linkplain Provider#calculate(Object, Map) calculated} by the provider
     *                          of its key quantity)
     */
    public Map<KeyT, Object> calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return unmodifableOrderedNodes.stream()
            .filter(n -> n.isTarget)
            .collect(Collectors.toMap(
                n -> n.calculator.provides(),
                n -> unmodifableValues.get(n.calculator.provides()),
                (u, v) -> { throw new UnsupportedOperationException(); },
                () -> new HashMap<>()
            ));
    }
}
