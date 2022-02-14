package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLongitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public EarthLongitudeCalculator() {
        super(new EarthLongitudePeriodicTerms());
    }

    public double calculateCoordinate(double tau) {
        return Calcs.normalizeLongitudinally(super.calculateCoordinate(tau));
    }
}
