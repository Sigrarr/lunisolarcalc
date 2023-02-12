package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain Subject#SUN_HOUR_ANGLE the Sun's hour angle at the Greenwich meridian (H0)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class SunHourAngleCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_HOUR_ANGLE;

    /**
     * Calculates {@linkplain Subject#SUN_HOUR_ANGLE the Sun's hour angle at the Greenwich meridian (H0)}: [0, 360°).
     * Quick.
     *
     * @param siderealTimeDegrees   {@linkplain Subject#SIDEREAL_APPARENT_TIME sidereal time at the Greenwich meridian (θ0)}, in degrees
     * @param sunRightAscension     {@linkplain Subject#SUN_RIGHT_ASCENSION the Sun's right ascension (α)}, in radians
     * @return                      {@linkplain Subject#SUN_HOUR_ANGLE the Sun's hour angle at the Greenwich meridian (H0)},
     *                              in degrees: [0, 360°)
     */
    public double calculate(double siderealTimeDegrees, double sunRightAscension) {
        return Transformations.calculateHourAngle(siderealTimeDegrees, Math.toDegrees(sunRightAscension), 360.0);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SIDEREAL_APPARENT_TIME, Subject.SUN_RIGHT_ASCENSION);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.SIDEREAL_APPARENT_TIME),
            (Double) precalculatedValues.get(Subject.SUN_RIGHT_ASCENSION)
        );
    }
}
