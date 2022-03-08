package com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions;

import java.util.*;
import java.util.stream.Collectors;

public final class ProviderLackException extends IllegalStateException {

    private final Collection<Enum<?>> missingSubjects;

    public ProviderLackException(Enum<?> subject) {
        super("No provider has been registered for the following subject: " + subject.name());
        missingSubjects = Collections.unmodifiableCollection(new ArrayList<Enum<?>>(1) {{ add(subject); }});
    }

    public ProviderLackException(Collection<Enum<?>> subjects) {
        super(
            "No providers have been registered for the following subjects: "
            + subjects.stream().map(Enum::name).collect(Collectors.joining(", "))
        );
        missingSubjects = Collections.unmodifiableCollection(subjects);
    }

    public Collection<Enum<?>> getMissingSubjects() {
        return missingSubjects;
    }
}
