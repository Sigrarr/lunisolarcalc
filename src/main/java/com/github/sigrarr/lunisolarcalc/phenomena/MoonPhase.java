package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public enum MoonPhase {

    NEW_MOON(0.0),
    FIRST_QUARTER(0.25),
    FULL_MOON(0.5),
    THIRD_QUARTER(0.75);

    public final double lunationFraction;
    public final double moonOverSunApparentLongitudeExcess;

    private MoonPhase(double lunationFraction) {
        this.lunationFraction = lunationFraction;
        this.moonOverSunApparentLongitudeExcess = lunationFraction * Calcs.ROUND;
    }
}
