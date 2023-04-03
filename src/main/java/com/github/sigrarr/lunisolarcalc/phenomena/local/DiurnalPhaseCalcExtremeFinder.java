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
        double diff = combineDiffOverStandardAltitude(vector);
        double interval = 0.5 / 24.0;
        DoubleStrictPairBuffer correction = new DoubleStrictPairBuffer(interval);

        while (Double.compare(Math.abs(diff), core.getRequest().precisionAngle) > 0) {
            OptionalDouble correctionInIntervalScale = TabularInterpolation.interpolateZeroPointFactorFromThreePoints(
                new double[] {-interval, 0.0, +interval},
                new double [] {
                    combineDiffOverStandardAltitude(vector - interval),
                    diff,
                    combineDiffOverStandardAltitude(vector + interval)
                }
            );
            if (!correctionInIntervalScale.isPresent())
                return OptionalDouble.empty();

            correction.push(correctionInIntervalScale.getAsDouble() * interval);
            if (Double.compare(Math.abs(correction.getCurrent()), Math.abs(correction.getPrevious())) >= 0)
                break;

            vector += correction.getCurrent();
            diff = combineDiffOverStandardAltitude(vector);
            interval *= 0.5;
        }

        return OptionalDouble.of(vector);
    }

    private double combineDiffOverStandardAltitude(double vector) {
        double altitude = core.coordsCombiner.combineCentralAltitude(vector);
        double standardAltitude = resolveCentralStandardAltitude(vector);
        return altitude - standardAltitude;
    }

    abstract protected double resolveCentralStandardAltitude(double vectorFromCenter);

}
