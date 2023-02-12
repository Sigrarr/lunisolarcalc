package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.periodicterms.SunLongitudeVariationPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of aberration of the Sun's geocentric position (caused by the Earth's motion).
 * Somewhat costly; processes its own {@linkplain SunLongitudeVariationPeriodicTerms periodic terms} table of moderate size.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CalcCompositions}.
 *
 * @see "Meeus 1998: 25.11 (p. 167)"
 */
public final class AberrationEarthSunCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.ABERRATION_EARTH_SUN;
    private static final double AU_LIGHT_TIME_DAYS = (
        (double) ConstantsAndUnits.ASTRONOMICAL_UNIT_METERS / (double) ConstantsAndUnits.LIGHT_SPEED_METERS_PER_SECOND
    ) / Calcs.DAY_SECONDS;

    private SunLongitudeVariationPeriodicTerms periodicTerms = new SunLongitudeVariationPeriodicTerms();

    /**
     * Calculates aberration of the Sun's geocentric position, in radians.
     * Somewhat costly.
     *
     * @param tx        time argument
     * @param radius    {@linkplain EarthSunRadiusCalculator Earth-Sun radius vector} (R), in AU
     * @return          aberration of the Sun's geocentric position, in radians
     */
    public double calculate(TimelinePoint tx, double radius) {
        double deltaLambda = periodicTerms.evaluate(tx.toDynamicalTime());
        return -AU_LIGHT_TIME_DAYS * radius * deltaLambda;
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_SUN_RADIUS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(tx, (Double) precalculatedValues.get(Subject.EARTH_SUN_RADIUS));
    }
}
