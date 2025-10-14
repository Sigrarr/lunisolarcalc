package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.periodicterms.MoonLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#MOON_LATITUDE latitude of the Moon's center (β)}.
 * Costly; processes its own {@linkplain MoonLatitudePeriodicTerms periodic terms} table of considerable size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 47 (p. 337...)"
 */
public final class MoonLatitudeCalculator implements Provider<GlobalCoord, TimelinePoint> {

    private MoonLatitudePeriodicTerms periodicTerms = new MoonLatitudePeriodicTerms();

    /**
     * Calcules the {@linkplain GlobalCoord#MOON_LATITUDE latitude of the Moon's center (β)}.
     * Costly.
     *
     * @param tx        time argument
     * @param elements  intermediate arguments used in periodic terms
     * @return          {@linkplain GlobalCoord#MOON_LATITUDE latitude of the Moon's center (β)},
     *                  in radians: [-π/2, π/2]
     */
    public double calculate(TimelinePoint tx, MoonCoordinateElements elements) {
        return Calcs.Angle.toNormalLatitude(periodicTerms.evaluate(tx.toDynamicalTime(), elements));
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.MOON_LATITUDE;
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
