package com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class DoubledProviderException extends UnsupportedOperationException {

    private final Object key;
    private final Provider<?, ?> registeredProvider;
    private final Provider<?, ?> rejectedProvider;

    public DoubledProviderException(Object key, Provider<?, ?> registeredProvider, Provider<?, ?> rejectedProvider) {
        super(
            "Registering more than one provider for one subject is not supported (yet?)."
            + " Cannot register " + rejectedProvider.getClass().getName() + ";"
            + " provider of " + key.toString() + " has been already registered: " + registeredProvider.getClass().getName()
        );
        this.key = key;
        this.registeredProvider = registeredProvider;
        this.rejectedProvider = rejectedProvider;
    }

    public Object getKey() {
        return key;
    }

    public Provider<?, ?> getRegisteredProvider() {
        return registeredProvider;
    }

    public Provider<?, ?> getRejectedProvider() {
        return rejectedProvider;
    }
}
