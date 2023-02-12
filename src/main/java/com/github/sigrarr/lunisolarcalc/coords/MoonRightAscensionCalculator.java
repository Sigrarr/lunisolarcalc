package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Moon's right ascension (α).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CalcCompositions}.
 *
 * @see Transformations
 */
public final class MoonRightAscensionCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_RIGHT_ASCENSION;

    /**
     * Calculates the Moon's right ascension (α): [0, 2π).
     * Quick.
     *
     * @param moonApparentLongitude     {@linkplain Subject#MOON_APPARENT_LONGITUDE the Moon's apparent longitude (λ)},
     *                                  in radians
     * @param moonLatitude              {@linkplain Subject#MOON_LATITUDE the Moon's latitude (β)}, in radians
     * @param eclipticObliquity         {@link Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)},
     *                                  in radians
     * @return                          the Moon's right ascension (α), in radians: [0, 2π)
     */
    public double calculate(double moonApparentLongitude, double moonLatitude, double eclipticObliquity) {
        return Transformations.eclipticalToRightAscension(moonApparentLongitude, moonLatitude, eclipticObliquity);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_APPARENT_LONGITUDE, Subject.MOON_LATITUDE, Subject.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.MOON_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(Subject.MOON_LATITUDE),
            (Double) precalculatedValues.get(Subject.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
