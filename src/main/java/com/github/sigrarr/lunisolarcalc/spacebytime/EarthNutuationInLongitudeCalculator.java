package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.EarthNutuationInLongitudePeriodicTerms;

public final class EarthNutuationInLongitudeCalculator extends EarthNutuationCalculator {

    public EarthNutuationInLongitudeCalculator() {
        super(new EarthNutuationInLongitudePeriodicTerms());
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_NUTUATION_IN_LONGITUDE;
    }
}
