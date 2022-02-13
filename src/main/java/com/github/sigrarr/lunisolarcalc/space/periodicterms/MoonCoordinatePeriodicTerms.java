package com.github.sigrarr.lunisolarcalc.space.periodicterms;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.*;

public abstract class MoonCoordinatePeriodicTerms {

    protected final static int INDEX_MULTIPLIER_D = 0;
    protected final static int INDEX_MULTIPLIER_M = 1;
    protected final static int INDEX_MULTIPLIER_M_PRIM = 2;
    protected final static int INDEX_MULTIPLIER_F = 3;

    /**
     * Meeus 1998, 47.6, p. 338
     */
    public static double calculateEarthOrbitEccentricityFactor(double centurialT) {
        return 1.0 - (0.002516 * centurialT) - (0.0000074 * centurialT * centurialT);
    }

    public double evaluate(double centurialT, MoonCoordinateElements elements) {
        double value = 0.0;
        int seriesLength = getSeriesLength();
        for (int n = 0; n < seriesLength; n++) {
            value += evaluateTermRaw(centurialT, elements, n);
        }
        return scale(value);
    }

    public double evaluateTerm(double centurialT, MoonCoordinateElements elements, int n) {
        return evaluateTerm(centurialT, elements, getCoefficient(n), getElementMultiplierRow(n));
    }

    public double evaluateTerm(double centurialT, MoonCoordinateElements elements, int coefficient, short[] elementsMultipliers) {
        return scale(evaluateTermRaw(centurialT, elements, coefficient, elementsMultipliers));
    }

    protected double evaluateTermRaw(double centurialT, MoonCoordinateElements elements, int n) {
        return evaluateTermRaw(centurialT, elements, getCoefficient(n), getElementMultiplierRow(n));
    }

    protected double evaluateTermRaw(double centurialT, MoonCoordinateElements elements, int coefficient, short[] elementsMultipliers) {
        double argument = (elementsMultipliers[INDEX_MULTIPLIER_D] * elements.getD())
            + (elementsMultipliers[INDEX_MULTIPLIER_M] * elements.getM())
            + (elementsMultipliers[INDEX_MULTIPLIER_M_PRIM] * elements.getMPrim())
            + (elementsMultipliers[INDEX_MULTIPLIER_F] * elements.getF());
        double value = applyTrigonometricFunction(Math.toRadians(argument)) * coefficient;

        int mMultiplier = elementsMultipliers[INDEX_MULTIPLIER_M];
        if (mMultiplier != 0) {
            double e = calculateEarthOrbitEccentricityFactor(centurialT);
            value *= (mMultiplier == 1 || mMultiplier == -1) ? e : e * e;
        }

        return value;
    }

    abstract protected double scale(double rawValue);
    abstract protected int getSeriesLength();
    abstract protected short[] getElementMultiplierRow(int n);
    abstract protected int getCoefficient(int n);
    abstract protected double applyTrigonometricFunction(double argument);
}
