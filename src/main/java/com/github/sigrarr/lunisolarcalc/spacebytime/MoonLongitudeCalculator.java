package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.MoonLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class MoonLongitudeCalculator extends MoonCoordinateCalculator {

    public static final Subject SUBJECT = Subject.MOON_LONGITUDE;

    public MoonLongitudeCalculator() {
        super(new MoonLongitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(TimelinePoint tx, MoonCoordinateElements elements) {
        return Calcs.normalizeLongitudinally(elements.getLPrim() + super.calculateCoordinate(tx, elements));
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }
}
