package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.periodicterms.MoonDistancePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}.
 * Costly; processes its own {@linkplain MoonDistancePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337...)"
 */
public final class MoonEarthDistanceCalculator implements Provider<GlobalCoord, TimelinePoint> {

    protected static final double BASE_VALUE_KILOMETERS = 385000.56;

    private MoonDistancePeriodicTerms periodicTerms = new MoonDistancePeriodicTerms();

    /**
     * Calculates the {@linkplain GlobalCoord#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}.
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in periodic terms
     * @return          {@linkplain GlobalCoord#MOON_EARTH_DISTANCE Moon-Earth distance (Δ)}, in km
     */
    public double calculate(TimelinePoint tx, MoonCoordinateElements elements) {
        return BASE_VALUE_KILOMETERS + periodicTerms.evaluate(tx.toDynamicalTime(), elements);
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.MOON_EARTH_DISTANCE;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.MOON_COORDINATE_ELEMENTS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(tx, (MoonCoordinateElements) precalculatedValues.get(GlobalCoord.MOON_COORDINATE_ELEMENTS));
    }
}
