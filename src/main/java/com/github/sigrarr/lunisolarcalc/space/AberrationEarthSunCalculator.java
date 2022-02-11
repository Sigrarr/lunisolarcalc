package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.SunLongitudeVariationPeriodicTerms;

public final class AberrationEarthSunCalculator {

    private static final double AU_LIGHT_TIME_DAYS = 0.005775518;
    private SunLongitudeVariationPeriodicTerms periodicTerms = new SunLongitudeVariationPeriodicTerms(); 
    private EarthSunRadiusCalculator radiusCalculator = new EarthSunRadiusCalculator();
    
    public double calculateAberration(double tau) {
        double radius = radiusCalculator.calculate(tau);
        double deltaLambda = periodicTerms.evaluate(tau);
        return -AU_LIGHT_TIME_DAYS * radius * deltaLambda;
    }
}
