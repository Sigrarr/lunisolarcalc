package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.OptionalDouble;

import com.github.sigrarr.lunisolarcalc.util.*;

abstract class DiurnalPhaseCalcExtremeFinder {

    private static final double INITIAL_INTERVAL_DAY_FRACTION = 0.5 / 24.0;
    private static final double MIN_INTERVAL_DAY_FRACTION = 0.5 * Calcs.SECOND_TO_DAY;
    protected final DiurnalPhaseCalcCore core;
    private double[] interpolationX = {0.0, 0.0, 0.0};
    private double[] interpolationY = new double[3];

    DiurnalPhaseCalcExtremeFinder(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    OptionalDouble findNoonToExtremePhaseVector(DiurnalPhase phase, double transitToPhaseApproximateVector) {
        double vector = core.getCloseNoonToTransitVector(0) + transitToPhaseApproximateVector;
        double excess = combineExcessOverStandardAltitude(vector);
        double interval = INITIAL_INTERVAL_DAY_FRACTION;

        while (
            Double.compare(Math.abs(excess), core.getRequest().precisionRadians) > 0
            && Double.compare(interval, MIN_INTERVAL_DAY_FRACTION) >= 0
        ) {
            combineSurroundingExcessValuesAndUpdateInterpolationPoints(vector, excess, interval);
            OptionalDouble correctionInIntervalScale = TabularInterpolation.interpolateZeroPointFactorFromThreePoints(interpolationX, interpolationY);
            if (!correctionInIntervalScale.isPresent())
                return OptionalDouble.empty();

            double correction = correctionInIntervalScale.getAsDouble() * interval;
            vector += correction;
            excess = combineExcessOverStandardAltitude(vector);
            interval *= 0.25;
        }

        return OptionalDouble.of(vector);
    }

    private void combineSurroundingExcessValuesAndUpdateInterpolationPoints(double vector, double excess, double interval) {
        interpolationX[0] = -interval;
        interpolationX[2] = interval;
        interpolationY[0] = combineExcessOverStandardAltitude(vector - interval);
        interpolationY[1] = excess;
        interpolationY[2] = combineExcessOverStandardAltitude(vector + interval);
    }

    private double combineExcessOverStandardAltitude(double vector) {
        double altitude = core.coordsCombiner.combineCentralAltitude(vector);
        double standardAltitude = resolveCentralStandardAltitude(vector);
        return altitude - standardAltitude;
    }

    abstract protected double resolveCentralStandardAltitude(double vectorFromCenter);

}
