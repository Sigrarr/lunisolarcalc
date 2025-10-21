package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain LocalCoord#MOON_TOPOCENTRIC_AZIMUTH topocentric azimuth of the Moon}
 * or {@linkplain LocalCoord#SUN_TOPOCENTRIC_AZIMUTH of the Sun} (A′), measured from the South.
 *
 * Given required parameters, it's in itself quick.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class TopocentricAzimuthCalculator implements Provider<Key, TimelinePoint> {
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
    public TopocentricAzimuthCalculator(Body body, GeoCoords geoCoords) {
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
    public TopocentricAzimuthCalculator(Body body, GeoPosition geoPosition) {
        this(body, geoPosition.coords);
    }

    /**
     * Determines the celestial body's topocentric azimuth (from the South; A′).
     *
     * @param topocentricLocalHourAngle    topocentric local hour angle (H′), in radians
     * @param topocentricDeclination       topocentric declination (δ′), in radians
     * @return                             topocentric azimuth (from the South; A), in radians: [0, 2π)
     */
    public double calculate(double topocentricLocalHourAngle, double topocentricDeclination) {
        return Transformations.calculateAzimuth(topocentricLocalHourAngle, topocentricDeclination, geoCoords.getLatitude());
    }

    @Override
    public Key provides() {
        return body.topocentricAzimuthCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(body.topocentricLocalHourAngleCoord.key(), body.topocentricDeclinationCoord.key());
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.topocentricLocalHourAngleCoord.key()),
            (Double) precalculatedValues.get(body.topocentricDeclinationCoord.key())
        );
    }
}
