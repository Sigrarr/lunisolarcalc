package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

/**
 * Calculator of airless geocentric {@linkplain LocalCoord#MOON_AZIMUTH azimuth of the Moon}
 * or {@linkplain LocalCoord#SUN_AZIMUTH of the Sun} (A),
 * measured from the South.
 *
 * Given required parameters, it's in itself quick.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: 13 (pp. 91-93)"
 */
public class AzimuthCalculator implements Provider<Key, TimelinePoint> {

    final Body body;
    final GeoCoords geoCoords;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun)
     * and specified geographical coordinates of the observer.
     *
     * @param body          celestial body (the Moon or the Sun)
     * @param geoCoords     geographical coordinates of the observer
     */
    public AzimuthCalculator(Body body, GeoCoords geoCoords) {
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
    public AzimuthCalculator(Body body, GeoPosition geoPosition) {
        this(body, geoPosition.coords);
    }

    /**
     * Determines the celestial body's airless geocentric azimuth (from the South; A): [0, 2π).
     *
     * @param hourAngle0        celestial body's hour angle at the Greenwich meridian (H0), in radians
     * @param declination       declination (δ), in radians
     * @return                  azimuth (from the South; A), in radians: [0, 2π)
     */
    public double calculate(double hourAngle0, double declination) {
        double lha = Transformations.calculateLocalHourAngle(hourAngle0, geoCoords.getPlanetographicLongitude());
        return Transformations.calculateAzimuth(lha, declination, geoCoords.getLatitude());
    }

    @Override
    public Key provides() {
        return body.azimuthCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(body.hourAngleCoord.key(), body.declinationCoord.key());
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        double ha0 = (Double) precalculatedValues.get(body.hourAngleCoord.key());
        double declination = (Double) precalculatedValues.get(body.declinationCoord.key());
        return calculate(ha0, declination);
    }
}
