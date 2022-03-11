package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class SunAberratedLongitudeCalculator implements Provider<Subject, Double> {

    public double calculateAberratedLongitude(double geometricLongitude, double aberration) {
        return Calcs.normalizeLongitudinally(geometricLongitude + aberration);
    }

    @Override
    public Subject provides() {
        return Subject.SUN_ABERRATED_LONGITUDE;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SUN_GEOMETRIC_LONGITUDE, Subject.ABERRATION_EARTH_SUN);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> calculatedValues) {
        return calculateAberratedLongitude(
            (Double) calculatedValues.get(Subject.SUN_GEOMETRIC_LONGITUDE),
            (Double) calculatedValues.get(Subject.ABERRATION_EARTH_SUN)
        );
    }
}
