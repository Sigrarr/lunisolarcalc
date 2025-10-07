package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.EnumSet;
import java.util.Map;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#MOON_EQUATORIAL_HORIZONTAL_PARALLAX the Moon's equatorial horizontal parallax (π)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337)"
 */
public final class MoonEquatorialHorizontalParallaxCalculator implements Provider<GlobalCoord, TimelinePoint> {

    private static final double NUMERATOR_KMRAD = 6378.14;

    /**
     * Calculates {@linkplain GlobalCoord#MOON_EQUATORIAL_HORIZONTAL_PARALLAX the Moon's equatorial horizontal parallax (π)}.
     * Quick.
     *
     * @param moonEarthDistance     {@linkplain GlobalCoord#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}, in kilometers
     * @return                      {@linkplain GlobalCoord#MOON_EQUATORIAL_HORIZONTAL_PARALLAX the Moon's equatorial horizontal parallax (π)},
     *                              in radians
     */
    public double calculate(double moonEarthDistance) {
        return Math.asin(NUMERATOR_KMRAD / moonEarthDistance);
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.MOON_EQUATORIAL_HORIZONTAL_PARALLAX;
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
