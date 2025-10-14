package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_DECLINATION declination of the Moon}
 * or {@linkplain GlobalCoord#SUN_DECLINATION of the Sun} (δ).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class DeclinationCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public DeclinationCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's declination (δ).
     * Quick.
     *
     * @param latitude              latitude (β), in radians
     * @param apparentLongitude     apparent longitude (λ), in radians
     * @param eclipticObliquity     {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     * @return                      declination (δ), in radians: [-π/2, π/2]
     */
    public double calculate(double latitude, double apparentLongitude, double eclipticObliquity) {
        return Transformations.eclipticalToDeclination(latitude, apparentLongitude, eclipticObliquity);
    }

    @Override
    public GlobalCoord provides() {
        return body.declinationCoord;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(body.latitudeCoord, body.apparentLongitudeCoord, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.latitudeCoord),
            (Double) precalculatedValues.get(body.apparentLongitudeCoord),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
