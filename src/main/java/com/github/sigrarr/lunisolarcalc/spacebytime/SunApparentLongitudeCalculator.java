package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class SunApparentLongitudeCalculator implements Provider<Subject, Double> {
    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 167 
     */
    public double calculateApparentLongitude(double geometricLongitude, double nutuationInLongitude, double aberration) {
        return Calcs.normalizeLongitudinally(geometricLongitude + nutuationInLongitude + aberration);
    }

    @Override
    public Subject provides() {
        return Subject.SUN_APPARENT_LONGITUDE;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SUN_GEOMETRIC_LONGITUDE, Subject.EARTH_NUTUATION_IN_LONGITUDE, Subject.ABERRATION_EARTH_SUN);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> calculatedValues) {
        return calculateApparentLongitude(
            (Double) calculatedValues.get(Subject.SUN_GEOMETRIC_LONGITUDE),
            (Double) calculatedValues.get(Subject.EARTH_NUTUATION_IN_LONGITUDE),
            (Double) calculatedValues.get(Subject.ABERRATION_EARTH_SUN)
        );
    }
}
