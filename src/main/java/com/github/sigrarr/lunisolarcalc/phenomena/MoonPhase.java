package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public enum MoonPhase {

    NEW_MOON(0.0, "New Moon"),
    FIRST_QUARTER(0.25, "First Quarter"),
    FULL_MOON(0.5, "Full Moon"),
    THIRD_QUARTER(0.75, "Third Quarter");

    public final double lunationFraction;
    public final double moonOverSunApparentLongitudeExcess;
    public final String name;

    private MoonPhase(double lunationFraction, String name) {
        this.lunationFraction = lunationFraction;
        this.moonOverSunApparentLongitudeExcess = lunationFraction * Calcs.ROUND;
        this.name = name;
    }
}
