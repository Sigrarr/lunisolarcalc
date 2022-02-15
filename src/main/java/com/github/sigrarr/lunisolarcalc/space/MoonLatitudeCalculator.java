package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.MoonLatitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class MoonLatitudeCalculator extends MoonCoordinateCalculator {

    public MoonLatitudeCalculator() {
        super(new MoonLatitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(double centurialT, MoonCoordinateElements elements) {
        return Calcs.normalizeLatitudinally(super.calculateCoordinate(centurialT, elements));
    }
}
