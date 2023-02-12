package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain Subject#MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS excess}
 * of the Moon's apparent longitude over the Sun's apparent longitude.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Seidelmann 1992: Ch. 9 by B.D. Yallop & C.Y. Hohenkerk, 9.213 (p. 478)"
 * @see "Meeus 1998: Ch. 49 (p. 349)"
 */
public class MoonOverSunApparentLongitudeExcessCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS;

    /**
     * Calculates the {@linkplain Subject#MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS excess}
     * of {@linkplain Subject#MOON_APPARENT_LONGITUDE the Moon's apparent longitude} over
     * {@linkplain Subject#SUN_APPARENT_LONGITUDE the Sun's apparent longitude}: [0, 2π).
     *
     * By definition, it would take these quantities to subtract one from another
     * - and you can pass them as arguments to this method, instead of those listed below -
     * but they both include {@linkplain Subject#EARTH_NUTUATION_IN_LONGITUDE the Earth's nutuation (Δψ)},
     * so the latter wouldn't affect the result anyway, while calculating it could turn out to be redundant.
     *
     * Quick operation.
     *
     * @param moonLongitude             {@linkplain Subject#MOON_LONGITUDE the Moon's longitude}, in radians
     * @param sunAberratedLongitude     {@linkplain Subject#SUN_ABERRATED_LONGITUDE the Sun's aberrated longitude}, in radians
     * @return                          {@linkplain Subject#MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS excess}
     *                                  of the Moon's apparent longitude over the Sun's apparent longitude, in radians: [0, 2π)
     */
    public double calculate(double moonLongitude, double sunAberratedLongitude) {
        return Calcs.Angle.normalizeLongitudinally(moonLongitude - sunAberratedLongitude);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_LONGITUDE, Subject.SUN_ABERRATED_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.MOON_LONGITUDE),
            (Double) precalculatedValues.get(Subject.SUN_ABERRATED_LONGITUDE)
        );
    }
}
