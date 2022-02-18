package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

public class MultiOutputComposition<E extends Enum<E>, InT, OutT> extends Composition<E, InT, OutT> {

    private final EnumSet<E> targets;
    private Map<E, OutT> results = new HashMap<>();

    MultiOutputComposition(EnumSet<E> targets, TreeSet<CompositionNode<E, InT, OutT>> orderedNodes, Class<OutT> outputClass) {
        super(orderedNodes, outputClass);
        this.targets = targets;
    }

    public MultiOutputComposition(MultiOutputComposition<E, InT, OutT> composition) {
        super(composition);
        this.targets = composition.targets;
    }

    @Override
    public MultiOutputComposition<E, InT, OutT> replicate() {
        return new MultiOutputComposition<>(this);
    }

    public Map<E, OutT> calculate(InT inputArgument) {
        results.clear();
        processCalculations(inputArgument);
        return results;
    }

    @Override
    protected final boolean isResultSubject(E subject) {
        return targets.contains(subject);
    }

    @Override
    protected final void setResult(E subject, OutT value) {
        results.put(subject, value);
    }
}
