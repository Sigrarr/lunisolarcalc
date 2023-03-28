package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.OptionalDouble;

import com.github.sigrarr.lunisolarcalc.util.*;

abstract class DiurnalPhaseCalcExtremeFinder {

    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseCalcExtremeFinder(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    OptionalDouble findNoonToExtremePhaseVector(DiurnalPhase phase, double transitToPhaseApproximateVector) {
        double vector = core.getCloseNoonToTransitVector(0) + transitToPhaseApproximateVector;
        double diff = calculateDiffOverStandardAltitude(vector);
        double interval = 0.5 / 24.0;

        while (Double.compare(Math.abs(diff), core.getRequest().precisionAngle) > 0) {
            OptionalDouble correctionInIntervalScale = TabularInterpolation.interpolateZeroPointFactorFromThreePoints(
                new double[] {-interval, 0.0, +interval},
                new double [] {
                    calculateDiffOverStandardAltitude(vector - interval),
                    diff,
                    calculateDiffOverStandardAltitude(vector + interval)
                }
            );
            if (!correctionInIntervalScale.isPresent())
                return OptionalDouble.empty();

            vector += correctionInIntervalScale.getAsDouble() * interval;
            diff = calculateDiffOverStandardAltitude(vector);
            interval *= 0.5;
        }

        return OptionalDouble.of(vector);
    }

    private double calculateDiffOverStandardAltitude(double vector) {
        double altitude = core.coordsCombiner.combineCentralAltitude(vector);
        double standardAltitude = resolveCentralStandardAltitude(vector);
        return altitude - standardAltitude;
    }

    abstract protected double resolveCentralStandardAltitude(double vectorFromCenter);

}
