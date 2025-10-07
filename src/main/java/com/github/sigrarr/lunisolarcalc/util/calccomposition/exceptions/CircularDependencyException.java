package com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions;

import java.util.*;
import java.util.stream.Collectors;

public final class CircularDependencyException extends IllegalStateException {

    private final List<?> unmodifableSubjectDependencyPath;

    public CircularDependencyException(Object[] subjectDependencyPath) {
        super(
            "Cannot compose calculation: Circular dependency detected: "
            + Arrays.stream(subjectDependencyPath).map(Object::toString).collect(Collectors.joining(" -> "))
        );
        unmodifableSubjectDependencyPath = Collections.unmodifiableList(Arrays.asList(subjectDependencyPath));
    }

    public List<?> getSubjectDependencyPath() {
        return unmodifableSubjectDependencyPath;
    }
}
