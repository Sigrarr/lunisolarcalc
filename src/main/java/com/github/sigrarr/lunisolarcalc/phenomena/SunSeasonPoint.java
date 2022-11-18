package com.github.sigrarr.lunisolarcalc.phenomena;

public enum SunSeasonPoint {

    MARCH_EQUINOX(0.0, "March Equinox"),
    JUNE_SOLSTICE(0.5 * Math.PI, "June Solstice"),
    SEPTEMBER_EQUINOX(Math.PI, "September Equinox"),
    DECEMBER_SOLSTICE(1.5 * Math.PI, "December Sosltice");

    public final double apparentLongitude;
    public final String name;

    private SunSeasonPoint(double apparentLongitude, String name) {
        this.apparentLongitude = apparentLongitude;
        this.name = name;
    }
}
