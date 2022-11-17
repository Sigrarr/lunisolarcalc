package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public class MoonOverSunApparentLongitudeExcessCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS;

    /**
     * ESAA 1992, 9.213, p. 478; cf. Meeus 1998, Ch. 49, p. 349
     */
    public double calculateExcess(double moonLongitude, double sunAberratedLongitude) {
        return Calcs.normalizeLongitudinally(moonLongitude - sunAberratedLongitude);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_LONGITUDE, Subject.SUN_ABERRATED_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateExcess(
            (Double) calculatedValues.get(Subject.MOON_LONGITUDE),
            (Double) calculatedValues.get(Subject.SUN_ABERRATED_LONGITUDE)
        );
    }
}
