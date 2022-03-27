package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLongitudeCalculator extends HeliocentricCoordinateCalculator {
    
    public EarthLongitudeCalculator() {
        super(new EarthLongitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(TimelinePoint tx) {
        return Calcs.normalizeLongitudinally(super.calculateCoordinate(tx));
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_LONGITUDE;
    }
}
