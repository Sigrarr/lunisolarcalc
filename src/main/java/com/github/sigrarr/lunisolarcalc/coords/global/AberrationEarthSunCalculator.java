package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.periodicterms.SunLongitudeVariationPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#ABERRATION_EARTH_SUN aberration of the Sun's geocentric position}.
 * Somewhat costly; processes its own {@linkplain SunLongitudeVariationPeriodicTerms periodic terms} table of moderate size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: 25.11 (p. 167)"
 */
public final class AberrationEarthSunCalculator implements Provider<GlobalCoord, TimelinePoint> {

    private static final double AU_LIGHT_TIME_DAYS = (
        (double) ConstantsAndUnits.ASTRONOMICAL_UNIT_METERS / (double) ConstantsAndUnits.LIGHT_SPEED_METERS_PER_SECOND
    ) / Calcs.DAY_SECONDS;

    private SunLongitudeVariationPeriodicTerms periodicTerms = new SunLongitudeVariationPeriodicTerms();

    /**
     * Calculates the {@linkplain GlobalCoord#ABERRATION_EARTH_SUN aberration of the Sun's geocentric position}.
     * Somewhat costly.
     *
     * @param tx        time argument
     * @param radius    Earth-Sun {@linkplain GlobalCoord#EARTH_SUN_RADIUS radius vector (R)}, in AU
     * @return          {@linkplain GlobalCoord#ABERRATION_EARTH_SUN aberration of the Sun's geocentric position},
     *                  in radians
     */
    public double calculate(TimelinePoint tx, double radius) {
        double deltaLambda = periodicTerms.evaluate(tx.toDynamicalTime());
        return -AU_LIGHT_TIME_DAYS * radius * deltaLambda;
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.ABERRATION_EARTH_SUN;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.EARTH_SUN_RADIUS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(tx, (Double) precalculatedValues.get(GlobalCoord.EARTH_SUN_RADIUS));
    }
}
