package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.MoonDistancePeriodicTerms;

public final class MoonDistanceCalculator extends MoonCoordinateCalculator {

    protected static final double BASE_VALUE_KILOMETERS = 385000.56;

    public MoonDistanceCalculator() {
        super(new MoonDistancePeriodicTerms());
    }

    @Override
    public double calculateCoordinate(double centurialT, MoonCoordinateElements elements) {
        return BASE_VALUE_KILOMETERS + super.calculateCoordinate(centurialT, elements);
    }
}
