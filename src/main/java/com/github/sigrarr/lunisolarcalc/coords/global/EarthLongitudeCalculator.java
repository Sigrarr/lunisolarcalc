package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}.
 * Costly; processes its own {@linkplain EarthLongitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 32 (p. 217...)"
 */
public final class EarthLongitudeCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.EARTH_LONGITUDE;

    private EarthLongitudePeriodicTerms periodicTerms = new EarthLongitudePeriodicTerms();

    /**
     * Calculates {@linkplain GlobalCoord#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}: [0, 2π).
     * Costly.
     *
     * @param tx    time argument
     * @return      {@linkplain GlobalCoord#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}: [0, 2π)
     */
    public double calculate(TimelinePoint tx) {
        return Calcs.Angle.toNormalLongitude(periodicTerms.evaluate(tx.toDynamicalTime()));
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
