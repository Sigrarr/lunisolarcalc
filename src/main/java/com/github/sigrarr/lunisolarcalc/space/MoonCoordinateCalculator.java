package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;

public abstract class MoonCoordinateCalculator {

    protected MoonCoordinatePeriodicTerms periodicTerms;
    protected MoonCoordinateElements currentElements = new MoonCoordinateElements();

    public MoonCoordinateCalculator(MoonCoordinatePeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    public double calculate(double centurialT) {
        currentElements.calculate(centurialT);
        return periodicTerms.evaluate(centurialT, currentElements);
    }
}
