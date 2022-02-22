package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

public class SingleOutputComposition<SubjectT extends Enum<SubjectT>, InT> extends Composition<SubjectT, InT> {

    SingleOutputComposition(Collection<CompositionNode<SubjectT, InT>> orderedNodes, Class<SubjectT> subjectEnumClass) {
        super(orderedNodes, subjectEnumClass);
    }

    public SingleOutputComposition(SingleOutputComposition<SubjectT, InT> composition) {
        super(composition);
    }

    @Override
    public SingleOutputComposition<SubjectT, InT> replicate() {
        return new SingleOutputComposition<>(this);
    }

    public Object calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return unmodifableOrderedNodes.stream()
            .filter(n -> n.isTarget)
            .map(n -> unmodifableValues.get(n.calculator.provides()))
            .findFirst().get();
    }
}
