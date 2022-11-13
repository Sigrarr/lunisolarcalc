package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import com.github.sigrarr.lunisolarcalc.spacebytime.MoonCoordinateElements;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

public abstract class MoonCoordinatePeriodicTerms {

    protected final static int INDEX_MULTIPLIER_D = 0;
    protected final static int INDEX_MULTIPLIER_M = 1;
    protected final static int INDEX_MULTIPLIER_M_PRIM = 2;
    protected final static int INDEX_MULTIPLIER_F = 3;

    public double evaluate(TimelinePoint tx, MoonCoordinateElements elements) {
        double value = 0.0;
        int seriesLength = getSeriesLength();
        for (int n = 0; n < seriesLength; n++) {
            value += evaluateTermRaw(tx.toCenturialT(), elements, getCoefficient(n), getElementMultiplierRow(n));
        }
        return scale(value);
    }

    public double evaluateTerm(TimelinePoint tx, MoonCoordinateElements elements, int n) {
        return scale(evaluateTermRaw(tx.toCenturialT(), elements, getCoefficient(n), getElementMultiplierRow(n)));
    }

    protected double calculateEarthOrbitEccentricityElement(double centurialT) {
        return 1.0 - (0.002516 * centurialT) - (0.0000074 * centurialT * centurialT);
    }

    protected double calculateEarthOrbitEccentricityCoefficient(double eccentricityElement, short mMultiplier) {
        return (mMultiplier == 1 || mMultiplier == -1) ? eccentricityElement : eccentricityElement * eccentricityElement;
    }

    protected double evaluateTermRaw(double centurialT, MoonCoordinateElements elements, int coefficient, short[] elementsMultipliers) {
        double argument = (elements.getD() * elementsMultipliers[INDEX_MULTIPLIER_D])
            + (elements.getM() * elementsMultipliers[INDEX_MULTIPLIER_M])
            + (elements.getMPrim() * elementsMultipliers[INDEX_MULTIPLIER_M_PRIM])
            + (elements.getF() * elementsMultipliers[INDEX_MULTIPLIER_F]);
        double value = applyTrigonometricFunction(argument) * coefficient;

        short mMultiplier = elementsMultipliers[INDEX_MULTIPLIER_M];
        if (mMultiplier != 0) {
            value *= calculateEarthOrbitEccentricityCoefficient(calculateEarthOrbitEccentricityElement(centurialT), mMultiplier);
        }

        return value;
    }

    abstract protected double scale(double rawValue);
    abstract protected int getSeriesLength();
    abstract protected short[] getElementMultiplierRow(int n);
    abstract protected int getCoefficient(int n);
    abstract protected double applyTrigonometricFunction(double argument);
}
