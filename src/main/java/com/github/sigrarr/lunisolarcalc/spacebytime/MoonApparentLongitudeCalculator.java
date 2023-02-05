package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of apparent longitude of the center of the Moon (λ + ΔΨ).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337...)"
 */
public final class MoonApparentLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_APPARENT_LONGITUDE;

    /**
     * Calculates apparent longitude of the center of the Moon (λ + ΔΨ): [0, 2π).
     * Quick.
     *
     * @param longitude                     {@linkplain MoonLongitudeCalculator the Moon's geocentric longitude} (λ), in radians
     * @param earthNutuationInLongitude     {@linkplain EarthNutuationInLongitudeCalculator the Earth's nutuation in longitude} (ΔΨ), in radians
     * @return                              apparent longitude of the center of the Moon (λ + ΔΨ): [0, 2π)
     */
    public double calculate(double longitude, double earthNutuationInLongitude) {
        return Calcs.Angle.normalizeLongitudinally(longitude + earthNutuationInLongitude);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_LONGITUDE, Subject.EARTH_NUTUATION_IN_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.MOON_LONGITUDE),
            (Double) precalculatedValues.get(Subject.EARTH_NUTUATION_IN_LONGITUDE)
        );
    }
}
