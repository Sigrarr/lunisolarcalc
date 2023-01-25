package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonDistancePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of distance between the centers of Earth and Moon (Δ).
 * Costly; processes its own {@link MoonDistancePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@link CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcComposition}.
 *
 * @see " Meeus 1998: Ch. 47 (p. 337...)
 */
public final class MoonEarthDistanceCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_EARTH_DISTANCE;
    protected static final double BASE_VALUE_KILOMETERS = 385000.56;

    private MoonDistancePeriodicTerms periodicTerms = new MoonDistancePeriodicTerms();

    /**
     * Calculates distance between the centers of Earth and Moon (Δ), in km.
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in {@link MoonDistancePeriodicTerms periodic terms}
     * @return          distance between the centers of Earth and Moon (Δ), in km
     */
    public double calculate(TimelinePoint tx, MoonCoordinateElements elements) {
        return BASE_VALUE_KILOMETERS + periodicTerms.evaluate(tx.toDynamicalTime(), elements);
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
