package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#SUN_PARALLAX_SINE the sine of the Sun's equatorial horizontal parallax (sin(π))}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 40 (p. 279)"
 */
public final class SunParallaxSineCalculator implements Provider<GlobalCoord, TimelinePoint> {

    private static final double NUMERATOR = Math.sin(Math.toRadians(Calcs.Angle.arcsecondsToDegrees(8.794)));

    /**
     * Calculates {@linkplain GlobalCoord#SUN_PARALLAX_SINE the sine of the Sun's equatorial horizontal parallax (sin(π))}.
     * Quick.
     *
     * @param earthSunRadius    {@linkplain GlobalCoord#EARTH_SUN_RADIUS Earth-Sun radius (Δ)}, in astronomical units
     * @return                  {@linkplain GlobalCoord#SUN_PARALLAX_SINE the sine of the Sun's equatorial horizontal parallax (sin(π))}
     */
    public double calculate(double earthSunRadius) {
        return NUMERATOR / earthSunRadius;
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.SUN_PARALLAX_SINE;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.EARTH_SUN_RADIUS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate((Double) precalculatedValues.get(GlobalCoord.EARTH_SUN_RADIUS));
    }
}
