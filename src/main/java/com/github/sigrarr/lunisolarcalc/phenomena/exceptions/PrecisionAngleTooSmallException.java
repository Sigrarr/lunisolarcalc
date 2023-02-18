package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

public class PrecisionAngleTooSmallException extends IllegalArgumentException {

    private final double precisionRadians;

    public PrecisionAngleTooSmallException(double precisionRadians, double minAllowed) {
        super(
            "Angular precision must not be lesser than " + minAllowed + " radians,"
            + " " + precisionRadians + " given."
        );
        this.precisionRadians = precisionRadians;
    }

    public double getRadians() {
        return precisionRadians;
    }
}
