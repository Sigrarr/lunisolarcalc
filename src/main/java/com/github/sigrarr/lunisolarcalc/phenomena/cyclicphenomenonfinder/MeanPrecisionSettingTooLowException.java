package com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder;

public class MeanPrecisionSettingTooLowException extends IllegalArgumentException {

    private final int meanPrecisionSeconds;

    public MeanPrecisionSettingTooLowException(int meanPrecisionSeconds) {
        super("Mean precision must be a positive number of seconds, " + meanPrecisionSeconds + " given.");
        this.meanPrecisionSeconds = meanPrecisionSeconds;
    }

    public int getMeanPrecisionSeconds() {
        return meanPrecisionSeconds;
    }
}
