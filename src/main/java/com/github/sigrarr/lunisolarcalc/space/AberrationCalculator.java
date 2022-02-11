package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.PeriodicTermsForSunLongitudeVariation;

public class AberrationCalculator {

    private static final double AU_LIGHT_TIME_DAYS = 0.005775518;
    private PeriodicTermsForSunLongitudeVariation periodicTerms = new PeriodicTermsForSunLongitudeVariation(); 
    private RadiusCalculator radiusCalculator = new RadiusCalculator();
    
    public double calculateAberration(double tau) {
        double radius = radiusCalculator.calculate(tau);
        double deltaLambda = periodicTerms.evaluate(tau);
        return -AU_LIGHT_TIME_DAYS * radius * deltaLambda;
    }
}
