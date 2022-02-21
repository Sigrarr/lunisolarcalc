package com.github.sigrarr.lunisolarcalc.phenomena;

public enum SunSeasonPoint {

    MARCH_EQUINOX(0.0),
    JUNE_SOLSTICE(0.5 * Math.PI),
    SEPTEMBER_EQUINOX(Math.PI),
    DECEMBER_SOLSTICE(1.5 * Math.PI);

    public final double apparentLongitude;

    private SunSeasonPoint(double apparentLongitude) {
        this.apparentLongitude = apparentLongitude;
    }
}
