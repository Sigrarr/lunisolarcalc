package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.Map;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthSunRadiusPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

public final class EarthSunRadiusCalculator extends HeliocentricCoordinateCalculator {

    public EarthSunRadiusCalculator() {
        super(new EarthSunRadiusPeriodicTerms());
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_SUN_RADIUS;
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateCoordinate(tx);
    }
}
