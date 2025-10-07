package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

abstract class CalcCompositionAbstract<KeyT, InT> {

    protected final List<CompositionNode<KeyT, InT>> unmodifableOrderedNodes;
    private final Map<KeyT, Object> values = new HashMap<>();
    protected final Map<KeyT, Object> unmodifableValues;

    CalcCompositionAbstract(List<CompositionNode<KeyT, InT>> orderedNodes) {
        unmodifableOrderedNodes = Collections.unmodifiableList(orderedNodes);
        unmodifableValues = Collections.unmodifiableMap(values);
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public CalcCompositionAbstract(CalcCompositionAbstract<KeyT, InT> composition) {
        this(
            composition.unmodifableOrderedNodes.stream()
                .map(node -> node.replicate())
                .collect(Collectors.toCollection(() -> new ArrayList<>(composition.unmodifableOrderedNodes.size())))
        );
    }

    /**
     * Replicates the composition,
     * i.e. prepares a composition instance like this one.
     *
     * @return  composition instance like this one
     */
    public abstract CalcCompositionAbstract<KeyT, InT> replicate();

    protected void processCalculations(InT inputArgument) {
        values.clear();
        unmodifableOrderedNodes.forEach(n -> values.put(
            n.calculator.provides(),
            n.calculator.calculate(inputArgument, unmodifableValues)
        ));
    }
}
