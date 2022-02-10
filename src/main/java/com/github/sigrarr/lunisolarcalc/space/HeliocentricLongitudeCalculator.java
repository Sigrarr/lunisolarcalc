package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.PeriodicTermsForEarthLongitude;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class HeliocentricLongitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public HeliocentricLongitudeCalculator() {
        super(new PeriodicTermsForEarthLongitude());
    }

    public double calculate(double tau) {
        return Calcs.normalizeLongitudinally(super.calculate(tau));
    }
}
