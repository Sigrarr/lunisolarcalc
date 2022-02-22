package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

public class MultiOutputComposition<SubjectT extends Enum<SubjectT>, InT> extends Composition<SubjectT, InT> {

    MultiOutputComposition(Collection<CompositionNode<SubjectT, InT>> orderedNodes, Class<SubjectT> subjectEnumClass) {
        super(orderedNodes, subjectEnumClass);
    }

    public MultiOutputComposition(MultiOutputComposition<SubjectT, InT> composition) {
        super(composition);
    }

    @Override
    public MultiOutputComposition<SubjectT, InT> replicate() {
        return new MultiOutputComposition<>(this);
    }

    public Map<SubjectT, Object> calculate(InT inputArgument) {
        processCalculations(inputArgument);
        return unmodifableOrderedNodes.stream()
            .filter(n -> n.isTarget)
            .collect(Collectors.toMap(
                n -> n.calculator.provides(),
                n -> unmodifableValues.get(n.calculator.provides()),
                (u, v) -> { throw new UnsupportedOperationException(); },
                () -> new EnumMap<>(subjectEnumClass)
            ));
    }
}
