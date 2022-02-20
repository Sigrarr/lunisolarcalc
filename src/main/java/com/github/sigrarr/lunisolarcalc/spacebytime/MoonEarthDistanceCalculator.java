package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonDistancePeriodicTerms;

public final class MoonEarthDistanceCalculator extends MoonCoordinateCalculator {

    protected static final double BASE_VALUE_KILOMETERS = 385000.56;

    public MoonEarthDistanceCalculator() {
        super(new MoonDistancePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(double centurialT, MoonCoordinateElements elements) {
        return BASE_VALUE_KILOMETERS + super.calculateCoordinate(centurialT, elements);
    }

    @Override
    public Subject provides() {
        return Subject.MOON_EARTH_DISTANCE;
    }
}
