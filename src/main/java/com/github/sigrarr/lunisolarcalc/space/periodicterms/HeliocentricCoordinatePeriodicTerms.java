package com.github.sigrarr.lunisolarcalc.space.periodicterms;

/**
 * Meeus 1998, Ch. 32, p. 218
 */
public abstract class HeliocentricCoordinatePeriodicTerms {

    public double evaluateSeries(double tau, int seriesIndex) {
        return evaluateSeries(tau, getSeries(seriesIndex));
    }

    public double evaluateSeries(double tau, double[][] series) {
        double sum = 0.0;
        for (double[] row : series) {
            sum += evaluateTerm(tau, row);
        }
        return sum;
    }

    public double evaluateTerm(double tau, int seriesIndex, int rowIndex) {
        return evaluateTerm(tau, getSeries(seriesIndex)[rowIndex]);
    }

    public double evaluateTerm(double tau, double[] seriesRow) {
        return seriesRow[0] * Math.cos(seriesRow[1] + (seriesRow[2] * tau));
    }

    abstract public int getSeriesCount();
    abstract protected double[][] getSeries(int n);
}
