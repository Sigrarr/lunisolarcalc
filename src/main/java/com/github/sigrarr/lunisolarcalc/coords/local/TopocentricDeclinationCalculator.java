package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain LocalCoord#MOON_TOPOCENTRIC_DECLINATION topocentric declination of the Moon}
 * or {@linkplain LocalCoord#SUN_TOPOCENTRIC_DECLINATION of the Sun} (δ′).
 * Given required parameters, it's not very costly.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Topo
 */
public class TopocentricDeclinationCalculator implements Provider<Key, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public TopocentricDeclinationCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's topocentric declination (δ′).
     *
     * @param declination                       geocentric declination (δ), in radians
     * @param parallaxInRightAscension          parallax in right ascension (Δα), in radians
     * @param localHourAngle                    local hour angle (H), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param rhoSinPhiPrime                    {@linkplain LocalCoord#RHO_SIN_PHI_PRIME ρ*sin(φ′)}
     * @param rhoCosPhiPrime                    {@linkplain LocalCoord#RHO_COS_PHI_PRIME ρ*cos(φ′)}
     * @return                                  topocentric declination (δ′), in radians: [-π/2, π/2]
     */
    public double calculate(
        double declination,
        double parallaxInRightAscension,
        double localHourAngle,
        double equatorialHorizontalParallaxSine,
        double rhoSinPhiPrime,
        double rhoCosPhiPrime
    ) {
        return Topo.calculateTopocentricDeclination(
            declination,
            parallaxInRightAscension,
            localHourAngle,
            equatorialHorizontalParallaxSine,
            rhoSinPhiPrime,
            rhoCosPhiPrime
        );
    }

    @Override
    public Key provides() {
        return body.topocentricDeclinationCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(
            body.declinationCoord.key(),
            body.parallaxInRrightAscensionCoord.key(),
            body.localHourAngleCoord.key(),
            body.parallaxSineCoord.key(),
            LocalCoord.RHO_SIN_PHI_PRIME.key(),
            LocalCoord.RHO_COS_PHI_PRIME.key()
        );
    }

    @Override
    public Object calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.declinationCoord.key()),
            (Double) precalculatedValues.get(body.parallaxInRrightAscensionCoord.key()),
            (Double) precalculatedValues.get(body.localHourAngleCoord.key()),
            (Double) precalculatedValues.get(body.parallaxSineCoord.key()),
            (Double) precalculatedValues.get(LocalCoord.RHO_SIN_PHI_PRIME.key()),
            (Double) precalculatedValues.get(LocalCoord.RHO_COS_PHI_PRIME.key())
        );
    }
}
