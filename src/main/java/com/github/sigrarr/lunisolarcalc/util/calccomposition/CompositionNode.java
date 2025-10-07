package com.github.sigrarr.lunisolarcalc.util.calccomposition;

final class CompositionNode<KeyT, InT> extends Node<KeyT, InT> implements Comparable<CompositionNode<KeyT, InT>> {

    protected final int id;
    protected boolean isTarget;
    protected int weight = 1;

    CompositionNode(Provider<KeyT, InT> calculator, int id, boolean isTarget) {
        super(calculator);
        this.id = id;
        this.isTarget = isTarget;
    }

    CompositionNode(CompositionNode<KeyT, InT> node) {
        super(node.calculator.getInstanceForNewComposition());
        this.id = node.id;
        this.isTarget = node.isTarget;
    }

    public CompositionNode<KeyT, InT> replicate() {
        return new CompositionNode<>(this);
    }

    @Override
    public int compareTo(CompositionNode<KeyT, InT> node) {
        int weightCmp = Integer.compare(node.weight, weight);
        if (weightCmp != 0) {
            return weightCmp;
        }
        return Integer.compare(node.id, id);
    }
}
