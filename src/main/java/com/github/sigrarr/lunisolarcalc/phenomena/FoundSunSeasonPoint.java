package com.github.sigrarr.lunisolarcalc.phenomena;

public class FoundSunSeasonPoint {

    public final double julianEphemerisDay;
    public final SunSeasonPoint sunSeasonPoint;

    public FoundSunSeasonPoint(double julianEphemerisDay, SunSeasonPoint sunSeasonPoint) {
        this.julianEphemerisDay = julianEphemerisDay;
        this.sunSeasonPoint = sunSeasonPoint;
    }
}
