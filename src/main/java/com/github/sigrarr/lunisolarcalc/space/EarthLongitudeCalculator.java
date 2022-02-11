package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLongitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public EarthLongitudeCalculator() {
        super(new EarthLongitudePeriodicTerms());
    }

    public double calculate(double tau) {
        return Calcs.normalizeLongitudinally(super.calculate(tau));
    }
}
