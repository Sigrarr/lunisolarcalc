package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class MoonLatitudeCalculator extends MoonCoordinateCalculator {

    public MoonLatitudeCalculator() {
        super(new MoonLatitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(TimelinePoint tx, MoonCoordinateElements elements) {
        return Calcs.normalizeLatitudinally(super.calculateCoordinate(tx, elements));
    }

    @Override
    public Subject provides() {
        return Subject.MOON_LATITUDE;
    }
}
