package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;

public abstract class HeliocentricCoordinateCalculator {

    private final static double SCALE = 0.00000001;
    protected PeriodicTermsForHeliocentricCoordinate periodicTerms;

    public HeliocentricCoordinateCalculator(PeriodicTermsForHeliocentricCoordinate periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, 32.2, p. 218
     */
    public double calculate(double tau) {
        double total = 0.0;
        int seriesCount = periodicTerms.getSeriesCount();
        for (int n = 0; n < seriesCount; n++) {
            total += periodicTerms.evaluateSeries(tau, n) * Math.pow(tau, n);
        }
        return total * SCALE;
    }
}
