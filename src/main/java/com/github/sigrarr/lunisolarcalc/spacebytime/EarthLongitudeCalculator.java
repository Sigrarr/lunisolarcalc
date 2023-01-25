package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Earth's heliocentric longitude (L).
 * Costly; processes its own {@link EarthLongitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@link CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcComposition}.
 *
 * @see " Meeus 1998: Ch. 32 (p. 217...)
 */
public final class EarthLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.EARTH_LONGITUDE;

    private EarthLongitudePeriodicTerms periodicTerms = new EarthLongitudePeriodicTerms();

    /**
     * Calculates the Earth's heliocentric longitude (L): [0, 2π).
     * Costly.
     *
     * @param tx    time argument
     * @return      the Earth's heliocentric longitude (L): [0, 2π)
     */
    public double calculate(TimelinePoint tx) {
        return Calcs.Angle.normalizeLongitudinally(periodicTerms.evaluate(tx));
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
    public Object calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(tx);
    }
}
