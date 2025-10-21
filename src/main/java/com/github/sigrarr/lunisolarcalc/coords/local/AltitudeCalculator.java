package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of airless geocentric {@linkplain LocalCoord#MOON_ALTITUDE altitude of the Moon}
 * or {@linkplain LocalCoord#SUN_ALTITUDE of the Sun} (h)
 * (i.e. the angular distance of the celestial body from the horizon).
 *
 * Given required parameters, it's in itself quick.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class AltitudeCalculator implements Provider<Key, TimelinePoint> {
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
    public AltitudeCalculator(Body body, GeoCoords geoCoords) {
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
    public AltitudeCalculator(Body body, GeoPosition geoPosition) {
        this(body, geoPosition.coords);
    }

    /**
     * Determines the celestial body's airless geocentric altitude (h)
     * (positive: over the horizon; negative: under).
     *
     * @param declination       declination (δ), in radians
     * @param localHourAngle    local hour angle (H), in radians
     * @return                  altitude (h), in radians: [-π/2, π/2]
     */
    public double calculate(double declination, double localHourAngle) {
        return Transformations.calculateAltitude(declination, localHourAngle, geoCoords.getLatitude());
    }

    @Override
    public Key provides() {
        return body.altitudeCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(body.declinationCoord.key(), body.localHourAngleCoord.key());
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.declinationCoord.key()),
            (Double) precalculatedValues.get(body.localHourAngleCoord.key())
        );
    }
}
