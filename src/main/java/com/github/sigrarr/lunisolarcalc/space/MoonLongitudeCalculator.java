package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.MoonLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class MoonLongitudeCalculator extends MoonCoordinateCalculator {

    public MoonLongitudeCalculator() {
        super(new MoonLongitudePeriodicTerms());
    }

    @Override
    public double calculate(double centurialT) {
        double baseValue = super.calculate(centurialT);
        return Calcs.normalizeLongitudinally(currentElements.getLPrim() + baseValue);
    }
}
