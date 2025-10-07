package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.periodicterms.EarthSunRadiusPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Earth's {@linkplain GlobalCoord#EARTH_SUN_RADIUS radius vector (R)}.
 * Costly; processes its own {@linkplain EarthSunRadiusPeriodicTerms periodic terms} table.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 32 (p. 217...)"
 */
public final class EarthSunRadiusCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.EARTH_SUN_RADIUS;

    private EarthSunRadiusPeriodicTerms periodicTerms = new EarthSunRadiusPeriodicTerms();

    /**
     * Calculates the Earth's {@linkplain GlobalCoord#EARTH_SUN_RADIUS radius vector (R)}, in AU.
     * Costly.
     *
     * @param tx    time argument
     * @return      the Earth's {@linkplain GlobalCoord#EARTH_SUN_RADIUS radius vector (R)}, in AU
     */
    public double calculate(TimelinePoint tx) {
        return periodicTerms.evaluate(tx.toDynamicalTime());
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.noneOf(GlobalCoord.class);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(tx);
    }
}
