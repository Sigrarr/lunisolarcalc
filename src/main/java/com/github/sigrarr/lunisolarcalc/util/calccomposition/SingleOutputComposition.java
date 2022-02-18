package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.TreeSet;

public class SingleOutputComposition<E extends Enum<E>, InT, OutT> extends Composition<E, InT, OutT> {

    private final E target;
    private OutT result;

    SingleOutputComposition(E target, TreeSet<CompositionNode<E, InT, OutT>> orderedNodes, Class<OutT> outpuClass) {
        super(orderedNodes, outpuClass);
        this.target = target;
    }

    public SingleOutputComposition(SingleOutputComposition<E, InT, OutT> composition) {
        super(composition);
        this.target = composition.target;
    }

    @Override
    public SingleOutputComposition<E, InT, OutT> replicate() {
        return new SingleOutputComposition<>(this);
    }

    public OutT calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return result;
    }

    @Override
    protected final boolean isResultSubject(E subject) {
        return target == subject;
    }

    @Override
    protected final void setResult(E subject, OutT value) {
        result = value;
    }
}
