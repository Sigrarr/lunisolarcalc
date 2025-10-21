package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain LocalCoord#MOON_TOPOCENTRIC_RIGHT_ASCENSION topocentric right ascension of the Moon}
 * or {@linkplain LocalCoord#SUN_TOPOCENTRIC_RIGHT_ASCENSION of the Sun} (α′).
 * Given required parameters, it's not very costly.
 * {@linkplain CalculationComposer Composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * @see Topo
 */
public class TopocentricRightAscensionCalculator implements Provider<Key, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public TopocentricRightAscensionCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's topocentric right ascension (α′).
     *
     * @param rightAscension            geocentric right ascension (α), in radians
     * @param parallaxInRightAscension  parallax in right ascension (Δα), in radians
     * @return                          topocentric right ascension (α′), in radians: [0, 2π)
     */
    public double calculate(double rightAscension, double parallaxInRightAscension) {
        return Topo.calculateTopocentricRightAscension(rightAscension, parallaxInRightAscension);
    }

    @Override
    public Key provides() {
        return body.topocentricRightAscensionCoord.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.of(
            body.rightAscensionCoord.key(),
            body.parallaxInRightAscensionCoord.key()
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(body.rightAscensionCoord.key()),
            (Double) precalculatedValues.get(body.parallaxInRightAscensionCoord.key())
        );
    }
}
