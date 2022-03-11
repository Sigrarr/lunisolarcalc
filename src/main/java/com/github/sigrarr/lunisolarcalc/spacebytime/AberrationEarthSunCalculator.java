package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.SunLongitudeVariationPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.ConstantsAndUnits;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class AberrationEarthSunCalculator implements Provider<Subject, Double> {

    private static final double AU_LIGHT_TIME_DAYS = (
        (double) ConstantsAndUnits.ASTRONOMICAL_UNIT_METERS / (double) ConstantsAndUnits.LIGHT_SPEED_METERS_PER_SECOND
    ) / 3600.0 / 24.0;

    private SunLongitudeVariationPeriodicTerms periodicTerms = new SunLongitudeVariationPeriodicTerms();
    
    public double calculateAberration(double tau, double radius) {
        double deltaLambda = periodicTerms.evaluate(tau);
        return -AU_LIGHT_TIME_DAYS * radius * deltaLambda;
    }

    @Override
    public Subject provides() {
        return Subject.ABERRATION_EARTH_SUN;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_SUN_RADIUS);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> calculatedValues) {
        return calculateAberration(
            Timeline.centurialTToMillenialTau(centurialT),
            (Double) calculatedValues.get(Subject.EARTH_SUN_RADIUS)
        );
    }
}
