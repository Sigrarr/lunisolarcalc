package com.github.sigrarr.lunisolarcalc.coords;

import java.util.EnumSet;
import java.util.Map;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain Subject#MOON_EQUATORIAL_HORIZONTA_PARALLAX the Moon's equatorial horizontal parallax (π)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337)"
 */
public final class MoonEquatorialHorizontalParallaxCalculator implements Provider<Subject, TimelinePoint> {

    private static final double NUMERATOR_KMRAD = 6378.14;

    /**
     * Calculates {@linkplain Subject#MOON_EQUATORIAL_HORIZONTA_PARALLAX the Moon's equatorial horizontal parallax (π)}.
     * Quick.
     *
     * @param moonEarthDistance     {@linkplain Subject#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}, in kilometers
     * @return                      {@linkplain Subject#MOON_EQUATORIAL_HORIZONTA_PARALLAX the Moon's equatorial horizontal parallax (π)},
     *                              in radians
     */
    public double calculate(double moonEarthDistance) {
        return Math.asin(NUMERATOR_KMRAD / moonEarthDistance);
    }

    @Override
    public Subject provides() {
        return Subject.MOON_EQUATORIAL_HORIZONTA_PARALLAX;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_EARTH_DISTANCE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate((Double) precalculatedValues.get(Subject.MOON_EARTH_DISTANCE));
    }
}
