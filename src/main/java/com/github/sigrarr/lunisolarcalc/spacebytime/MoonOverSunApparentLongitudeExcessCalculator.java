package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public class MoonOverSunApparentLongitudeExcessCalculator implements Provider<Subject, Double> {
    /**
     * ESAA 1992, 9.213, p. 478; cf. Meeus 1998, Ch. 49, p. 349
     */
    public double calculateExcess(double moonLongitude, double sunAberratedLongitude) {
        return Calcs.normalizeLongitudinally(moonLongitude - sunAberratedLongitude);
    }

    @Override
    public Subject provides() {
        return Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_LONGITUDE, Subject.SUN_ABERRATED_LONGITUDE);
    }

    @Override
    public Object calculate(Double rootArgument, Map<Subject, Object> requiredArguments) {
        return calculateExcess(
            (Double) requiredArguments.get(Subject.MOON_LONGITUDE),
            (Double) requiredArguments.get(Subject.SUN_ABERRATED_LONGITUDE)
        );
    }
}
