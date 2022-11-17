package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthNutuationInLongitudePeriodicTerms;

public final class EarthNutuationInLongitudeCalculator extends EarthNutuationCalculator {

    public static final Subject SUBJECT = Subject.EARTH_NUTUATION_IN_LONGITUDE;

    public EarthNutuationInLongitudeCalculator() {
        super(new EarthNutuationInLongitudePeriodicTerms());
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }
}
