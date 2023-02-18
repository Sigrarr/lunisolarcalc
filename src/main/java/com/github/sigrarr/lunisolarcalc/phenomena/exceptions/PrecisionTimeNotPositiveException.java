package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

public class PrecisionTimeNotPositiveException extends IllegalArgumentException {

    private final int precisionSeconds;

    public PrecisionTimeNotPositiveException(int precisionSeconds) {
        super("Precision time must be a positive number of seconds, " + precisionSeconds + " given.");
        this.precisionSeconds = precisionSeconds;
    }

    public int getSeconds() {
        return precisionSeconds;
    }
}
