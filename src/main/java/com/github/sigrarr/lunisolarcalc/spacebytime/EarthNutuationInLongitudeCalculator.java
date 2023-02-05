package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthNutuationInLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Earth's nutuation in longitude (ΔΨ).
 * Costly; processes its own {@linkplain EarthNutuationInLongitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (pp. 143-144)"
 */
public final class EarthNutuationInLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.EARTH_NUTUATION_IN_LONGITUDE;

    private EarthNutuationInLongitudePeriodicTerms periodicTerms = new EarthNutuationInLongitudePeriodicTerms();

    /**
     * Calculates the Earth's nutuation in longitude (ΔΨ), in radians.
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in {@linkplain EarthNutuationInLongitudePeriodicTerms periodic terms}
     * @return          the Earth's nutuation in longitude (ΔΨ), in radians
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
