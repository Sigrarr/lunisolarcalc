package com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder;

public class EpsilonTimeSettingTooLowException extends IllegalArgumentException {

    private final int epsilonSeconds;

    public EpsilonTimeSettingTooLowException(int epsilonSeconds) {
        super("Epsilon time must be a positive number of seconds, " + epsilonSeconds + " given.");
        this.epsilonSeconds = epsilonSeconds;
    }

    public int getSeconds() {
        return epsilonSeconds;
    }
}
