package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of longitude of the Moon's center (λ).
 * Costly; processes its own {@link MoonLongitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@link CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcComposition}.
 *
 * @see " Meeus 1998: Ch. 47 (p. 337...)
 */
public final class MoonLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_LONGITUDE;

    private MoonLongitudePeriodicTerms periodicTerms = new MoonLongitudePeriodicTerms();

    /**
     * Calcules longitude of the Moon's center (λ): [0, 2π).
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in {@link MoonLongitudePeriodicTerms periodic terms}
     * @return          longitude of the Moon's center (λ): [0, 2π)
     */
    public double calculate(TimelinePoint tx, MoonCoordinateElements elements) {
        return Calcs.Angle.normalizeLongitudinally(elements.getLPrim() + periodicTerms.evaluate(tx.toDynamicalTime(), elements));
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_COORDINATE_ELEMENTS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(tx, (MoonCoordinateElements) precalculatedValues.get(Subject.MOON_COORDINATE_ELEMENTS));
    }
}
