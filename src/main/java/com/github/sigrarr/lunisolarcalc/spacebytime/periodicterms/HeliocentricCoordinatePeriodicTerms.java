package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

/**
 * Periodic terms for heliocentric coordinate.
 *
 * @see " Meeus 1998: Ch. 32 (p. 217...); App. III, Earth (p. 418...)
 */
public abstract class HeliocentricCoordinatePeriodicTerms {

    protected final static double SCALE = 0.00000001;

    public double evaluate(TimelinePoint tx) {
        double tau = tx.toMillenialTau();
        double total = 0.0;
        int seriesCount = getNumberOfSeries();
        for (int n = 0; n < seriesCount; n++) {
            total += evaluateSeries(tau, getSeries(n)) * Math.pow(tau, n);
        }
        return total * SCALE;
    }

    protected double evaluateSeries(TimelinePoint tx, int seriesIndex) {
        return evaluateSeries(tx.toMillenialTau(), getSeries(seriesIndex));
    }

    protected double evaluateSeries(double tau, double[][] series) {
        double sum = 0.0;
        for (double[] row : series) {
            sum += evaluateTerm(tau, row);
        }
        return sum;
    }

    protected double evaluateTerm(TimelinePoint tx, int seriesIndex, int rowIndex) {
        return evaluateTerm(tx.toMillenialTau(), getSeries(seriesIndex)[rowIndex]);
    }

    protected double evaluateTerm(double tau, double[] seriesRow) {
        return seriesRow[0] * Math.cos(seriesRow[1] + (seriesRow[2] * tau));
    }

    abstract protected int getNumberOfSeries();
    abstract protected double[][] getSeries(int n);
}
