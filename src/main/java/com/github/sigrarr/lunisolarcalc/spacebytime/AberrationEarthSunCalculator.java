package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.SunLongitudeVariationPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.ConstantsAndUnits;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class AberrationEarthSunCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.ABERRATION_EARTH_SUN;

    private static final double AU_LIGHT_TIME_DAYS = (
        (double) ConstantsAndUnits.ASTRONOMICAL_UNIT_METERS / (double) ConstantsAndUnits.LIGHT_SPEED_METERS_PER_SECOND
    ) / 3600.0 / 24.0;

    private SunLongitudeVariationPeriodicTerms periodicTerms = new SunLongitudeVariationPeriodicTerms();

    public double calculateAberration(TimelinePoint tx, double radius) {
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
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateAberration(tx, (Double) calculatedValues.get(Subject.EARTH_SUN_RADIUS));
    }
}
