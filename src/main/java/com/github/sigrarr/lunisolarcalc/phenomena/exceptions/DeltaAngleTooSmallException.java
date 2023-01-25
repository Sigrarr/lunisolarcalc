package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

public class DeltaAngleTooSmallException extends IllegalArgumentException {

    private final double deltaRadians;

    public DeltaAngleTooSmallException(double deltaRadians, double minAllowed) {
        super(
            "Angular delta must not be lesser than " + minAllowed + " radians,"
            + " " + deltaRadians + " given."
        );
        this.deltaRadians = deltaRadians;
    }

    public double getRadians() {
        return deltaRadians;
    }
}
