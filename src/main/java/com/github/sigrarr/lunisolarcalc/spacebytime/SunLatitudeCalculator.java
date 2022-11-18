package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class SunLatitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_LATITUDE;

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 166
     */
    public double calculateLatitude(TimelinePoint tx, double heliocentricLatitude, double heliocentricLongitude) {
        double basicLongitude = heliocentricLongitude + Math.PI;
        double lambdaPrim = calculateLambdaPrim(tx.toDynamicalTime().toCenturialT(), basicLongitude);
        double basicToFK5DeltaArcseconds = 0.03916 * (Math.cos(lambdaPrim) - Math.sin(lambdaPrim));
        return -heliocentricLatitude + Math.toRadians(Calcs.arcsecondsToDegrees(basicToFK5DeltaArcseconds));
    }

    protected double calculateLambdaPrim(double centurialT, double basicLongitude) {
        return basicLongitude - Math.toRadians((1.397 * centurialT) + (0.00031 * centurialT * centurialT));
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_LATITUDE, Subject.EARTH_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateLatitude(
            tx,
            (Double) calculatedValues.get(Subject.EARTH_LATITUDE),
            (Double) calculatedValues.get(Subject.EARTH_LONGITUDE)
        );
    }
}
