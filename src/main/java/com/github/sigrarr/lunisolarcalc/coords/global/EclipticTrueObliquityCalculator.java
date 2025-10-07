package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (pp. 147-148)"
 */
public final class EclipticTrueObliquityCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY;

    /**
     * Calculates the {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians.
     * Quick operation.
     *
     * @param eclipticMeanObliquity     {@linkplain GlobalCoord#ECLIPTIC_MEAN_OBLIQUITY mean obliquity of the ecliptic (ε0)}, in radians
     * @param nutuationInObliquity      {@linkplain GlobalCoord#EARTH_NUTUATION_IN_OBLIQUITY nutuation in obliquity (Δε)}, in radians
     * @return                          {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     */
    public double calculate(double eclipticMeanObliquity, double nutuationInObliquity) {
        return eclipticMeanObliquity + nutuationInObliquity;
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.ECLIPTIC_MEAN_OBLIQUITY, GlobalCoord.EARTH_NUTUATION_IN_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_MEAN_OBLIQUITY),
            (Double) precalculatedValues.get(GlobalCoord.EARTH_NUTUATION_IN_OBLIQUITY)
        );
    }
}
