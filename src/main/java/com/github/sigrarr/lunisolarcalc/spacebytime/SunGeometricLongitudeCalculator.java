package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class SunGeometricLongitudeCalculator implements Provider<Subject, TimelinePoint> {
    /**
     * Meeus 1998, 25.9, p. 166
     */
    public static final double BASIC_TO_FK5_DELTA = Math.toRadians(Calcs.arcsecondsToDegrees(-0.09033));
    private static final double HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND = Math.PI + BASIC_TO_FK5_DELTA;

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 166 
     */
    public double calculateGeometricLongitude(double heliocentricLongitude) {
        return Calcs.normalizeLongitudinally(heliocentricLongitude + HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND);
    }

    @Override
    public Subject provides() {
        return Subject.SUN_GEOMETRIC_LONGITUDE;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateGeometricLongitude((Double) calculatedValues.get(Subject.EARTH_LONGITUDE));
    }
}
