package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class SunApparentLongitudeCalculator {
    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 167 
     */
    public double calculateApparentLongitude(double geometricLongitude, double nutuationInLongitude, double aberration) {
        return Calcs.normalizeLongitudinally(geometricLongitude + nutuationInLongitude + aberration);
    }

    public double calculateApparentLongitude(double aberratedLongitude, double nutuationInLongitude) {
        return Calcs.normalizeLongitudinally(aberratedLongitude + nutuationInLongitude);
    }
}
