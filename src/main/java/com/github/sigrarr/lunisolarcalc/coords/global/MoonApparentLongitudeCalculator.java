package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_APPARENT_LONGITUDE apparent longitude of the center of the Moon (λ)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337...)"
 */
public final class MoonApparentLongitudeCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * Calculates the {@linkplain GlobalCoord#MOON_APPARENT_LONGITUDE apparent longitude of the center of the Moon (λ)}.
     * Quick.
     *
     * @param longitude             {@linkplain GlobalCoord#MOON_LONGITUDE the Moon's geocentric longitude (λ)}, in radians
     * @param nutuationInLongitude  {@linkplain GlobalCoord#EARTH_NUTUATION_IN_LONGITUDE the Earth's nutuation in longitude (Δψ)}, in radians
     * @return                      {@linkplain GlobalCoord#MOON_APPARENT_LONGITUDE apparent longitude of the center of the Moon (λ)},
     *                              in radians: [0, 2π)
     */
    public double calculate(double longitude, double nutuationInLongitude) {
        return Calcs.Angle.toNormalLongitude(longitude + nutuationInLongitude);
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.MOON_APPARENT_LONGITUDE;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.MOON_LONGITUDE, GlobalCoord.EARTH_NUTUATION_IN_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.MOON_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.EARTH_NUTUATION_IN_LONGITUDE)
        );
    }
}
