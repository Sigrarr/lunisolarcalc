package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_PARALLAX equatorial horizontal parallax of the Moon}
 * or {@linkplain GlobalCoord#SUN_PARALLAX of the Sun} (π).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered
 * for both celestial bodies in {@link CoordsCalcCompositions}.
 *
 * It just applies {@link Math#asin(double) Math.asin()} to sin(π).
 *
 * @see "Meeus 1998: Ch. 40 (p. 279)"
 */
public class ParallaxCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * The celestial body whose coordinate this calculator provides.
     */
    public final Body body;

    /**
     * Constructs an instance for given celestial body (the Moon or the Sun).
     *
     * @param body  celestial body (the Moon or the Sun)
     */
    public ParallaxCalculator(Body body) {
        this.body = body;
    }

    /**
     * Calculates the celestial body's equatorial horizontal parallax (π).
     * Quick. Equivalent of applying {@link Math#asin(double) Math.asin()} to sin(π).
     *
     * @param parallaxSine  the sine of the equatorial horizontal parallax (sin(π))
     * @return              equatorial horizontal parallax (π), in radians.
     */
    public double calculate(double parallaxSine) {
        return Math.asin(parallaxSine);
    }

    @Override
    public GlobalCoord provides() {
        return body.parallaxCoord;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(body.parallaxSineCoord);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate((Double) precalculatedValues.get(body.parallaxSineCoord));
    }
}
