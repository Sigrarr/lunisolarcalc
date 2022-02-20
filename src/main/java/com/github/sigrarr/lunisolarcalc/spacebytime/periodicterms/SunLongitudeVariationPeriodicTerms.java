package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Meeus 1998, Ch. 25, Daily variation..., p. 168
 */
public final class SunLongitudeVariationPeriodicTerms {

    public static final double INITIAL_TERM = 3548.193;
    protected static final double COEFFICIENT_EARTH_ORBIT_A =  359993.7286;
    protected static final double COEFFICIENT_EARTH_ORBIT_B =  719987.4571;
    protected static final double COEFFICIENT_EARTH_ORBIT_C = 1079981.1857;
    protected static final double COEFFICIENT_MOON_A = 4452671.1152;
    protected static final double COEFFICIENT_MOON_B = 9224659.7915;
    protected static final double COEFFICIENT_MOON_C = 4092677.3866;
    protected static final double COEFFICIENT_VENUS_A = 450368.8564;
    protected static final double COEFFICIENT_VENUS_B = 225184.4282;
    protected static final double COEFFICIENT_VENUS_C = 315559.5560;
    protected static final double COEFFICIENT_VENUS_D = 675553.2846;
    protected static final double COEFFICIENT_JUPITER_A = 329644.6718;
    protected static final double COEFFICIENT_JUPITER_B = 659289.3436;
    protected static final double COEFFICIENT_JUPITER_C = 299295.6151;
    protected static final double COEFFICIENT_MARS_A = 337181.4711;
    protected static final double[][][] SERIES_ARRAY = {
        {
            { 118.586 , Math.toRadians(87.5287) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_A) },
            { 2.476 , Math.toRadians(85.0561) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_B) },
            { 1.376 , Math.toRadians(27.8502) , Math.toRadians(COEFFICIENT_MOON_A) },
            { 0.119 , Math.toRadians(73.1375) , Math.toRadians(COEFFICIENT_VENUS_A) },
            { 0.114 , Math.toRadians(337.2264) , Math.toRadians(COEFFICIENT_JUPITER_A) },
            { 0.086 , Math.toRadians(222.54) , Math.toRadians(COEFFICIENT_JUPITER_B) },
            { 0.078 , Math.toRadians(162.8136) , Math.toRadians(COEFFICIENT_MOON_B) },
            { 0.054 , Math.toRadians(82.5823) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_C) },
            { 0.052 , Math.toRadians(171.5189) , Math.toRadians(COEFFICIENT_VENUS_B) },
            { 0.034 , Math.toRadians(30.3214) , Math.toRadians(COEFFICIENT_MOON_C) },
            { 0.033 , Math.toRadians(119.8105) , Math.toRadians(COEFFICIENT_MARS_A) },
            { 0.023 , Math.toRadians(247.5418) , Math.toRadians(COEFFICIENT_JUPITER_C) },
            { 0.023 , Math.toRadians(325.1526) , Math.toRadians(COEFFICIENT_VENUS_C) },
            { 0.021 , Math.toRadians(155.1241) , Math.toRadians(COEFFICIENT_VENUS_D) },
        },
        {
            { 7.311 , Math.toRadians(333.4515) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_A) },
            { 0.305 , Math.toRadians(330.9814) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_B) },
            { 0.01 , Math.toRadians(328.517) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_C) },
        },
        {
            { 0.309 , Math.toRadians(241.4518) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_A) },
            { 0.021 , Math.toRadians(205.0482) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_B) },
            { 0.004 , Math.toRadians(297.861) , Math.toRadians(COEFFICIENT_MOON_A) },
        },
        {
            { 0.01 , Math.toRadians(154.7066) , Math.toRadians(COEFFICIENT_EARTH_ORBIT_A) },
        }
    };

    public double evaluate(double tau) {
        double result = INITIAL_TERM;
        for (int n = 0; n < SERIES_ARRAY.length; n++) {
            for (double[] row : SERIES_ARRAY[n]) {
                result += evaluateTermRaw(tau, n, row);
            }
        }
        return scale(result);
    }

    public double evaluateTerm(double tau, int seriexIndex, double[] row) {
        return scale(evaluateTermRaw(tau, seriexIndex, row));
    }

    protected double evaluateTermRaw(double tau, int seriesIndex, double[] row) {
        return row[0] * Math.pow(tau, seriesIndex) * Math.sin(row[1] + (row[2] * tau));
    }

    protected double scale(double value) {
        return Math.toRadians(Calcs.arcsecondsToDegrees(value));
    }
}
