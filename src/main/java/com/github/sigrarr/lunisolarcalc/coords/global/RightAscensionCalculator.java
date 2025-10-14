package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_RIGHT_ASCENSION right ascension of the Moon}
 * or {@linkplain GlobalCoord#SUN_RIGHT_ASCENSION of the Sun} (α).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public class RightAscensionCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public RightAscensionCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's right ascension (α).
     * Quick.
     *
     * @param apparentLongitude     apparent longitude (λ), in radians
     * @param latitude              latitude (β), in radians
     * @param eclipticObliquity     true obliquity of the ecliptic (ε), in radians
     * @return                      right ascension (α), in radians: [0, 2π)
     */
    public double calculate(double apparentLongitude, double latitude, double eclipticObliquity) {
        return Transformations.eclipticalToRightAscension(apparentLongitude, latitude, eclipticObliquity);
    }

    @Override
    public GlobalCoord provides() {
        return body.rightAscensionCoord;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(body.apparentLongitudeCoord, body.latitudeCoord, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.apparentLongitudeCoord),
            (Double) precalculatedValues.get(body.latitudeCoord),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
