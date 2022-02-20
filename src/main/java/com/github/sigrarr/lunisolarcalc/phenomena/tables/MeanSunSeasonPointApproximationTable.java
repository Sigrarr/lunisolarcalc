package com.github.sigrarr.lunisolarcalc.phenomena.tables;

import com.github.sigrarr.lunisolarcalc.phenomena.SunSeasonPoint;

public class MeanSunSeasonPointApproximationTable {
    
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

    private double[] yPowers = {1.0, 0.0, 0.0, 0.0, 0.0};

    public double evaluate(int romanYear, SunSeasonPoint point) {
        double[] row;
        double y1;
        int rowIndex = point.ordinal();
        if (romanYear > 1000) {
            row = COEFFICIENTS_FOR_YEARS_1K_TO_3K[rowIndex];
            y1 = 0.001 * (romanYear - 2000);
        } else {
            row = COEFFICIENTS_FOR_YEARS_NEGATIVE_1K_TO_1K[rowIndex];
            y1 = 0.001 * romanYear;
        }
        return evaluate(row, y1);
    }

    private double evaluate(double[] row, double y1) {
        fillYPowers(y1);
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
