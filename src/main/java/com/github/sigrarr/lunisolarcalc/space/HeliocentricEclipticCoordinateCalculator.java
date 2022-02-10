package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class HeliocentricEclipticCoordinateCalculator {

    private final static double UNIT_RADIANS = 0.00000001;
    protected PeriodicTermsForHeliocentricCoordinate periodicTerms;

    public HeliocentricEclipticCoordinateCalculator(PeriodicTermsForHeliocentricCoordinate periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, 32.2, p. 218
     */
    public double calculateCoordinate(double tau) {
        double total = 0.0;
        int seriesCount = periodicTerms.getSeriesCount();
        for (int n = 0; n < seriesCount; n++) {
            total += periodicTerms.evaluateSeries(tau, n) * Math.pow(tau, n);
        }
        double longitudeRadians = total * UNIT_RADIANS;
        return Calcs.normalizeAngle(longitudeRadians);
    }
}
