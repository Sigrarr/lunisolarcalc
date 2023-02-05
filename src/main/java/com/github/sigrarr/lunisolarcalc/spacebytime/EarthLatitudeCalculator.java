package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Earth's heliocentric latitude (B).
 * Somewhat costly; processes its own {@linkplain EarthLatitudePeriodicTerms periodic terms} table of small size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 32 (p. 217...)"
 */
public final class EarthLatitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.EARTH_LATITUDE;

    private EarthLatitudePeriodicTerms periodicTerms = new EarthLatitudePeriodicTerms();

    /**
     * Calculates the Earth's heliocentric latitude (B): [-π/2, π/2].
     * Somewhat costly.
     *
     * @param tx    time argument
     * @return      the Earth's heliocentric latitude (B): [-π/2, π/2]
     */
    public double calculate(TimelinePoint tx) {
        return Calcs.Angle.normalizeLatitudinally(periodicTerms.evaluate(tx));
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
