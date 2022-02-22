package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class SunLatitudeCalculator implements Provider<Subject, Double> {
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

    @Override
    public Subject provides() {
        return Subject.SUN_LATITUDE;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_LATITUDE, Subject.EARTH_LONGITUDE);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> requiredArguments) {
        return calculateLatitude(
            centurialT,
            (Double) requiredArguments.get(Subject.EARTH_LATITUDE),
            (Double) requiredArguments.get(Subject.EARTH_LONGITUDE)
        );
    }
}
