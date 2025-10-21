package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of airless {@linkplain LocalCoord#MOON_TOPOCENTRIC_ALTITUDE topocentric altitude of the Moon}
 * or {@linkplain LocalCoord#SUN_TOPOCENTRIC_ALTITUDE of the Sun} (h′)
 * (i.e. the angular distance of the celestial body from the horizon).
 *
 * Given required parameters, it's in itself quick.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class TopocentricAltitudeCalculator implements Provider<Key, TimelinePoint> {
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
    public TopocentricAltitudeCalculator(Body body, GeoCoords geoCoords) {
        this.body = body;
        this.geoCoords = geoCoords;
    }

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun)
     * and specified observer's position on Earth (elevation is not needed though).
     *
     * @param body          celestial body (the Moon or the Sun)
     * @param geoPosition   the observer's position on Earth
     */
    public TopocentricAltitudeCalculator(Body body, GeoPosition geoPosition) {
        this(body, geoPosition.coords);
    }

    /**
     * Determines the celestial body's airless topocentric altitude (h′)
     * (positive: over the horizon; negative: under).
     *
     * @param topocentricDeclination       topocentric declination (δ′), in radians
     * @param topocentricLocalHourAngle    topocentric local hour angle (H′), in radians
     * @return                             topocentric altitude (h′), in radians: [-π/2, π/2]
     */
    public double calculate(double topocentricDeclination, double topocentricLocalHourAngle) {
        return Transformations.calculateAltitude(topocentricDeclination, topocentricLocalHourAngle, geoCoords.getLatitude());
    }

    @Override
    public Key provides() {
        return body.topocentricAltitudeCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(body.topocentricDeclinationCoord.key(), body.topocentricLocalHourAngleCoord.key());
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.topocentricDeclinationCoord.key()),
            (Double) precalculatedValues.get(body.topocentricLocalHourAngleCoord.key())
        );
    }
}
