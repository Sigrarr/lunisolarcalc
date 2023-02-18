package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain Subject#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}.
 * Costly; processes its own {@linkplain EarthLongitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 32 (p. 217...)"
 */
public final class EarthLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.EARTH_LONGITUDE;

    private EarthLongitudePeriodicTerms periodicTerms = new EarthLongitudePeriodicTerms();

    /**
     * Calculates {@linkplain Subject#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}: [0, 2π).
     * Costly.
     *
     * @param tx    time argument
     * @return      {@linkplain Subject#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}: [0, 2π)
     */
    public double calculate(TimelinePoint tx) {
        return Calcs.Angle.toNormalLongitude(periodicTerms.evaluate(tx.toDynamicalTime()));
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
