package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.OptionalDouble;

import com.github.sigrarr.lunisolarcalc.util.*;

abstract class DiurnalPhaseCalcExtremeFinder {

    private static final double INITIAL_INTERVAL_HOURS = 0.5;
    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseCalcExtremeFinder(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    OptionalDouble findNoonToExtremePhaseVector(DiurnalPhase phase, double transitToPhaseApproximateVector) {
        double vector = core.getCloseNoonToTransitVector(0) + transitToPhaseApproximateVector;
        double excess = combineExcessOverStandardAltitude(vector);
        double interval = INITIAL_INTERVAL_HOURS / 24.0;
        DoubleStrictPairBuffer correction = new DoubleStrictPairBuffer(interval);

        while (Double.compare(Math.abs(excess), core.getRequest().precisionRadians) > 0) {
            OptionalDouble correctionInIntervalScale = TabularInterpolation.interpolateZeroPointFactorFromThreePoints(
                new double[] {-interval, 0.0, +interval},
                new double [] {
                    combineExcessOverStandardAltitude(vector - interval),
                    excess,
                    combineExcessOverStandardAltitude(vector + interval)
                }
            );
            if (!correctionInIntervalScale.isPresent())
                return OptionalDouble.empty();

            correction.push(correctionInIntervalScale.getAsDouble() * interval);
            if (Double.compare(Math.abs(correction.getCurrent()), Math.abs(correction.getPrevious())) >= 0)
                break;

            vector += correction.getCurrent();
            excess = combineExcessOverStandardAltitude(vector);
            interval *= 0.25;
        }

        return OptionalDouble.of(vector);
    }

    private double combineExcessOverStandardAltitude(double vector) {
        double altitude = core.coordsCombiner.combineCentralAltitude(vector);
        double standardAltitude = resolveCentralStandardAltitude(vector);
        return altitude - standardAltitude;
    }

    abstract protected double resolveCentralStandardAltitude(double vectorFromCenter);

}
