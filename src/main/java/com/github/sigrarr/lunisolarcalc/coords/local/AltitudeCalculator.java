package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.Body;
import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

/**
 * Calculator of airless geocentric {@linkplain LocalCoord#MOON_ALTITUDE altitude of the Moon}
 * or {@linkplain LocalCoord#SUN_ALTITUDE of the Sun} (h)
 * (i.e. the angular distance of the celestial body from the horizon).
 *
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: 13 (pp. 91-93)"
 */
public class AltitudeCalculator implements Provider<Key, TimelinePoint> {

    final Body body;
    final GeoCoords geoCoords;

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
     * Determines the celestial body's airless geocentric altitude (h): [-π/2, π/2]
     * (positive: over the horizon; negative: under).
     *
     * @param declination       celestial body's declination (δ), in radians
     * @param hourAngle0        celestial body's hour angle at the Greenwich meridian (H0), in radians
     * @return                  altitude (h), in radians: [-π/2, π/2]
     */
    public double calculate(double declination, double hourAngle0) {
        double lha = Transformations.calculateLocalHourAngle(hourAngle0, geoCoords.getPlanetographicLongitude());
        return Transformations.calculateAltitude(declination, lha, geoCoords.getLatitude());
    }

    @Override
    public Key provides() {
        return body.altitudeCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(body.declinationCoord.key(), body.hourAngleCoord.key());
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        double declination = (Double) precalculatedValues.get(body.declinationCoord.key());
        double ha0 = (Double) precalculatedValues.get(body.hourAngleCoord.key());
        return calculate(declination, ha0);
    }
}
