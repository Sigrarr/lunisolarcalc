package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.SunLongitudeVariationPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.ConstantsAndUnits;

public final class AberrationEarthSunCalculator {

    private static final double AU_LIGHT_TIME_DAYS = (
        (double) ConstantsAndUnits.ASTRONOMICAL_UNIT_METERS / (double) ConstantsAndUnits.LIGHT_SPEED_METERS_PER_SECOND
    ) / 3600.0 / 24.0;
    private SunLongitudeVariationPeriodicTerms periodicTerms = new SunLongitudeVariationPeriodicTerms(); 
    private EarthSunRadiusCalculator radiusCalculator = new EarthSunRadiusCalculator();
    
    public double calculateAberration(double tau) {
        double radius = radiusCalculator.calculate(tau);
        double deltaLambda = periodicTerms.evaluate(tau);
        return -AU_LIGHT_TIME_DAYS * radius * deltaLambda;
    }
}
