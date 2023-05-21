package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcDayValues.*;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;

class DiurnalPhaseCalcCoordsCombiner {

    private final static Map<Integer, DiscontinuityMitigator> DISCONTINUITY_MITIGATORS = new HashMap<Integer, DiscontinuityMitigator>() {{
        put(COORD_NOON_RIGHT_ASCENSION, new DiscontinuityMitigator(0.0, Calcs.TURN, Calcs.Monotony.ASCENDING, Calcs.Angle::toNormalLongitude));
    }};

    private final DiurnalPhaseCalcCore core;
    private final SiderealMeanTimeCalculator siderealMeanTimeCalc = new SiderealMeanTimeCalculator();
    private final SiderealApparentTimeCalculator sideralTimeCalc = new SiderealApparentTimeCalculator();

    DiurnalPhaseCalcCoordsCombiner(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    double combineCentralLocalHourAngle(double vectorFromCenter) {
        TimelinePoint tx = core.getDay(0).noon.add(vectorFromCenter);
        double siderealTimeDeg = sideralTimeCalc.calculate(
            siderealMeanTimeCalc.calculate(tx),
            interpolateCentralCoord(COORD_NOON_NUTUATION_IN_LONGITUDE, vectorFromCenter),
            interpolateCentralCoord(COORD_NOON_ECLIPTIC_OBLIQUITY, vectorFromCenter)
        );
        double hourAngle0 = Transformations.calculateHourAngle(
            Math.toRadians(siderealTimeDeg),
            interpolateCentralCoord(COORD_NOON_RIGHT_ASCENSION, vectorFromCenter)
        );

        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - core.getRequest().longitude);
    }

    double combineCentralAltitude(double vectorFromCenter) {
        double localHourAngle = combineCentralLocalHourAngle(vectorFromCenter);
        double declination = interpolateCentralCoord(COORD_NOON_DECLINATION, vectorFromCenter);
        double latitude = core.getRequest().latitude;

        return Math.asin(
            Math.sin(latitude) * Math.sin(declination)
                + Math.cos(latitude) * Math.cos(declination) * Math.cos(localHourAngle)
        );
    }

    double interpolateCentralCoord(int coordKey, double vectorFromCenter) {
        double[] values = {
            core.getDay(-2).getCoord(coordKey),
            core.getDay(-1).getCoord(coordKey),
            core.getDay(0).getCoord(coordKey),
            core.getDay(+1).getCoord(coordKey),
            core.getDay(+2).getCoord(coordKey),
        };
        if (!DISCONTINUITY_MITIGATORS.containsKey(coordKey))
            return TabularInterpolation.interpolateFromFiveValuesAndFactor(values, vectorFromCenter);

        DiscontinuityMitigator mitigator = DISCONTINUITY_MITIGATORS.get(coordKey);
        mitigator.forceContinuityForInterpolation(values);
        return mitigator.normalization.applyAsDouble(
            TabularInterpolation.interpolateFromFiveValuesAndFactor(values, vectorFromCenter)
        );
    }

    double combineCloseLocalHourAngle(int dayPosition, double vectorFromNoon) {
        TimelinePoint tx = core.getDay(dayPosition).noon.add(vectorFromNoon);
        double siderealTimeDeg = sideralTimeCalc.calculate(
            siderealMeanTimeCalc.calculate(tx),
            interpolateCloseCoord(dayPosition, COORD_NOON_NUTUATION_IN_LONGITUDE, vectorFromNoon),
            interpolateCloseCoord(dayPosition, COORD_NOON_ECLIPTIC_OBLIQUITY, vectorFromNoon)
        );
        double hourAngle0 = Transformations.calculateHourAngle(
            Math.toRadians(siderealTimeDeg),
            interpolateCloseCoord(dayPosition, COORD_NOON_RIGHT_ASCENSION, vectorFromNoon)
        );

        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - core.getRequest().longitude);
    }

    double interpolateCloseCoord(int dayPosition, int coordKey, double vectorFromNoon) {
        if (dayPosition > 1 || dayPosition < -1)
            throw new IllegalArgumentException();

        double[] values = {
            core.getDay(dayPosition - 1).getCoord(coordKey),
            core.getDay(dayPosition).getCoord(coordKey),
            core.getDay(dayPosition + 1).getCoord(coordKey)
        };
        if (!DISCONTINUITY_MITIGATORS.containsKey(coordKey))
            return TabularInterpolation.interpolateFromThreeValuesAndFactor(values, vectorFromNoon);

        DiscontinuityMitigator mitigator = DISCONTINUITY_MITIGATORS.get(coordKey);
        mitigator.forceContinuityForInterpolation(values);
        return mitigator.normalization.applyAsDouble(
            TabularInterpolation.interpolateFromThreeValuesAndFactor(values, vectorFromNoon)
        );
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
