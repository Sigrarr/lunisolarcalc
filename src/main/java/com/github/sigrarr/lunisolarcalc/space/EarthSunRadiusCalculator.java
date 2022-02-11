package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.EarthSunRadiusPeriodicTerms;

public final class EarthSunRadiusCalculator extends HeliocentricCoordinateCalculator {
    public EarthSunRadiusCalculator() {
        super(new EarthSunRadiusPeriodicTerms());
    }
}
