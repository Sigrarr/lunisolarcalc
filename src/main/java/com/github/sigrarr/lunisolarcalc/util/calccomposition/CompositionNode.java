package com.github.sigrarr.lunisolarcalc.util.calccomposition;

final class CompositionNode<SubjectT extends Enum<SubjectT>, InT> extends Node<SubjectT, InT> implements Comparable<CompositionNode<SubjectT, InT>> {

    protected final int id;
    protected final boolean isTarget;
    protected int weight = 1;

    CompositionNode(Provider<SubjectT, InT> calculator, int id, boolean isTarget) {
        super(calculator);
        this.id = id;
        this.isTarget = isTarget;
    }

    CompositionNode(CompositionNode<SubjectT, InT> node) {
        super(node.calculator.getInstanceForNewComposition());
        this.id = node.id;
        this.isTarget = node.isTarget;
    }

    public CompositionNode<SubjectT, InT> replicate() {
        return new CompositionNode<>(this);
    }
   
    @Override
    public int compareTo(CompositionNode<SubjectT, InT> node) {
        int weightCmp = Integer.compare(node.weight, weight);
        if (weightCmp != 0) {
            return weightCmp;
        }
        return Integer.compare(node.id, id);
    }
}
