package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class SunAberratedLongitudeCalculator {

    public double calculateAberratedLongitude(double geometricLongitude, double aberration) {
        return Calcs.normalizeLongitudinally(geometricLongitude + aberration);
    }
}
