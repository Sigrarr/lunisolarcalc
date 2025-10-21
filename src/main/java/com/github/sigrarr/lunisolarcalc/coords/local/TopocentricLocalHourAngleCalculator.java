package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Sets;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain LocalCoord#MOON_TOPOCENTRIC_LOCAL_HOUR_ANGLE topocentric local hour angle of the Moon}
 * or {@linkplain LocalCoord#SUN_TOPOCENTRIC_LOCAL_HOUR_ANGLE of the Sun} (H′).
 * Given required parameters, it's not very costly.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Topo
 */
public class TopocentricLocalHourAngleCalculator implements Provider<Key, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public TopocentricLocalHourAngleCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's topocentric local hour angle (H′).
     *
     * @param localHourAngle                    geocentric local hour angle (H), in radians
     * @param declination                       declination (δ), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param rhoCosPhiPrime                    {@linkplain LocalCoord#RHO_COS_PHI_PRIME ρ*cos(φ′)}
     * @return                                  topocentric local hour angle (H′), in radians: [-π, π)
     */
    public double calculate(double localHourAngle, double declination, double equatorialHorizontalParallaxSine, double rhoCosPhiPrime) {
        return Topo.calculateTopocentricLocalHourAngle(localHourAngle, declination, equatorialHorizontalParallaxSine, rhoCosPhiPrime);
    }

    @Override
    public Key provides() {
        return body.topocentricLocalHourAngleCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(
            body.localHourAngleCoord.key(),
            body.declinationCoord.key(),
            body.parallaxSineCoord.key(),
            LocalCoord.RHO_COS_PHI_PRIME.key()
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.localHourAngleCoord.key()),
            (Double) precalculatedValues.get(body.declinationCoord.key()),
            (Double) precalculatedValues.get(body.parallaxSineCoord.key()),
            (Double) precalculatedValues.get(LocalCoord.RHO_COS_PHI_PRIME.key())
        );
    }
}
