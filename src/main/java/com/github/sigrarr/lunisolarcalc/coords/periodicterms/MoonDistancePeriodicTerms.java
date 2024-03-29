package com.github.sigrarr.lunisolarcalc.coords.periodicterms;

/**
 * Periodic terms for distance between the centers of Earth and Moon (Δ).
 * Results are in km.
 *
 * @see com.github.sigrarr.lunisolarcalc.coords.MoonEarthDistanceCalculator
 * @see "Meeus 1998: Table 47.A (pp. 339-340)"
 */
public final class MoonDistancePeriodicTerms extends MoonCoordinatePeriodicTerms {

    protected static final double SCALE_KILOMETERS = 0.001;

    // : Argument \ Multiple of D M M' F
    protected static final short[][] ELEMENTS_MULTIPLIERS = {
        { 0 , 0 , 1 , 0 },
        { 2 , 0 , -1 , 0 },
        { 2 , 0 , 0 , 0 },
        { 0 , 0 , 2 , 0 },
        { 0 , 1 , 0 , 0 },
        { 0 , 0 , 0 , 2 },
        { 2 , 0 , -2 , 0 },
        { 2 , -1 , -1 , 0 },
        { 2 , 0 , 1 , 0 },
        { 2 , -1 , 0 , 0 },
        { 0 , 1 , -1 , 0 },
        { 1 , 0 , 0 , 0 },
        { 0 , 1 , 1 , 0 },
        { 2 , 0 , 0 , -2 },
        { 0 , 0 , 1 , -2 },
        { 4 , 0 , -1 , 0 },
        { 0 , 0 , 3 , 0 },
        { 4 , 0 , -2 , 0 },
        { 2 , 1 , -1 , 0 },
        { 2 , 1 , 0 , 0 },
        { 1 , 0 , -1 , 0 },
        { 1 , 1 , 0 , 0 },
        { 2 , -1 , 1 , 0 },
        { 2 , 0 , 2 , 0 },
        { 4 , 0 , 0 , 0 },
        { 2 , 0 , -3 , 0 },
        { 0 , 1 , -2 , 0 },
        { 2 , -1 , -2 , 0 },
        { 1 , 0 , 1 , 0 },
        { 2 , -2 , 0 , 0 },
        { 0 , 1 , 2 , 0 },
        { 2 , -2 , -1 , 0 },
        { 2 , 0 , 1 , -2 },
        { 4 , -1 , -1 , 0 },
        { 3 , 0 , -1 , 0 },
        { 2 , 1 , 1 , 0 },
        { 4 , -1 , -2 , 0 },
        { 0 , 2 , -1 , 0 },
        { 2 , 2 , -1 , 0 },
        { 4 , 0 , 1 , 0 },
        { 0 , 0 , 4 , 0 },
        { 4 , -1 , 0 , 0 },
        { 1 , 0 , -2 , 0 },
        { 0 , 0 , 2 , -2 },
        { 0 , 2 , 1 , 0 },
        { 2 , 0 , -1 , -2 },
    };

    // : Σr \ Coefficient of the cosine...
    protected static final int[] COEFFICIENTS = {
        -20905355,
        -3699111,
        -2955968,
        -569925,
        48888,
        -3149,
        246158,
        -152138,
        -170733,
        -204586,
        -129620,
        108743,
        104755,
        10321,
        79661,
        -34782,
        -23210,
        -21636,
        24208,
        30824,
        -8379,
        -16675,
        -12831,
        -10445,
        -11650,
        14403,
        -7003,
        10056,
        6322,
        -9884,
        5751,
        -4950,
        4130,
        -3958,
        3258,
        2616,
        -1897,
        -2117,
        2354,
        -1423,
        -1117,
        -1571,
        -1739,
        -4421,
        1165,
        8752,
    };

    @Override
    protected double scale(double rawValue) {
        return rawValue * SCALE_KILOMETERS;
    }

    @Override
    protected int getSeriesLength() {
        return COEFFICIENTS.length;
    }

    @Override
    protected short[] getElementMultiplierRow(int n) {
        return ELEMENTS_MULTIPLIERS[n];
    }

    @Override
    protected int getCoefficient(int n) {
        return COEFFICIENTS[n];
    }

    @Override
    protected double applyTrigonometricFunction(double argument) {
        return Math.cos(argument);
    }
}
