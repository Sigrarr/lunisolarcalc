package com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.SunSeasonPoint;

/**
 * Meeus 1998, Table 27.A-B, p. 178
 */
public class MeanSunSeasonPointApproximator {
    
    private static final double[][] COEFFICIENTS_FOR_YEARS_NEGATIVE_1K_TO_1K = {
        { 1721139.29189 , 365242.13740 , +0.06134 , +0.00111 , -0.00071 },
        { 1721233.25401 , 365241.72562 , -0.05323 , +0.00907 , +0.00025 },
        { 1721325.70455 , 365242.49558 , -0.11677 , -0.00297 , +0.00074 },
        { 1721414.39987 , 365242.88257 , -0.00769 , -0.00933 , -0.00006 },
    };
    private static final double[][] COEFFICIENTS_FOR_YEARS_1K_TO_3K = {
        { 2451623.80984 , 365242.37404 , +0.05169 , -0.00411 , -0.00057 },
        { 2451716.56767 , 365241.62603 , +0.00325 , +0.00888 , -0.00030 },
        { 2451810.21715 , 365242.01767 , -0.11575 , +0.00337 , +0.00078 },
        { 2451900.05952 , 365242.74049 , -0.06223 , -0.06223 , +0.00032 },
    };

    private int currentGregorianYear = Integer.MIN_VALUE;
    private double[][] currentSubtable = null;
    private double[] yPowers = {1.0, 0.0, 0.0, 0.0, 0.0};

    public double approximateJulianEphemerisDay(int gregorianYear, SunSeasonPoint point) {
        if (gregorianYear != currentGregorianYear) {
            if (gregorianYear > 1000) {
                currentSubtable = COEFFICIENTS_FOR_YEARS_1K_TO_3K;
                fillYPowers(0.001 * (gregorianYear - 2000));
            } else {
                currentSubtable = COEFFICIENTS_FOR_YEARS_NEGATIVE_1K_TO_1K;
                fillYPowers(0.001 * gregorianYear);
            }
            currentGregorianYear = gregorianYear;
        }
        return evaluate(currentSubtable[point.ordinal()]);
    }

    private double evaluate(double[] row) {
        double value = 0.0;
        for (int i = 0; i < row.length; i++) {
            value += row[i] * yPowers[i];
        }
        return value;
    }

    private void fillYPowers(double y1) {
        yPowers[1] = y1;
        yPowers[2] = y1 * y1;
        yPowers[3] = yPowers[2] * y1;
        yPowers[4] = yPowers[3] * y1;
    }
}
