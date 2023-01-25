package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Sun's geometric longitude (☉).
 * Given required parameters, it's in itself quick.
 * Stateless, {@link CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcComposition}.
 *
 * @see " Meeus 1998: Ch. 25 (Higher accuracy, p. 166)
 */
public final class SunGeometricLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_GEOMETRIC_LONGITUDE;
    public static final double BASIC_TO_FK5_DELTA = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(-0.09033));
    private static final double HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND = Math.PI + BASIC_TO_FK5_DELTA;

    /**
     * Calculates the Sun's geometric longitude (☉): [0, 2π).
     * Quick.
     *
     * @param heliocentricLongitude {@link EarthLongitudeCalculator the Earth's heliocentric longitude} (L), in radians
     * @return                      the Sun's geometric longitude (☉): [0, 2π)
     */
    public double calculate(double heliocentricLongitude) {
        return Calcs.Angle.normalizeLongitudinally(heliocentricLongitude + HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate((Double) precalculatedValues.get(Subject.EARTH_LONGITUDE));
    }
}
