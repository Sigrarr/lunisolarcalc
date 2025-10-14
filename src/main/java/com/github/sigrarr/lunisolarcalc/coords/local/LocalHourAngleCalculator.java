package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of apparent geocentric {@linkplain LocalCoord#MOON_LOCAL_HOUR_ANGLE local hour angle of the Moon}
 * or {@linkplain LocalCoord#SUN_LOCAL_HOUR_ANGLE of the Sun} (H).
 * Given required parameters, it's in itself quick.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class LocalHourAngleCalculator implements Provider<Key, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;
    public final GeoCoords geoCoords;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun)
     * and specified geographical coordinates of the observer.
     *
     * @param body          celestial body (the Moon or the Sun)
     * @param geoCoords     geographical coordinates of the observer
     */
    public LocalHourAngleCalculator(Body body, GeoCoords geoCoords) {
        this.body = body;
        this.geoCoords = geoCoords;
    }

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun)
     * and specified observer's position on Earth (elevation is irrelevant though).
     *
     * @param body          celestial body (the Moon or the Sun)
     * @param geoPosition   the observer's position on Earth
     */
    public LocalHourAngleCalculator(Body body, GeoPosition geoPosition) {
        this(body, geoPosition.coords);
    }

    /**
     * Calculates the celestial body's apparent geocentric local hour angle (H).
     *
     * @param siderealTime0     sidereal time at the Greenwich meridian (θ0), in radians
     * @param rightAscension    right ascension (α), in radians
     * @return                  geocentric local hour angle (H), in radians: [-π, π)
     */
    public double calculate(double siderealTime0, double rightAscension) {
        return Transformations.calculateLocalHourAngle(siderealTime0, geoCoords.getPlanetographicLongitude(), rightAscension);
    }

    @Override
    public Key provides() {
        return body.localHourAngleCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(
            GlobalCoord.SIDEREAL_APPARENT_TIME_0.key(),
            body.rightAscensionCoord.key()
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SIDEREAL_APPARENT_TIME_0.key()),
            (Double) precalculatedValues.get(body.rightAscensionCoord.key())
        );
    }
}
