package com.github.sigrarr.lunisolarcalc.space.periodicterms;

import com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public abstract class EarthNutuationPeriodicTerms {

    protected final static double SCALE_ARCSECONDS = 0.0001;

    public double evaluate(double centurialT, EarthNutuationElements elements) {
        double value = 0.0;
        int seriesLength = getSeriesLength();
        for (int n = 0; n < seriesLength; n++) {
            value += evaluateTermRaw(centurialT, elements, n);
        }
        return scale(value);
    }

    public double evaluateTerm(double centurialT, EarthNutuationElements elements, int n) {
        return evaluateTerm(centurialT, elements, getCoefficientRow(n), getElementMultiplierRow(n));
    }

    public double evaluateTerm(double centurialT, EarthNutuationElements elements, double[] coefficientRow, short[] elementsMultipliers) {
        return scale(evaluateTermRaw(centurialT, elements, coefficientRow, elementsMultipliers));
    }

    protected double evaluateTermRaw(double centurialT, EarthNutuationElements elements, int n) {
        return evaluateTermRaw(centurialT, elements, getCoefficientRow(n), getElementMultiplierRow(n));
    }

    protected double evaluateTermRaw(double centurialT, EarthNutuationElements elements, double[] coefficientRow, short[] elementsMultipliers) {
        double argument = 0.0;
        for (int dim = 0; dim < EarthNutuationElements.ELEMENTS_N; dim++) {
            argument += elements.getValue(dim) * elementsMultipliers[dim];
        }
        return (coefficientRow[0] + (coefficientRow[1] * centurialT)) * applyTrigonometricFunction(argument);
    }

    protected double scale(double rawValue) {
        return Math.toRadians(Calcs.arcsecondsToDegrees(rawValue * SCALE_ARCSECONDS));
    }

    abstract protected int getSeriesLength();
    abstract protected short[] getElementMultiplierRow(int n);
    abstract protected double[] getCoefficientRow(int n);
    abstract protected double applyTrigonometricFunction(double argument);
}
