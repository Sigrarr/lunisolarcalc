package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#MOON_PARALLAX_SINE the sine the Moon's equatorial horizontal parallax (sin(π))}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337)"
 */
public final class MoonParallaxSineCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * Calculates {@linkplain GlobalCoord#MOON_PARALLAX_SINE the sine of the Moon's equatorial horizontal parallax (sin(π))}.
     * Quick.
     *
     * @param moonEarthDistance     {@linkplain GlobalCoord#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}, in kilometers
     * @return                      {@linkplain GlobalCoord#MOON_PARALLAX_SINE the sine of the Moon's equatorial horizontal parallax (sin(π))}
     */
    public double calculate(double moonEarthDistance) {
        return Topo.EQUATORIAL_RADIUS_KM / moonEarthDistance;
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.MOON_PARALLAX_SINE;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.MOON_EARTH_DISTANCE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate((Double) precalculatedValues.get(GlobalCoord.MOON_EARTH_DISTANCE));
    }
}
