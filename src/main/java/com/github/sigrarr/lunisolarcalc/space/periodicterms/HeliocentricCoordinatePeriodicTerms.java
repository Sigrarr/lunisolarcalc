package com.github.sigrarr.lunisolarcalc.space.periodicterms;

/**
 * Meeus 1998, Ch. 32, p. 218
 */
public abstract class HeliocentricCoordinatePeriodicTerms {

    protected final static double SCALE = 0.00000001;

    public double evaluate(double tau) {
        double total = 0.0;
        int seriesCount = getNumberOfSeries();
        for (int n = 0; n < seriesCount; n++) {
            total += evaluateSeries(tau, n) * Math.pow(tau, n);
        }
        return total * SCALE;
    }

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

    abstract public int getNumberOfSeries();
    abstract protected double[][] getSeries(int n);
}
