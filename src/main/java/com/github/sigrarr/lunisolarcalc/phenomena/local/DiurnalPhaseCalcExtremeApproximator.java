package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcDayValues.*;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.*;

class DiurnalPhaseCalcExtremeApproximator {

    private final DiurnalPhaseCalcCore core;

    DiurnalPhaseCalcExtremeApproximator(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    OptionalDouble approximateVectorFromTransitToExtremePhase(int direction) {
        double centerTransitXlhaCos = getTransitExtremeLocalHourAngleCos(0);
        double adjacentTransitXlhaCos = getTransitExtremeLocalHourAngleCos(direction);
        boolean isCenterTransitPolar = Double.compare(Math.abs(centerTransitXlhaCos), 1.0) > 0;
        boolean isAdjacentTransitPolar = Double.compare(Math.abs(adjacentTransitXlhaCos), 1.0) > 0;

        if (!isCenterTransitPolar && !isAdjacentTransitPolar)
            return OptionalDouble.of(approximateNormalTransitToExtremePhaseVector(direction, centerTransitXlhaCos));

        if (isCenterTransitPolar && isAdjacentTransitPolar)
            return OptionalDouble.empty();

        boolean polarNightBoundary = isCenterTransitPolar ? centerTransitXlhaCos > 0 : adjacentTransitXlhaCos > 0;
        if (polarNightBoundary)
            return isCenterTransitPolar ? OptionalDouble.empty()
                : OptionalDouble.of(approximateNormalTransitToExtremePhaseVector(direction, centerTransitXlhaCos));

        return approximateVectorFromTransitToExtremePhaseAtPolarDayBoundary(direction, isCenterTransitPolar);
    }

    private OptionalDouble approximateVectorFromTransitToExtremePhaseAtPolarDayBoundary(int direction, boolean isCenterTransitPolar) {
        double noonToTransit = core.getCloseNoonToTransitVector(0);
        int polarPosition = isCenterTransitPolar ? 0 : direction;
        double polarNoonToThresholdVector = interpolaterNoonToPolarThresholdVector(polarPosition);
        double polarTransitToThresholdVector = polarNoonToThresholdVector - core.getCloseNoonToTransitVector(polarPosition);

        if (Double.compare(Math.abs(polarTransitToThresholdVector), 0.5) > 0)
            return OptionalDouble.empty();

        double noonToThresholdVector = isCenterTransitPolar ? polarNoonToThresholdVector : polarNoonToThresholdVector + direction;
        double noonToMidpointVector = (noonToTransit + core.getCloseNoonToTransitVector(direction) + direction) / 2.0;
        double interval = noonToMidpointVector - noonToThresholdVector;
        double noonToThresholdReflectionVector = noonToMidpointVector + interval;

        TreeMap<Double, Double> vectorToZeroSearchFunction = new TreeMap<>();
        vectorToZeroSearchFunction.put(noonToThresholdVector, noonToThresholdVector - noonToTransit - direction * 0.5);
        vectorToZeroSearchFunction.put(noonToMidpointVector, noonToMidpointVector - noonToTransit - direction * (
            Math.acos(interpolateCentralExtremeLocalHourAngleCos(noonToMidpointVector)) / Calcs.TURN
        ));
        vectorToZeroSearchFunction.put(noonToThresholdReflectionVector, noonToThresholdReflectionVector - noonToTransit - direction * (
            Math.acos(interpolateCentralExtremeLocalHourAngleCos(noonToThresholdReflectionVector)) / Calcs.TURN
        ));

        OptionalDouble midToPhaseInIntervalScale = TabularInterpolation.interpolateZeroPointFactorFromThreePoints(
            vectorToZeroSearchFunction.keySet().stream().mapToDouble(Double::doubleValue).toArray(),
            vectorToZeroSearchFunction.values().stream().mapToDouble(Double::doubleValue).toArray()
        );
        if (!midToPhaseInIntervalScale.isPresent())
            return OptionalDouble.empty();

        double noonToPhase = (midToPhaseInIntervalScale.getAsDouble() * Math.abs(interval)) + noonToMidpointVector;
        return OptionalDouble.of(noonToPhase - noonToTransit);
    }

    private double calculateNoonExtremeLocalHourAngleCos(int dayPosition) {
        double standardAltitude = core.getNoonStandardAltitude(dayPosition);
        double declination = core.getDay(dayPosition).getCoord(COORD_NOON_DECLINATION);
        double latitude = core.getRequest().latitude;
        return (Math.sin(standardAltitude) - Math.sin(latitude) * Math.sin(declination))
            / (Math.cos(latitude) * Math.cos(declination));
    }

    private double approximateNormalTransitToExtremePhaseVector(int direction, double extremeLocalHourAngleCos) {
        return direction * Math.acos(extremeLocalHourAngleCos) / Calcs.TURN;
    }

    private double interpolateTransitExtremeLocalHourAngleCos(int dayPosition) {
        return TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                getNoonExtremeLocalHourAngleCos(dayPosition - 1),
                getNoonExtremeLocalHourAngleCos(dayPosition),
                getNoonExtremeLocalHourAngleCos(dayPosition + 1)
            },
            core.getCloseNoonToTransitVector(dayPosition)
        );
    }

    private double interpolaterNoonToPolarThresholdVector(int dayPosition) {
        return TabularInterpolation.interpolateZeroPointFactorFromThreePoints(
            new double[] {-1.0, 0.0, +1.0},
            new double[] {
                getNoonExtremeLocalHourAngleCos(dayPosition - 1) - (-1.0),
                getNoonExtremeLocalHourAngleCos(dayPosition) - (-1.0),
                getNoonExtremeLocalHourAngleCos(dayPosition + 1) - (-1.0),
            }
        ).getAsDouble();
    }

    private double interpolateCentralExtremeLocalHourAngleCos(double vectorFromCenter) {
        return TabularInterpolation.interpolateFromFiveValuesAndFactor(
            new double[] {
                getNoonExtremeLocalHourAngleCos(-2),
                getNoonExtremeLocalHourAngleCos(-1),
                getNoonExtremeLocalHourAngleCos(0),
                getNoonExtremeLocalHourAngleCos(+1),
                getNoonExtremeLocalHourAngleCos(+2),
            },
            vectorFromCenter
        );
    }

    private double getNoonExtremeLocalHourAngleCos(int dayPosition) {
        return core.getDay(dayPosition).get(NOON_EXTREME_LOCAL_HOUR_ANGLE_COS, () -> calculateNoonExtremeLocalHourAngleCos(dayPosition));
    }

    private double getTransitExtremeLocalHourAngleCos(int dayPosition) {
        return core.getDay(dayPosition).get(TRANSIT_EXTREME_LOCAL_HOUR_ANGLE_COS, () -> interpolateTransitExtremeLocalHourAngleCos(dayPosition));
    }
}
