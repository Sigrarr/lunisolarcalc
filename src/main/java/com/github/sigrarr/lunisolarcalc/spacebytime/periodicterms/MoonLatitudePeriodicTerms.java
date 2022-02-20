package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import com.github.sigrarr.lunisolarcalc.spacebytime.MoonCoordinateElements;

/**
 * Meeus 1998, Table 47.B, p. 341
 */
public final class MoonLatitudePeriodicTerms extends MoonCoordinatePeriodicTerms {

    protected static final double SCALE_DEGREES = 0.000001;
    /**
     * : Argument \ Multiple of
     */
    protected static final short[][] ELEMENTS_MULTIPLIERS = {
        { 0 , 0 , 0 , 1 },
        { 0 , 0 , 1 , 1 },
        { 0 , 0 , 1 , -1 },
        { 2 , 0 , 0 , -1 },
        { 2 , 0 , -1 , 1 },
        { 2 , 0 , -1 , -1 },
        { 2 , 0 , 0 , 1 },
        { 0 , 0 , 2 , 1 },
        { 2 , 0 , 1 , -1 },
        { 0 , 0 , 2 , -1 },
        { 2 , -1 , 0 , -1 },
        { 2 , 0 , -2 , -1 },
        { 2 , 0 , 1 , 1 },
        { 2 , 1 , 0 , -1 },
        { 2 , -1 , -1 , 1 },
        { 2 , -1 , 0 , 1 },
        { 2 , -1 , -1 , -1 },
        { 0 , 1 , -1 , -1 },
        { 4 , 0 , -1 , -1 },
        { 0 , 1 , 0 , 1 },
        { 0 , 0 , 0 , 3 },
        { 0 , 1 , -1 , 1 },
        { 1 , 0 , 0 , 1 },
        { 0 , 1 , 1 , 1 },
        { 0 , 1 , 1 , -1 },
        { 0 , 1 , 0 , -1 },
        { 1 , 0 , 0 , -1 },
        { 0 , 0 , 3 , 1 },
        { 4 , 0 , 0 , -1 },
        { 4 , 0 , -1 , 1 },
        { 0 , 0 , 1 , -3 },
        { 4 , 0 , -2 , 1 },
        { 2 , 0 , 0 , -3 },
        { 2 , 0 , 2 , -1 },
        { 2 , -1 , 1 , -1 },
        { 2 , 0 , -2 , 1 },
        { 0 , 0 , 3 , -1 },
        { 2 , 0 , 2 , 1 },
        { 2 , 0 , -3 , -1 },
        { 2 , 1 , -1 , 1 },
        { 2 , 1 , 0 , 1 },
        { 4 , 0 , 0 , 1 },
        { 2 , -1 , 1 , 1 },
        { 2 , -2 , 0 , -1 },
        { 0 , 0 , 1 , 3 },
        { 2 , 1 , 1 , -1 },
        { 1 , 1 , 0 , -1 },
        { 1 , 1 , 0 , 1 },
        { 0 , 1 , -2 , -1 },
        { 2 , 1 , -1 , -1 },
        { 1 , 0 , 1 , 1 },
        { 2 , -1 , -2 , -1 },
        { 0 , 1 , 2 , 1 },
        { 4 , 0 , -2 , -1 },
        { 4 , -1 , -1 , -1 },
        { 1 , 0 , 1 , -1 },
        { 4 , 0 , 1 , -1 },
        { 1 , 0 , -1 , -1 },
        { 4 , -1 , 0 , -1 },
        { 2 , -2 , 0 , 1 },        
    };
    /**
     * : Sigma b \ Coefficient of the sine...
     */
    protected static final int[] COEFFICIENTS = {
        5128122,
        280602,
        277693,
        173237,
        55413,
        46271,
        32573,
        17198,
        9266,
        8822,
        8216,
        4324,
        4200,
        -3359,
        2463,
        2211,
        2065,
        -1870,
        1828,
        -1794,
        -1749,
        -1565,
        -1491,
        -1475,
        -1410,
        -1344,
        -1335,
        1107,
        1021,
        833,
        777,
        671,
        607,
        596,
        491,
        -451,
        439,
        422,
        421,
        -366,
        -351,
        331,
        315,
        302,
        -283,
        -229,
        223,
        223,
        -220,
        -220,
        -185,
        181,
        -177,
        176,
        166,
        -164,
        132,
        -119,
        115,
        107,
    };

    @Override
    public double evaluate(double centurialT, MoonCoordinateElements elements) {
        return super.evaluate(centurialT, elements) + scale(
            - (2235 * Math.sin(elements.getLPrim()))
            + ( 382 * Math.sin(elements.getA3()))
            + ( 175 * Math.sin(elements.getA1() - elements.getF()))
            + ( 175 * Math.sin(elements.getA1() + elements.getF()))
            + ( 127 * Math.sin(elements.getLPrim() - elements.getMPrim()))
            - ( 115 * Math.sin(elements.getLPrim() + elements.getMPrim()))
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
