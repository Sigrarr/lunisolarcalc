package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of latitude of the Moon's center (β).
 * Costly; processes its own {@link MoonLatitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@link CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcComposition}.
 *
 * @see " Meeus 1998: Ch. 47 (p. 337...)
 */
public final class MoonLatitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_LATITUDE;

    private MoonLatitudePeriodicTerms periodicTerms = new MoonLatitudePeriodicTerms();

    /**
     * Calcules latitude of the Moon's center (β): [-π/2, π/2].
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in {@link MoonLatitudePeriodicTerms periodic terms}
     * @return          latitude of the Moon's center (β): [-π/2, π/2]
     */
    public double calculate(TimelinePoint tx, MoonCoordinateElements elements) {
        return Calcs.Angle.normalizeLatitudinally(periodicTerms.evaluate(tx.toDynamicalTime(), elements));
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
