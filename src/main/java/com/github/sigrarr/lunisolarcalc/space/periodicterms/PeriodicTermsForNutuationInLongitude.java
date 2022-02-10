package com.github.sigrarr.lunisolarcalc.space.periodicterms;

public class PeriodicTermsForNutuationInLongitude extends PeriodicTermsForNutuation {
    /**
     * Meeus 1998, ch. 22, Table 22.A: Delta Psi \ Coefficient of the sine..., pp. 145-146
     */
    protected static final double[][] COEFFICIENTS = {
        { -171996.0 , -174.2 },
        { -13187.0 , -1.6 },
        { -2274.0 , -0.2 },
        { 2062.0 , 0.2 },
        { 1426.0 , -3.4 },
        { 712.0 , 0.1 },
        { -517.0 , 1.2 },
        { -386.0 , -0.4 },
        { -301.0 , 0.0 },
        { 217.0 , -0.5 },
        { -158.0 , 0.0 },
        { 129.0 , 0.1 },
        { 123.0 , 0.0 },
        { 63.0 , 0.0 },
        { 63.0 , 0.1 },
        { -59.0 , 0.0 },
        { -58.0 , -0.1 },
        { -51.0 , 0.0 },
        { 48.0 , 0.0 },
        { 46.0 , 0.0 },
        { -38.0 , 0.0 },
        { -31.0 , 0.0 },
        { 29.0 , 0.0 },
        { 29.0 , 0.0 },
        { 26.0 , 0.0 },
        { -22.0 , 0.0 },
        { 21.0 , 0.0 },
        { 17.0 , -0.1 },
        { 16.0 , 0.0 },
        { -16.0 , 0.1 },
        { -15.0 , 0.0 },
        { -13.0 , 0.0 },
        { -12.0 , 0.0 },
        { 11.0 , 0.0 },
        { -10.0 , 0.0 },
        { -8.0 , 0.0 },
        { 7.0 , 0.0 },
        { -7.0 , 0.0 },
        { -7.0 , 0.0 },
        { -7.0 , 0.0 },
        { 6.0 , 0.0 },
        { 6.0 , 0.0 },
        { 6.0 , 0.0 },
        { -6.0 , 0.0 },
        { -6.0 , 0.0 },
        { 5.0 , 0.0 },
        { -5.0 , 0.0 },
        { -5.0 , 0.0 },
        { -5.0 , 0.0 },
        { 4.0 , 0.0 },
        { 4.0 , 0.0 },
        { 4.0 , 0.0 },
        { -4.0 , 0.0 },
        { -4.0 , 0.0 },
        { -4.0 , 0.0 },
        { 3.0 , 0.0 },
        { -3.0 , 0.0 },
        { -3.0 , 0.0 },
        { -3.0 , 0.0 },
        { -3.0 , 0.0 },
        { -3.0 , 0.0 },
        { -3.0 , 0.0 },
        { -3.0 , 0.0 },
    };

    protected double[] getCoefficientRow(int n) {
        return COEFFICIENTS[n];
    }

    protected double applyTrigonometricFunction(double argument) {
        return Math.sin(argument);
    }
}
