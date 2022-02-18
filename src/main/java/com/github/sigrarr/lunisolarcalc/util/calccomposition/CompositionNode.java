package com.github.sigrarr.lunisolarcalc.util.calccomposition;

class CompositionNode<E extends Enum<E>, InT, OutT> extends Node<E, InT, OutT> implements Comparable<CompositionNode<E, InT, OutT>> {

    protected final int ordinal;
    protected int weight = 1;

    CompositionNode(Provider<E, InT, OutT> calculator, int ordinal) {
        super(calculator);
        this.ordinal = ordinal;
    }

    CompositionNode(CompositionNode<E, InT, OutT> node) {
        super(node.calculator.getInstanceForNewComposition());
        this.ordinal = node.ordinal;
    }

    public CompositionNode<E, InT, OutT> replicate() {
        return new CompositionNode<>(this);
    }

    @Override
    public int compareTo(CompositionNode<E, InT, OutT> node) {
        int weightCmp = Integer.compare(node.weight, weight);
        if (weightCmp != 0) {
            return weightCmp;
        }
        return Integer.compare(node.ordinal, ordinal);
    }
}
