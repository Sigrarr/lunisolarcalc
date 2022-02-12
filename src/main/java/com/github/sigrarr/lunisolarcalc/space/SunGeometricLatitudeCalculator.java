package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class SunGeometricLatitudeCalculator {
    
    private EarthLatitudeCalculator heliocentricCalculator = new EarthLatitudeCalculator();
    private EarthLongitudeCalculator heliocentricLongitudeCalculator = new EarthLongitudeCalculator();

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 166 
     */
    public double calculateGeometricLatitude(double tau) {
        double basicValue = -heliocentricCalculator.calculate(tau);
        double basicLongitude = heliocentricLongitudeCalculator.calculate(tau) + Math.PI;
        double lambdaPrim = calculateLambdaPrim(basicLongitude, Timeline.millenialTauToCenturialT(tau));
        double basicToFK5DeltaArcseconds = 0.03916 * (Math.cos(lambdaPrim) - Math.sin(lambdaPrim));
        return basicValue + Math.toRadians(Calcs.arcsecondsToDegrees(basicToFK5DeltaArcseconds));
    }

    protected double calculateLambdaPrim(double basicLongitude, double centurialT) {
        return basicLongitude - Math.toRadians((1.397 * centurialT) + (0.00031 * centurialT * centurialT));
    }
}
