package com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class DoubledProviderException extends UnsupportedOperationException {

    private final Enum<?> subject;
    private final Provider<?, ?> registeredProvider;
    private final Provider<?, ?> rejectedProvider;

    public DoubledProviderException(Enum<?> subject, Provider<?, ?> registeredProvider, Provider<?, ?> rejectedProvider) {
        super(
            "Registering more than one provider for one subject is not supported."
            + " Cannot register " + rejectedProvider.getClass().getName() + ";"
            + " provider of " + subject.name() + " has been already registered: " + registeredProvider.getClass().getName()
        );
        this.subject = subject;
        this.registeredProvider = registeredProvider;
        this.rejectedProvider = rejectedProvider;
    }

    public Enum<?> getSubject() {
        return subject;
    }

    public Provider<?, ?> getRegisteredProvider() {
        return registeredProvider;
    }

    public Provider<?, ?> getRejectedProvider() {
        return rejectedProvider;
    }
}
