package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class MoonApparentLongitudeCalculator {
    /**
     * Meeus 1998, Example 47.a, p. 343
     */
    public double calculateApparentLongitude(double longitude, double earthNutuationInLongitude) {
        return Calcs.normalizeLongitudinally(longitude + earthNutuationInLongitude);
    }
}
