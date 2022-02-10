package com.github.sigrarr.lunisolarcalc.space.periodicterms;

import com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

abstract public class PeriodicTermsForNutuation {
    /**
     * Meeus 1998, Table 22.A, pp. 145-146
     */
    protected final static short[][] ELEMENTS_VECTOR_MULTIPLIERS = {
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
        { 0 , 0 , 1 , 2 , 2 },
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
    protected final static double UNIT_ARCSECONDS = 0.0001;

    protected int limit = ELEMENTS_VECTOR_MULTIPLIERS.length;

    public int getSize() {
        return limit;
    }

    public void setSize(int limit) {
        if (limit < 0 || limit > ELEMENTS_VECTOR_MULTIPLIERS.length) {
            throw new IllegalArgumentException();
        }
        this.limit = limit;
    }

    public double evaluateSeries(double centurialT, EarthNutuationElementsVector elements) {
        double value = 0.0;
        for (int n = 0; n < limit; n++) {
            value += evaluateTermRaw(centurialT, elements, n);
        }
        return scale(value);
    }

    public double evaluateTerm(double centurialT, EarthNutuationElementsVector elements, int n) {
        return evaluateTerm(centurialT, elements, getCoefficientRow(n), ELEMENTS_VECTOR_MULTIPLIERS[n]);
    }

    public double evaluateTerm(double centurialT, EarthNutuationElementsVector elements, double[] coefficientRow, short[] argumentMultipliers) {
        return scale(evaluateTermRaw(centurialT, elements, coefficientRow, argumentMultipliers));
    }

    protected double evaluateTermRaw(double centurialT, EarthNutuationElementsVector elements, int n) {
        return evaluateTermRaw(centurialT, elements, getCoefficientRow(n), ELEMENTS_VECTOR_MULTIPLIERS[n]);
    }

    protected double evaluateTermRaw(double centurialT, EarthNutuationElementsVector elements, double[] coefficientRow, short[] argumentMultipliers) {
        double argument = 0.0;
        for (int dim = 0; dim < EarthNutuationElementsVector.DIMENSIONS_N; dim++) {
            argument += elements.getValue(dim) * argumentMultipliers[dim];
        }
        return (coefficientRow[0] + (coefficientRow[1] * centurialT)) * applyTrigonometricFunction(argument);
    }

    protected double scale(double rawValue) {
        return Math.toRadians(Calcs.arcsecondsToDegrees(rawValue * UNIT_ARCSECONDS));
    }

    abstract protected double[] getCoefficientRow(int n);
    abstract protected double applyTrigonometricFunction(double argument);
}
