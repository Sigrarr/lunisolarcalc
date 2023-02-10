package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Moon's declination (δ).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see Transformations
 */
public final class MoonDeclinationCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_DECLINATION;

    /**
     * Calculates the Moon's declination (δ): [-π/2, π/2].
     * Quick.
     *
     * @param moonLatitude              {@linkplain Subject#MOON_LATITUDE the Moon's latitude (β)}, in radians
     * @param moonApparentLongitude     {@linkplain Subject#MOON_APPARENT_LONGITUDE the Moon's apparent longitude (λ)},
     *                                  in radians
     * @param eclipticObliquity         {@link Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)},
     *                                  in radians
     * @return                          the Moon's declination (δ), in radians: [-π/2, π/2]
     */
    public double calculate(double moonLatitude, double moonApparentLongitude, double eclipticObliquity) {
        return Transformations.eclipticalToDeclination(moonLatitude, moonApparentLongitude, eclipticObliquity);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_LATITUDE, Subject.MOON_APPARENT_LONGITUDE, Subject.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.MOON_LATITUDE),
            (Double) precalculatedValues.get(Subject.MOON_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(Subject.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
