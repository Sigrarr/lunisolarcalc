package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.PeriodicTermsForEarthLatitude;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class HeliocentricLatitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public HeliocentricLatitudeCalculator() {
        super(new PeriodicTermsForEarthLatitude());
    }

    public double calculate(double tau) {
        return Calcs.normalizeLatitudinally(super.calculate(tau));
    }
}
