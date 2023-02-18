package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.periodicterms.EarthLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain Subject#EARTH_LATITUDE the Earth's heliocentric latitude (B)}.
 * Somewhat costly; processes its own {@linkplain EarthLatitudePeriodicTerms periodic terms} table of small size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 32 (p. 217...)"
 */
public final class EarthLatitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.EARTH_LATITUDE;

    private EarthLatitudePeriodicTerms periodicTerms = new EarthLatitudePeriodicTerms();

    /**
     * Calculates {@linkplain Subject#EARTH_LATITUDE the Earth's heliocentric latitude (B)}: [-π/2, π/2].
     * Somewhat costly.
     *
     * @param tx    time argument
     * @return      {@linkplain Subject#EARTH_LATITUDE the Earth's heliocentric latitude (B)}: [-π/2, π/2]
     */
    public double calculate(TimelinePoint tx) {
        return Calcs.Angle.toNormalLatitude(periodicTerms.evaluate(tx.toDynamicalTime()));
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
