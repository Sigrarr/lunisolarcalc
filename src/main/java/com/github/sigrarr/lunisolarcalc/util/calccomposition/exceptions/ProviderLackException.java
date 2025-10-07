package com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions;

import java.util.*;
import java.util.stream.Collectors;

public final class ProviderLackException extends IllegalStateException {

    private final Collection<?> missingSubjects;

    public ProviderLackException(Object subject) {
        super("No provider has been registered for the following subject: " + subject.toString());
        missingSubjects = Collections.unmodifiableCollection(new ArrayList<Object>(1) {{ add(subject); }});
    }

    public ProviderLackException(Collection<?> subjects) {
        super(
            "No providers have been registered for the following subjects: "
            + subjects.stream().map(Object::toString).collect(Collectors.joining(", "))
        );
        missingSubjects = Collections.unmodifiableCollection(subjects);
    }

    public Collection<?> getMissingSubjects() {
        return missingSubjects;
    }
}
