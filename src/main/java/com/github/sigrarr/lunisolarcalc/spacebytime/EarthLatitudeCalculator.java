package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLatitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public EarthLatitudeCalculator() {
        super(new EarthLatitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(double tau) {
        return Calcs.normalizeLatitudinally(super.calculateCoordinate(tau));
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_LATITUDE;
    }
}
