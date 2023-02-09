package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the true obliquity of the ecliptic (the angle between the ecliptc
 * and the celestial equator; ε = ε0 + Δε).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (pp. 147-148)"
 */
public final class EclipticTrueObliquityCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.ECLIPTIC_TRUE_OBLIQUITY;

    /**
     * Calculates the true obliquity of the ecliptic (ε = ε0 + Δε), in radians.
     * Quick operation.
     *
     * @param eclipticMeanObliquity         {@linkplain EclipticMeanObliquityCalculator mean obliquity of the ecliptic (ε0)}, in radians
     * @param earthNutuationInObliquity     {@linkplain EarthNutuationInObliquityCalculator nutuation in obliquity (Δε)}, in radians
     * @return                              the true obliquity of the ecliptic (ε = ε0 + Δε), in radians
     */
    public double calculate(double eclipticMeanObliquity, double earthNutuationInObliquity) {
        return eclipticMeanObliquity + earthNutuationInObliquity;
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.ECLIPTIC_MEAN_OBLIQUITY, Subject.EARTH_NUTUATION_IN_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.ECLIPTIC_MEAN_OBLIQUITY),
            (Double) precalculatedValues.get(Subject.EARTH_NUTUATION_IN_OBLIQUITY)
        );
    }
}
