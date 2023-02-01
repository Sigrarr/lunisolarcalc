package com.github.sigrarr.lunisolarcalc.time.exceptions;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EquivUnitTooSmallException extends IllegalArgumentException {

    private final double equivUnitDays;

    public EquivUnitTooSmallException(double equivUnitDays, double minAllowed) {
        super(
            "The timeline's equivalence unit must not be smaller than " + minAllowed + " days"
            + " (" + String.format("%1d", (int) Calcs.roundToDelta(minAllowed * Calcs.DAY_SECONDS * 1000, 0.01)) + " milisecond(s))"
            + ", " + equivUnitDays + " days given."
        );
        this.equivUnitDays = equivUnitDays;
    }

    public double getEquivUnitDays() {
        return equivUnitDays;
    }
}
