package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonDistancePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

public final class MoonEarthDistanceCalculator extends MoonCoordinateCalculator {

    public static final Subject SUBJECT = Subject.MOON_EARTH_DISTANCE;
    protected static final double BASE_VALUE_KILOMETERS = 385000.56;

    public MoonEarthDistanceCalculator() {
        super(new MoonDistancePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(TimelinePoint tx, MoonCoordinateElements elements) {
        return BASE_VALUE_KILOMETERS + super.calculateCoordinate(tx, elements);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }
}
