package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Sun's right ascension (α).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see Transformations
 */
public final class SunRightAscensionCalculator extends Transformations implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_RIGHT_ASCENSION;

    /**
     * Calculates the Sun's right ascension (α): [0, 2π).
     * Quick.
     *
     * @param sunApparentLongitude  {@linkplain Subject#SUN_APPARENT_LONGITUDE the Sun's apparent longitude (λ)},
     *                              in radians
     * @param sunLatitude           {@linkplain Subject#SUN_LATITUDE the Sun's latitude (β)}, in radians
     * @param eclipticObliquity     {@link Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)},
     *                              in radians
     * @return                      the Sun's right ascension (α), in radians: [0, 2π)
     */
    public double calculate(double sunApparentLongitude, double sunLatitude, double eclipticObliquity) {
        return Transformations.eclipticalToRightAscension(sunApparentLongitude, sunLatitude, eclipticObliquity);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SUN_APPARENT_LONGITUDE, Subject.SUN_LATITUDE, Subject.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.SUN_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(Subject.SUN_LATITUDE),
            (Double) precalculatedValues.get(Subject.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
