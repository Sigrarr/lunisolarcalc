package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.MoonLongitudePeriodicTerms;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class MoonLongitudeCalculator extends MoonCoordinateCalculator {

    public MoonLongitudeCalculator() {
        super(new MoonLongitudePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(double centurialT, MoonCoordinateElements elements) {
        return Calcs.normalizeLongitudinally(elements.getLPrim() + super.calculateCoordinate(centurialT, elements));
    }
}
