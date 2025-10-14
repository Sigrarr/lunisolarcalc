package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_HOUR_ANGLE_0 hour angle of the Moon}
 * or {@linkplain GlobalCoord#SUN_HOUR_ANGLE_0 of the Sun} at the Greenwich meridian (H0).
 * Given required parameters, it's in itself quick.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class HourAngleCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public HourAngleCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's hour angle at the Greenwich meridian (H0).
     * If you pass the local sidereal time (θ) instead of Greenwich (θ0),
     * the local hour angle will be returned (H).
     * Quick.
     *
     * @param siderealTime0Degrees  {@linkplain GlobalCoord#SIDEREAL_APPARENT_TIME_0 sidereal time at the Greenwich meridian (θ0)}, in degrees
     * @param rightAscension        right ascension (α), in radians
     * @return                      hour angle at the Greenwich meridian (H0),
     *                              in radians: [-π, +π)
     */
    public double calculate(double siderealTime0Degrees, double rightAscension) {
        return Transformations.calculateHourAngle(Math.toRadians(siderealTime0Degrees), rightAscension);
    }

    @Override
    public GlobalCoord provides() {
        return body.hourAngle0Coord;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.SIDEREAL_APPARENT_TIME_0, body.rightAscensionCoord);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SIDEREAL_APPARENT_TIME_0),
            (Double) precalculatedValues.get(body.rightAscensionCoord)
        );
    }
}
