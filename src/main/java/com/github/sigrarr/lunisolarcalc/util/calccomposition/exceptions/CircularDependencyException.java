package com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions;

import java.util.*;
import java.util.stream.Collectors;

public final class CircularDependencyException extends IllegalStateException {

    private final List<Enum<?>> unmodifableSubjectDependencyPath;

    public CircularDependencyException(List<Enum<?>> subjectDependencyPath) {
        super(
            "Cannot compose calculation: Circular dependency detected: "
            + subjectDependencyPath.stream().map(Enum::name).collect(Collectors.joining(" -> "))
        );
        unmodifableSubjectDependencyPath = Collections.unmodifiableList(subjectDependencyPath);
    }

    public List<Enum<?>> getSubjectDependencyPath() {
        return unmodifableSubjectDependencyPath;
    }
}
