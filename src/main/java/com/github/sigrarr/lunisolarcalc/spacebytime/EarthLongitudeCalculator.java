package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLongitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public EarthLongitudeCalculator() {
        super(new EarthLongitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(double tau) {
        return Calcs.normalizeLongitudinally(super.calculateCoordinate(tau));
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_LONGITUDE;
    }
}
