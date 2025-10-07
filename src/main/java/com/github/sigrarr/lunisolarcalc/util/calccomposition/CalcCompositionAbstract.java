package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

abstract class CalcCompositionAbstract<KeyT, InT> {

    protected final List<CompositionNode<KeyT, InT>> unmodifableOrderedNodes;
    private final Map<KeyT, Object> values;
    protected final Map<KeyT, Object> unmodifableValues;
    protected final KeyUtil<KeyT> ku;

    CalcCompositionAbstract(List<CompositionNode<KeyT, InT>> orderedNodes, KeyUtil<KeyT> ku) {
        unmodifableOrderedNodes = Collections.unmodifiableList(orderedNodes);
        values = ku.getMap();
        unmodifableValues = Collections.unmodifiableMap(values);
        this.ku = ku;
    }

    /**
     * Constructs a copy of another composition.
     *
     * @param composition   composition to copy
     */
    public CalcCompositionAbstract(CalcCompositionAbstract<KeyT, InT> composition) {
        this(
            composition.unmodifableOrderedNodes.stream()
                .map(CompositionNode::replicate)
                .collect(Collectors.toCollection(() -> new ArrayList<>(composition.unmodifableOrderedNodes.size()))),
            composition.ku
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
