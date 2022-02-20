package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public final class MoonApparentLongitudeCalculator implements Provider<Subject, Double, Double> {
    /**
     * Meeus 1998, Example 47.a, p. 343
     */
    public double calculateApparentLongitude(double longitude, double earthNutuationInLongitude) {
        return Calcs.normalizeLongitudinally(longitude + earthNutuationInLongitude);
    }

    @Override
    public Subject provides() {
        return Subject.MOON_APPARENT_LONGITUDE;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_LONGITUDE, Subject.EARTH_NUTUATION_IN_LONGITUDE);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> arguments) {
        return calculateApparentLongitude((Double) arguments.get(Subject.MOON_LONGITUDE), (Double) arguments.get(Subject.EARTH_NUTUATION_IN_LONGITUDE));
    }
}
