package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;

public abstract class HeliocentricCoordinateCalculator {

    protected HeliocentricCoordinatePeriodicTerms periodicTerms;

    public HeliocentricCoordinateCalculator(HeliocentricCoordinatePeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, 32.2, p. 218
     */
    public double calculateCoordinate(double tau) {
        return periodicTerms.evaluate(tau);
    }
}
