package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain Subject#MOON_HOUR_ANGLE the Moon's hour angle at the Greenwich meridian (H0)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class MoonHourAngleCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_HOUR_ANGLE;

    /**
     * Calculates {@linkplain Subject#MOON_HOUR_ANGLE the Moon's hour angle at the Greenwich meridian (H0)}: [-π/2°, +π/2°).
     * Quick.
     *
     * @param siderealTimeDegrees   {@linkplain Subject#SIDEREAL_APPARENT_TIME sidereal time at the Greenwich meridian (θ0)}, in degrees
     * @param moonRightAscension    {@linkplain Subject#MOON_RIGHT_ASCENSION the Moon's right ascension (α)}, in radians
     * @return                      {@linkplain Subject#MOON_HOUR_ANGLE the Moon's hour angle at the Greenwich meridian (H0)},
     *                              in radians: [-π/2°, +π/2°)
     */
    public double calculate(double siderealTimeDegrees, double moonRightAscension) {
        return Transformations.calculateHourAngle(Math.toRadians(siderealTimeDegrees), moonRightAscension);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SIDEREAL_APPARENT_TIME, Subject.MOON_RIGHT_ASCENSION);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.SIDEREAL_APPARENT_TIME),
            (Double) precalculatedValues.get(Subject.MOON_RIGHT_ASCENSION)
        );
    }
}
