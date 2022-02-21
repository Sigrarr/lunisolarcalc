package com.github.sigrarr.lunisolarcalc.phenomena;

public enum MoonPhase {

    NEW_MOON(0.0),
    FIRST_QUARTER(0.5 * Math.PI),
    FULL_MOON(Math.PI),
    THIRD_QUARTER(1.5 * Math.PI);

    public final double moonOverSunApparentLongitudeExcess;

    private MoonPhase(double moonOverSunApparentLongitudeExcess) {
        this.moonOverSunApparentLongitudeExcess = moonOverSunApparentLongitudeExcess;
    }
}
