package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.periodicterms.MoonDistancePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain Subject#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}.
 * Costly; processes its own {@linkplain MoonDistancePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337...)"
 */
public final class MoonEarthDistanceCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_EARTH_DISTANCE;
    protected static final double BASE_VALUE_KILOMETERS = 385000.56;

    private MoonDistancePeriodicTerms periodicTerms = new MoonDistancePeriodicTerms();

    /**
     * Calculates the {@linkplain Subject#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}, in km.
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in {@linkplain MoonDistancePeriodicTerms periodic terms}
     * @return          {@linkplain Subject#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}, in km
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
