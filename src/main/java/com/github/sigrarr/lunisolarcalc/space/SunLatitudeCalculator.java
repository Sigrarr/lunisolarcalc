package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class SunLatitudeCalculator {
    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 166 
     */
    public double calculateLatitude(double centurialT, double heliocentricLatitude, double heliocentricLongitude) {
        double basicLongitude = heliocentricLongitude + Math.PI;
        double lambdaPrim = calculateLambdaPrim(centurialT, basicLongitude);
        double basicToFK5DeltaArcseconds = 0.03916 * (Math.cos(lambdaPrim) - Math.sin(lambdaPrim));
        return -heliocentricLatitude + Math.toRadians(Calcs.arcsecondsToDegrees(basicToFK5DeltaArcseconds));
    }

    protected double calculateLambdaPrim(double centurialT, double basicLongitude) {
        return basicLongitude - Math.toRadians((1.397 * centurialT) + (0.00031 * centurialT * centurialT));
    }
}
