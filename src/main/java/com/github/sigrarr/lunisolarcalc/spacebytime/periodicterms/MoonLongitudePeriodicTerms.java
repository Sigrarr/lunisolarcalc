package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import com.github.sigrarr.lunisolarcalc.spacebytime.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

/**
 * Periodic terms for longitude of the Moon's center (λ).
 * Results are in radians.
 *
 * @see com.github.sigrarr.lunisolarcalc.spacebytime.MoonLongitudeCalculator
 * @see "Meeus 1998: Table 47.A (pp. 339-340)"
 */
public final class MoonLongitudePeriodicTerms extends MoonCoordinatePeriodicTerms {

    protected static final double SCALE_DEGREES = 0.000001;

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
        { 0 , 0 , 1 , 2 },
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
        { 2 , 0 , -1 , 2 },
        { 2 , -1 , -2 , 0 },
        { 1 , 0 , 1 , 0 },
        { 2 , -2 , 0 , 0 },
        { 0 , 1 , 2 , 0 },
        { 0 , 2 , 0 , 0 },
        { 2 , -2 , -1 , 0 },
        { 2 , 0 , 1 , -2 },
        { 2 , 0 , 0 , 2 },
        { 4 , -1 , -1 , 0 },
        { 0 , 0 , 2 , 2 },
        { 3 , 0 , -1 , 0 },
        { 2 , 1 , 1 , 0 },
        { 4 , -1 , -2 , 0 },
        { 0 , 2 , -1 , 0 },
        { 2 , 2 , -1 , 0 },
        { 2 , 1 , -2 , 0 },
        { 2 , -1 , 0 , -2 },
        { 4 , 0 , 1 , 0 },
        { 0 , 0 , 4 , 0 },
        { 4 , -1 , 0 , 0 },
        { 1 , 0 , -2 , 0 },
        { 2 , 1 , 0 , -2 },
        { 0 , 0 , 2 , -2 },
        { 1 , 1 , 1 , 0 },
        { 3 , 0 , -2 , 0 },
        { 4 , 0 , -3 , 0 },
        { 2 , -1 , 2 , 0 },
        { 0 , 2 , 1 , 0 },
        { 1 , 1 , -1 , 0 },
        { 2 , 0 , 3 , 0 },
    };

    // : Σl \ Coefficient of the sine...
    protected static final int[] COEFFICIENTS = {
        6288774,
        1274027,
        658314,
        213618,
        -185116,
        -114332,
        58793,
        57066,
        53322,
        45758,
        -40923,
        -34720,
        -30383,
        15327,
        -12528,
        10980,
        10675,
        10034,
        8548,
        -7888,
        -6766,
        -5163,
        4987,
        4036,
        3994,
        3861,
        3665,
        -2689,
        -2602,
        2390,
        -2348,
        2236,
        -2120,
        -2069,
        2048,
        -1773,
        -1595,
        1215,
        -1110,
        -892,
        -810,
        759,
        -713,
        -700,
        691,
        596,
        549,
        537,
        520,
        -487,
        -399,
        -381,
        351,
        -340,
        330,
        327,
        -323,
        299,
        294,
    };

    @Override
    public double evaluate(TimelinePoint tx, MoonCoordinateElements elements) {
        return super.evaluate(tx, elements) + scale(
              (3958 * Math.sin(elements.getA1()))
            + (1962 * Math.sin(elements.getLPrim() - elements.getF()))
            + ( 318 * Math.sin(elements.getA2()))
        );
    }

    @Override
    protected double scale(double rawValue) {
        return Math.toRadians(rawValue * SCALE_DEGREES);
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
        return Math.sin(argument);
    }
}
