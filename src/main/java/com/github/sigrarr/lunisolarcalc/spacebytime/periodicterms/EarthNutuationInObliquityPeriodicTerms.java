package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

/**
 * Periodic terms for the Earth's nutuation in obliquity (Δε).
 * Results are in radians.
 *
 * @see com.github.sigrarr.lunisolarcalc.spacebytime.EarthNutuationInObliquityCalculator
 * @see "Meeus 1998: Table 22.A (pp. 145-146)"
 */
public class EarthNutuationInObliquityPeriodicTerms extends EarthNutuationPeriodicTerms {

    // : Δε \ Coefficient of the cosine...
    protected static final double[][] NON_ZERO_COEFFICIENTS = {
        {92025, 8.9},
        {5736, -3.1},
        {977, -0.5},
        {-895, 0.5},
        {54, -0.1},
        {-7, 0},
        {224, -0.6},
        {200, 0},
        {129, -0.1},
        {-95, 0.3},
        {-70, 0},
        {-53, 0},
        {-33, 0},
        {26, 0},
        {32, 0},
        {27, 0},
        {-24, 0},
        {16, 0},
        {13, 0},
        {-12, 0},
        {-10, 0},
        {-8, 0},
        {7, 0},
        {9, 0},
        {7, 0},
        {6, 0},
        {5, 0},
        {3, 0},
        {-3, 0},
        {3, 0},
        {3, 0},
        {-3, 0},
        {-3, 0},
        {3, 0},
        {3, 0},
        {3, 0},
        {3, 0},
        {3, 0},
    };

    protected static final int[] NON_ZERO_COEFFICIENT_ORD_TO_PARENT_INDEX = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        11, 12,
        14, 15, 16, 17,
        19, 20, 21,
        23,
        26,
        28, 29, 30,
        31, 32,
        34, 35, 36,
        38, 39,
        41, 42, 43, 44,
        46, 47, 48
    };

    @Override
    protected int getSeriesLength() {
        return NON_ZERO_COEFFICIENTS.length;
    }

    @Override
    protected int[] getElementMultiplierRow(int n) {
        return ELEMENTS_MULTIPLIERS[NON_ZERO_COEFFICIENT_ORD_TO_PARENT_INDEX[n]];
    }

    @Override
    protected double[] getCoefficientRow(int n) {
        return NON_ZERO_COEFFICIENTS[n];
    }

    @Override
    protected double applyTrigonometricFunction(double argument) {
        return Math.cos(argument);
    }
}
