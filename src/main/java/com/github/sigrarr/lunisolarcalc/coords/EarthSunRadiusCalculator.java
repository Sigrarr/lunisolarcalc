package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.periodicterms.EarthSunRadiusPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Earth's {@linkplain Subject#EARTH_SUN_RADIUS radius vector (R)}.
 * Costly; processes its own {@linkplain EarthSunRadiusPeriodicTerms periodic terms} table.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 32 (p. 217...)"
 */
public final class EarthSunRadiusCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.EARTH_SUN_RADIUS;

    private EarthSunRadiusPeriodicTerms periodicTerms = new EarthSunRadiusPeriodicTerms();

    /**
     * Calculates the Earth's {@linkplain Subject#EARTH_SUN_RADIUS radius vector (R)}, in AU.
     * Costly.
     *
     * @param tx    time argument
     * @return      the Earth's {@linkplain Subject#EARTH_SUN_RADIUS radius vector (R)}, in AU
     */
    public double calculate(TimelinePoint tx) {
        return periodicTerms.evaluate(tx.toDynamicalTime());
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.noneOf(Subject.class);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(tx);
    }
}
