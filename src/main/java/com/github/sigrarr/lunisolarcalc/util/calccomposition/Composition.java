package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Composition<SubjectT extends Enum<SubjectT>, InT> {

    protected final Collection<CompositionNode<SubjectT, InT>> unmodifableOrderedNodes;
    protected final Class<SubjectT> subjectEnumClass;
    private final Map<SubjectT, Object> values;
    protected final Map<SubjectT, Object> unmodifableValues;

    Composition(Collection<CompositionNode<SubjectT, InT>> orderedNodes, Class<SubjectT> subjectEnumClass) {
        unmodifableOrderedNodes = Collections.unmodifiableCollection(orderedNodes);
        this.subjectEnumClass = subjectEnumClass;
        values = new EnumMap<>(subjectEnumClass);
        unmodifableValues = Collections.unmodifiableMap(values);
    }

    public Composition(Composition<SubjectT, InT> composition) {
        this(
            composition.unmodifableOrderedNodes.stream()
                .map(node -> node.replicate())
                .collect(Collectors.toCollection(() -> new ArrayList<>(composition.unmodifableOrderedNodes.size()))),
            composition.subjectEnumClass
        );
    }

    public abstract Composition<SubjectT, InT> replicate();

    protected void processCalculations(InT inputArgument) {
        values.clear();
        unmodifableOrderedNodes.stream().forEach(n -> values.put(
            n.calculator.provides(),
            n.calculator.calculate(inputArgument, unmodifableValues)
        ));
    }
}
