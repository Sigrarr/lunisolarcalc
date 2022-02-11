package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.EarthLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLatitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public EarthLatitudeCalculator() {
        super(new EarthLatitudePeriodicTerms());
    }

    public double calculate(double tau) {
        return Calcs.normalizeLatitudinally(super.calculate(tau));
    }
}
