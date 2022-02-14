package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;

public abstract class MoonCoordinateCalculator {

    protected MoonCoordinatePeriodicTerms periodicTerms;

    public MoonCoordinateCalculator(MoonCoordinatePeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    public double calculateCoordinate(double centurialT, MoonCoordinateElements elements) {
        return periodicTerms.evaluate(centurialT, elements);
    }
}
