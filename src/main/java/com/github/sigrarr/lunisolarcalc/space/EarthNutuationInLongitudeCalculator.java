package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.EarthNutuationInLongitudePeriodicTerms;

public final class EarthNutuationInLongitudeCalculator extends EarthNutuationCalculator {
    public EarthNutuationInLongitudeCalculator() {
        super(new EarthNutuationInLongitudePeriodicTerms());
    }
}
