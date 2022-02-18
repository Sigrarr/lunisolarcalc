package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Composition<E extends Enum<E>, InT, OutT> {

    private final Collection<CompositionNode<E, InT, OutT>> orderedNodes;
    private final Class<OutT> outputClass;
    private final Map<E, Object> values = new HashMap<>();
    private final Map<E, Object> readonlyValues = Collections.unmodifiableMap(values);

    Composition(Collection<CompositionNode<E, InT, OutT>> orderedNodes, Class<OutT> outputClass) {
        this.orderedNodes = orderedNodes;
        this.outputClass = outputClass;
    }

    public Composition(Composition<E, InT, OutT> composition) {
        this(
            composition.orderedNodes.stream().map(node -> node.replicate()).collect(Collectors.toList()),
            composition.outputClass
        );
    }

    public abstract Composition<E, InT, OutT> replicate();

    protected void processCalculations(InT inputArgument) {
        values.clear();
        for (CompositionNode<E, InT, OutT> node : orderedNodes) {
            E subject = node.calculator.provides();
            Object value = node.calculator.calculate(inputArgument, readonlyValues);
            values.put(subject, value);
            if (isResultSubject(subject)) {
                handleResult(subject, value);
            }
        }
    }

    protected void handleResult(E subject, Object value) {
        if (outputClass.isInstance(value)) {
            @SuppressWarnings("unchecked") OutT resultValue = (OutT) value;
            setResult(subject, resultValue);
        } else {
            throw new IllegalArgumentException(
                subject + " is a composition target so its provider must return instances of " + outputClass.getName() + "."
                + " " + (value == null ? "NULL" : "Instance of " + value.getClass().getName()) + " given instead."                
            );
        }
    }

    abstract protected void setResult(E subject, OutT value);
    abstract protected boolean isResultSubject(E subject);
}
