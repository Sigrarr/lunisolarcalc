package com.github.sigrarr.lunisolarcalc.util;

/**
 * @see "Numerical Recipes", 3.3
 */
public class NaturalCubicSplineInterpolation {

    private double[] args;
    private double[] vals;
    private double[] d2Vals;

    public NaturalCubicSplineInterpolation(double[] args, double[] vals) {
        reset(args, vals);
    }

    public void reset(double[] args, double[] vals) {
        validate(args, vals);
        this.args = args;
        this.vals = vals;
        int n = args.length;
        d2Vals = new double[n];

        double[] u = new double[n - 1];
        d2Vals[0] = u[0] = 0.0;
        for (int i = 1; i < n - 1; i++) {
            double sig = (args[i] - args[i-1]) / (args[i+1] - args[i]);
            double p = sig * d2Vals[i-1] + 2.0;
            d2Vals[i] = (sig - 1.0) / p;
            u[i] = (vals[i+1] - vals[i]) / (args[i+1] - args[i]) - (vals[i] - vals[i-1]) / (args[i] - args[i-1]);
            u[i] = (6.0 * u[i] / (args[i+1] - args[i-1]) - sig * u[i-1]) / p;
        }
        d2Vals[n - 1] = 0.0;
        for (int k = n - 2; k >= 0; k--)
            d2Vals[k] = d2Vals[k] * d2Vals[k+1] + u[k];
    }

    public double interpolate(double x) {
        validate(x);
        int floorIndex = 0, ceilIndex = 1;
        for (; ceilIndex < args.length; floorIndex++, ceilIndex++)
            if (Double.compare(x, args[floorIndex]) >= 0 && Double.compare(x, args[ceilIndex]) <= 0)
                break;

        double h = args[ceilIndex] - args[floorIndex];
        double a = (args[ceilIndex] - x) / h;
        double b = (x - args[floorIndex]) / h;

        return a * vals[floorIndex]
            + b * vals[ceilIndex]
            + (
                (a*a*a - a) * d2Vals[floorIndex]
                + (b*b*b - b) * d2Vals[ceilIndex]
            ) * h*h / 6.0;
    }

    private void validate(double[] args, double[] vals) {
        if (args.length != vals.length || args.length < 2)
            throw new IllegalArgumentException();
        double prevDiffSignum = Double.NaN;
        for (int i = 0; i < args.length - 1; i++) {
            double diffSignum = Math.signum(args[i+1] - args[i]);
            if (diffSignum == 0.0)
                throw new IllegalArgumentException();
            if (!Double.isNaN(prevDiffSignum) && diffSignum != prevDiffSignum)
                throw new IllegalArgumentException();
            prevDiffSignum = diffSignum;
        }
    }

    private void validate(double x) {
        if (Double.compare(x, args[0]) < 0 || Double.compare(x, args[args.length-1]) > 0)
            throw new IllegalArgumentException();
    }
}
