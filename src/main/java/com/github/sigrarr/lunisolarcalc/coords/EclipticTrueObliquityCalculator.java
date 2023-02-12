package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (pp. 147-148)"
 */
public final class EclipticTrueObliquityCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.ECLIPTIC_TRUE_OBLIQUITY;

    /**
     * Calculates the {@linkplain Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians.
     * Quick operation.
     *
     * @param eclipticMeanObliquity     {@linkplain Subject#ECLIPTIC_MEAN_OBLIQUITY mean obliquity of the ecliptic (ε0)}, in radians
     * @param nutuationInObliquity      {@linkplain Subject#EARTH_NUTUATION_IN_OBLIQUITY nutuation in obliquity (Δε)}, in radians
     * @return                          {@linkplain Subject#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     */
    public double calculate(double eclipticMeanObliquity, double nutuationInObliquity) {
        return eclipticMeanObliquity + nutuationInObliquity;
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
