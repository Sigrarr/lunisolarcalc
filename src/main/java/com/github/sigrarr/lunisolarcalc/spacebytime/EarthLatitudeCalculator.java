package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class EarthLatitudeCalculator extends HeliocentricCoordinateCalculator {

    public static final Subject SUBJECT = Subject.EARTH_LATITUDE;

    public EarthLatitudeCalculator() {
        super(new EarthLatitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(TimelinePoint tx) {
        return Calcs.normalizeLatitudinally(super.calculateCoordinate(tx));
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }
}
