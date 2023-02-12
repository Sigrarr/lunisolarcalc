package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Sun's declination (δ).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CalcCompositions}.
 *
 * @see Transformations
 */
public final class SunDeclinationCalculator extends Transformations implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_DECLINATION;

    /**
     * Calculates the Sun's declination (δ): [-π/2, π/2].
     * Quick.
     *
     * @param sunLatitude           {@linkplain Subject#SUN_LATITUDE the Sun's latitude (β)}, in radians
     * @param sunApparentLongitude  {@linkplain Subject#SUN_APPARENT_LONGITUDE the Sun's apparent longitude (λ)},
     *                              in radians
     * @param eclipticObliquity     {@link Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)},
     *                              in radians
     * @return                      the Sun's declination (δ), in radians: [-π/2, π/2]
     */
    public double calculate(double sunLatitude, double sunApparentLongitude, double eclipticObliquity) {
        return Transformations.eclipticalToDeclination(sunLatitude, sunApparentLongitude, eclipticObliquity);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SUN_LATITUDE, Subject.SUN_APPARENT_LONGITUDE, Subject.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.SUN_LATITUDE),
            (Double) precalculatedValues.get(Subject.SUN_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(Subject.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
