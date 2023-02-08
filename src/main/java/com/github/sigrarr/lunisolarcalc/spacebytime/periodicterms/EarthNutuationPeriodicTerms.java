package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import com.github.sigrarr.lunisolarcalc.spacebytime.EarthNutuationElements;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Periodic terms for the Earth's nutuation.
 *
 * @see "Meeus 1998: Ch. 22, pp. 143-146"
 */
public abstract class EarthNutuationPeriodicTerms {

    protected final static double SCALE_ARCSECONDS = 0.0001;
    // Table 22.A : Argument \ multiple of D M M' F Ω
    protected final static int[][] ELEMENTS_MULTIPLIERS = {
        { 0 , 0 , 0 , 0 , 1 },
        { -2 , 0 , 0 , 2 , 2 },
        { 0 , 0 , 0 , 2 , 2 },
        { 0 , 0 , 0 , 0 , 2 },
        { 0 , 1 , 0 , 0 , 0 },
        { 0 , 0 , 1 , 0 , 0 },
        { -2 , 1 , 0 , 2 , 2 },
        { 0 , 0 , 0 , 2 , 1 },
        { 0 , 0 , 1 , 2 , 2 },
        { -2 , -1 , 0 , 2 , 2 },
        { -2 , 0 , 1 , 0 , 0 },
        { -2 , 0 , 0 , 2 , 1 },
        { 0 , 0 , -1 , 2 , 2 },
        { 2 , 0 , 0 , 0 , 0 },
        { 0 , 0 , 1 , 0 , 1 },
        { 2 , 0 , -1 , 2 , 2 },
        { 0 , 0 , -1 , 0 , 1 },
        { 0 , 0 , 1 , 2 , 1 },
        { -2 , 0 , 2 , 0 , 0 },
        { 0 , 0 , -2 , 2 , 1 },
        { 2 , 0 , 0 , 2 , 2 },
        { 0 , 0 , 2 , 2 , 2 },
        { 0 , 0 , 2 , 0 , 0 },
        { -2 , 0 , 1 , 2 , 2 },
        { 0 , 0 , 0 , 2 , 0 },
        { -2 , 0 , 0 , 2 , 0 },
        { 0 , 0 , -1 , 2 , 1 },
        { 0 , 2 , 0 , 0 , 0 },
        { 2 , 0 , -1 , 0 , 1 },
        { -2 , 2 , 0 , 2 , 2 },
        { 0 , 1 , 0 , 0 , 1 },
        { -2 , 0 , 1 , 0 , 1 },
        { 0 , -1 , 0 , 0 , 1 },
        { 0 , 0 , 2 , -2 , 0 },
        { 2 , 0 , -1 , 2 , 1 },
        { 2 , 0 , 1 , 2 , 2 },
        { 0 , 1 , 0 , 2 , 2 },
        { -2 , 1 , 1 , 0 , 0 },
        { 0 , -1 , 0 , 2 , 2 },
        { 2 , 0 , 0 , 2 , 1 },
        { 2 , 0 , 1 , 0 , 0 },
        { -2 , 0 , 2 , 2 , 2 },
        { -2 , 0 , 1 , 2 , 1 },
        { 2 , 0 , -2 , 0 , 1 },
        { 2 , 0 , 0 , 0 , 1 },
        { 0, -1 , 1 , 0 , 0 },
        { -2 , -1 , 0 , 2 , 1 },
        { -2 , 0 , 0 , 0 , 1 },
        { 0 , 0 , 2 , 2 , 1 },
        { -2 , 0 , 2 , 0 , 1 },
        { -2 , 1 , 0 , 2 , 1 },
        { 0 , 0 , 1 , -2 , 0 },
        { -1 , 0  , 1 , 0 , 0 },
        { -2 , 1 , 0 , 0 , 0 },
        { 1 , 0 , 0 , 0 , 0 },
        { 0 , 0 , 1 , 2 , 0 },
        { 0 , 0 , -2 , 2 , 2 },
        { -1 , -1 , 1 , 0 , 0 },
        { 0 , 1 , 1 , 0 , 0 },
        { 0 , -1 , 1 , 2 , 2 },
        { 2 , -1 , -1 , 2 , 2 },
        { 0 , 0 , 3 , 2 , 2 },
        { 2 , -1 , 0 , 2 , 2 },
    };

    public double evaluate(TimelinePoint tx, EarthNutuationElements elements) {
        double centurialT = tx.toCenturialT();
        double value = 0.0;
        int seriesLength = getSeriesLength();
        for (int n = 0; n < seriesLength; n++) {
            value += evaluateTermRaw(centurialT, elements, n);
        }
        return scale(value);
    }

    protected double evaluateTerm(TimelinePoint tx, EarthNutuationElements elements, int n) {
        return evaluateTerm(tx, elements, getCoefficientRow(n), getElementMultiplierRow(n));
    }

    protected double evaluateTerm(TimelinePoint tx, EarthNutuationElements elements, double[] coefficientRow, int[] elementsMultipliers) {
        return scale(evaluateTermRaw(tx.toCenturialT(), elements, coefficientRow, elementsMultipliers));
    }

    protected double evaluateTermRaw(double centurialT, EarthNutuationElements elements, int n) {
        return evaluateTermRaw(centurialT, elements, getCoefficientRow(n), getElementMultiplierRow(n));
    }

    protected double evaluateTermRaw(double centurialT, EarthNutuationElements elements, double[] coefficientRow, int[] elementsMultipliers) {
        double argument = 0.0;
        for (int dim = 0; dim < EarthNutuationElements.ELEMENTS_N; dim++) {
            argument += elements.getValue(dim) * elementsMultipliers[dim];
        }
        return (coefficientRow[0] + (coefficientRow[1] * centurialT)) * applyTrigonometricFunction(argument);
    }

    protected double scale(double rawValue) {
        return Math.toRadians(Calcs.Angle.arcsecondsToDegrees(rawValue * SCALE_ARCSECONDS));
    }

    protected int getSeriesLength() {
        return ELEMENTS_MULTIPLIERS.length;
    }

    protected int[] getElementMultiplierRow(int n) {
        return ELEMENTS_MULTIPLIERS[n];
    }

    abstract protected double[] getCoefficientRow(int n);
    abstract protected double applyTrigonometricFunction(double argument);
}
