package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

public class DeltaTimeNotPositiveException extends IllegalArgumentException {

    private final int deltaSeconds;

    public DeltaTimeNotPositiveException(int deltaSeconds) {
        super("Delta time must be a positive number of seconds, " + deltaSeconds + " given.");
        this.deltaSeconds = deltaSeconds;
    }

    public int getSeconds() {
        return deltaSeconds;
    }
}
