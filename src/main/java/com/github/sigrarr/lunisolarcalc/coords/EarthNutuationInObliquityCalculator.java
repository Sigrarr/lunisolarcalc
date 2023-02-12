package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.periodicterms.EarthNutuationInObliquityPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Earth's {@linkplain Subject#EARTH_NUTUATION_IN_OBLIQUITY nutuation in obliquity (Δε)}.
 * Costly; processes its own {@linkplain EarthNutuationInObliquityPeriodicTerms periodic terms} table.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (pp. 143-144)"
 */
public final class EarthNutuationInObliquityCalculator implements Provider<Subject, TimelinePoint> {

    private static final Subject SUBJECT = Subject.EARTH_NUTUATION_IN_OBLIQUITY;

    private EarthNutuationInObliquityPeriodicTerms periodicTerms = new EarthNutuationInObliquityPeriodicTerms();

    /**
     * Calculates the Earth's {@linkplain Subject#EARTH_NUTUATION_IN_OBLIQUITY nutuation in obliquity (Δε)}, in radians.
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in {@linkplain EarthNutuationInObliquityPeriodicTerms periodic terms}
     * @return          the Earth's {@linkplain Subject#EARTH_NUTUATION_IN_OBLIQUITY nutuation in obliquity (Δε)}, in radians
     */
    public double calculate(TimelinePoint tx, EarthNutuationElements elements) {
        return periodicTerms.evaluate(tx.toDynamicalTime(), elements);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_NUTUATION_ELEMENTS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(tx, (EarthNutuationElements) precalculatedValues.get(Subject.EARTH_NUTUATION_ELEMENTS));
    }
}
