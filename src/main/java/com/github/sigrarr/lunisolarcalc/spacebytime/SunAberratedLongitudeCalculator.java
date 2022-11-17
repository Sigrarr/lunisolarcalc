package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class SunAberratedLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_ABERRATED_LONGITUDE;

    public double calculateAberratedLongitude(double geometricLongitude, double aberration) {
        return Calcs.normalizeLongitudinally(geometricLongitude + aberration);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SUN_GEOMETRIC_LONGITUDE, Subject.ABERRATION_EARTH_SUN);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateAberratedLongitude(
            (Double) calculatedValues.get(Subject.SUN_GEOMETRIC_LONGITUDE),
            (Double) calculatedValues.get(Subject.ABERRATION_EARTH_SUN)
        );
    }
}
