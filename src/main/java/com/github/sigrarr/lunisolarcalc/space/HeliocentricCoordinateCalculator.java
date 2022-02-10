package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class HeliocentricCoordinateCalculator {

    public static enum Unit {
        RADIAN, ASTRONOMICAL_UNIT
    }

    private final static double SCALE = 0.00000001;
    protected PeriodicTermsForHeliocentricCoordinate periodicTerms;
    protected Unit unit;

    public HeliocentricCoordinateCalculator(PeriodicTermsForHeliocentricCoordinate periodicTerms, Unit unit) {
        this.periodicTerms = periodicTerms;
        this.unit = unit;
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
        double value = total * SCALE;
        return unit == Unit.RADIAN ? Calcs.normalizeAngle(value) : value;
    }
}
