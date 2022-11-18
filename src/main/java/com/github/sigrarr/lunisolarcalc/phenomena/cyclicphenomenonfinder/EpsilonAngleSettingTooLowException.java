package com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder;

public class EpsilonAngleSettingTooLowException extends IllegalArgumentException {

    private final double epsilonRadians;

    public EpsilonAngleSettingTooLowException(double epsilonRadians) {
        super("Epsilon of value " + epsilonRadians + " rad is too small.");
        this.epsilonRadians = epsilonRadians;
    }

    public double getRadians() {
        return epsilonRadians;
    }
}
