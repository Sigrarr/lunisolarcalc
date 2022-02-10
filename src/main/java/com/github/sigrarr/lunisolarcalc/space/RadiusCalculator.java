package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.PeriodicTermsForEarthRadius;

public class RadiusCalculator extends HeliocentricCoordinateCalculator {
    public RadiusCalculator() {
        super(new PeriodicTermsForEarthRadius());
    }
}
