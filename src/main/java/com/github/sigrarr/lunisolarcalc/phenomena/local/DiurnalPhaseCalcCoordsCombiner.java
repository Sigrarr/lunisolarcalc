package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcDayValues.*;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;

class DiurnalPhaseCalcCoordsCombiner {

    private final static Map<Integer, DiscontinuityMitigator> DISCONTINUITY_MITIGATORS = new HashMap<Integer, DiscontinuityMitigator>() {{
        put(COORD_RIGHT_ASCENSION, new DiscontinuityMitigator(0.0, Calcs.TURN, Calcs.Monotony.ASCENDING, Calcs.Angle::toNormalLongitude));
    }};

    protected final DiurnalPhaseCalcCore core;
    private final SiderealMeanTimeCalculator siderealMeanTimeCalc = new SiderealMeanTimeCalculator();
    private final SiderealApparentTimeCalculator sideralTimeCalc = new SiderealApparentTimeCalculator();
    private final Interpolator centralInterpolator = new Interpolator(5);
    private final Interpolator closeInterpolator = new Interpolator(3);

    DiurnalPhaseCalcCoordsCombiner(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    double combineCloseLocalHourAngle(int dayPosition, double vectorFromNoon) {
        return combineLocalHourAngle(dayPosition, vectorFromNoon, closeInterpolator);
    }

    double combineCentralLocalHourAngle(double vectorFromCenter) {
        return combineLocalHourAngle(0, vectorFromCenter, centralInterpolator);
    }

    double combineCentralAltitude(double vectorFromCenter) {
        double localHourAngle = combineLocalHourAngle(0, vectorFromCenter, centralInterpolator);
        double declination = centralInterpolator.interpolate(0, COORD_DECLINATION, vectorFromCenter);
        double latitude = core.getRequest().latitude;

        return Math.asin(
            Math.sin(latitude) * Math.sin(declination)
                + Math.cos(latitude) * Math.cos(declination) * Math.cos(localHourAngle)
        );
    }

    double interpolateCloseCoord(int dayPosition, int coordKey, double vectorFromNoon) {
        return closeInterpolator.interpolate(dayPosition, coordKey, vectorFromNoon);
    }

    double interpolateCentralCoord(int coordKey, double vectorFromCenter) {
        return centralInterpolator.interpolate(0, coordKey, vectorFromCenter);
    }

    private double combineLocalHourAngle(int dayPosition, double vector, Interpolator interpolator) {
        TimelinePoint tx = core.getDay(dayPosition).noon.add(vector);
        double siderealTimeDeg = sideralTimeCalc.calculate(
            siderealMeanTimeCalc.calculate(tx),
            interpolator.interpolate(dayPosition, COORD_NUTUATION_IN_LONGITUDE, vector),
            interpolator.interpolate(dayPosition, COORD_ECLIPTIC_OBLIQUITY, vector)
        );
        double hourAngle0 = Transformations.calculateHourAngle(
            Math.toRadians(siderealTimeDeg),
            interpolator.interpolate(dayPosition, COORD_RIGHT_ASCENSION, vector)
        );

        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - core.getRequest().longitude);
    }

    private class Interpolator {
        final InterpolatingFunction function;
        final double[] values;
        final int radius;

        Interpolator(int pointsNumber) {
            if (pointsNumber == 5)
                function = TabularInterpolation::interpolateFromFiveValuesAndFactor;
            else if (pointsNumber == 3)
                function = TabularInterpolation::interpolateFromThreeValuesAndFactor;
            else throw new IllegalArgumentException();
            values = new double[pointsNumber];
            radius = pointsNumber / 2;
        }

        double interpolate(int dayPosition, int coordKey, double vector) {
            for (int r = -radius, v = 0; r <= radius; r++, v++)
                values[v] = core.getDay(dayPosition + r).getNoonCoord(coordKey);

            if (!DISCONTINUITY_MITIGATORS.containsKey(coordKey))
                return function.apply(values, vector);

            DiscontinuityMitigator mitigator = DISCONTINUITY_MITIGATORS.get(coordKey);
            mitigator.forceContinuityForInterpolation(values);
            return mitigator.normalization.applyAsDouble(function.apply(values, vector));
        }
    }

    @FunctionalInterface
    private static interface InterpolatingFunction {
        public double apply(double[] values, double factor);
    }

    private static class DiscontinuityMitigator {
        final Calcs.Monotony monotony;
        final DoubleUnaryOperator normalization;
        final double breakSize;
        final double projectionCheckExtremeValue;

        DiscontinuityMitigator(double minValue, double maxValue, Calcs.Monotony monotony, DoubleUnaryOperator normalization) {
            this.monotony = monotony;
            this.normalization = normalization;
            breakSize = maxValue - minValue;
            projectionCheckExtremeValue = monotony == Calcs.Monotony.ASCENDING ? minValue : maxValue;
        }

        void forceContinuityForInterpolation(double[] values) {
            for (int i = 1; i < values.length; i++) {
                double currentValueDistanceToPrevious = Math.abs(values[i] - values[i-1]);
                double currentValueDistanceToExtreme = Math.abs(values[i] - projectionCheckExtremeValue);
                if (Double.compare(currentValueDistanceToExtreme, currentValueDistanceToPrevious) < 0) {
                    for (int j = i; j < values.length; j++)
                        values[j] += monotony.progressSignum * breakSize;
                    return;
                }
            }
        }
    }
}
