package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain LocalCoord#MOON_PARALLAX_IN_RIGHT_ASCENSION parallax in right ascension of the Moon}
 * or {@linkplain LocalCoord#SUN_PARALLAX_IN_RIGHT_ASCENSION of the Sun} (Δα).
 * I.e. the difference between the celestial body's topocentric and geocentric right ascension (α′ − α)
 * Given required parameters, it's not very costly.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Topo
 */
public class ParallaxInRightAscensionCalculator implements Provider<Key, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public ParallaxInRightAscensionCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's parallax in right ascension (Δα),
     * i.e. the difference between its topocentric and geocentric right ascension (α′ − α).
     *
     * @param declination                       declination (δ), in radians
     * @param localHourAngle                    local hour angle (H), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param rhoCosPhiPrime                    {@linkplain LocalCoord#RHO_COS_PHI_PRIME ρ*cos(φ′)}
     * @return                                  the parallax in right ascension (Δα = α′ − α), in radians
     */
    public double calculate(double declination, double localHourAngle, double equatorialHorizontalParallaxSine, double rhoCosPhiPrime) {
        return Topo.calculateParallaxInRightAscension(declination, localHourAngle, equatorialHorizontalParallaxSine, rhoCosPhiPrime);
    }

    @Override
    public Key provides() {
        return body.parallaxInRightAscensionCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(
            body.declinationCoord.key(),
            body.localHourAngleCoord.key(),
            body.parallaxSineCoord.key(),
            LocalCoord.RHO_COS_PHI_PRIME.key()
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.declinationCoord.key()),
            (Double) precalculatedValues.get(body.localHourAngleCoord.key()),
            (Double) precalculatedValues.get(body.parallaxSineCoord.key()),
            (Double) precalculatedValues.get(LocalCoord.RHO_COS_PHI_PRIME.key())
        );
    }
}
