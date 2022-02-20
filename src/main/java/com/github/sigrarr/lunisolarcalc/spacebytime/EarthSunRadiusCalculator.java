package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.Map;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthSunRadiusPeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.Timeline;

public final class EarthSunRadiusCalculator extends HeliocentricCoordinateCalculator {

    public EarthSunRadiusCalculator() {
        super(new EarthSunRadiusPeriodicTerms());
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_SUN_RADIUS;
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> arguments) {
        return calculateCoordinate(Timeline.centurialTToMillenialTau(centurialT));
    }
}
