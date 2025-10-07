package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Transformations;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#MOON_HOUR_ANGLE the Moon's hour angle at the Greenwich meridian (H0)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class MoonHourAngleCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.MOON_HOUR_ANGLE;

    /**
     * Calculates {@linkplain GlobalCoord#MOON_HOUR_ANGLE the Moon's hour angle at the Greenwich meridian (H0)}: [-π/2°, +π/2°).
     * Quick.
     *
     * @param siderealTimeDegrees   {@linkplain GlobalCoord#SIDEREAL_APPARENT_TIME sidereal time at the Greenwich meridian (θ0)}, in degrees
     * @param moonRightAscension    {@linkplain GlobalCoord#MOON_RIGHT_ASCENSION the Moon's right ascension (α)}, in radians
     * @return                      {@linkplain GlobalCoord#MOON_HOUR_ANGLE the Moon's hour angle at the Greenwich meridian (H0)},
     *                              in radians: [-π/2°, +π/2°)
     */
    public double calculate(double siderealTimeDegrees, double moonRightAscension) {
        return Transformations.calculateHourAngle(Math.toRadians(siderealTimeDegrees), moonRightAscension);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.SIDEREAL_APPARENT_TIME, GlobalCoord.MOON_RIGHT_ASCENSION);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SIDEREAL_APPARENT_TIME),
            (Double) precalculatedValues.get(GlobalCoord.MOON_RIGHT_ASCENSION)
        );
    }
}
