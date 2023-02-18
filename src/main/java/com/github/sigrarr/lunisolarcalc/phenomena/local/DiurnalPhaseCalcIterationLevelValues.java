package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.TabularInterpolation;

class DiurnalPhaseCalcIterationLevelValues {

    private static final double M_TO_SIDEREAL_TIME_DEGREES_MULTIPLIER = 360.985647;

    private final DiurnalPhaseCalcCore core;

    private double siderealTime;
    private double interpolatingFactor;
    private double rightAscension;
    private double declination;
    private double localHourAngle;
    private double mCorrection;

    DiurnalPhaseCalcIterationLevelValues(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    double getLastMCorrection() {
        return mCorrection;
    }

    double calculateTransit(double m) {
        calculateSiderealTimeAndInterpolatingFactor(m);
        interpolateRightAscension();
        calculateLocalHourAngle();
        mCorrection = -localHourAngle/Calcs.TURN;
        return Calcs.Angle.toNormalLongitude(m + mCorrection, 1.0);
    }

    double calculateRise(double m) {
        calculateSiderealTimeAndInterpolatingFactor(m);
        interpolateRightAscension();
        interpolateDeclination();
        calculateLocalHourAngle();
        claculateMCorrectionForLiminalPhase();
        return Calcs.Angle.toNormalLongitude(m + mCorrection, 1.0);
    }

    double calculateSet(double m) {
        calculateSiderealTimeAndInterpolatingFactor(m);
        interpolateRightAscension();
        interpolateDeclination();
        calculateLocalHourAngle();
        claculateMCorrectionForLiminalPhase();
        return Calcs.Angle.toNormalLongitude(m + mCorrection, 1.0);
    }

    private void calculateSiderealTimeAndInterpolatingFactor(double m) {
        siderealTime = Math.toRadians(core.dateLevel.utSiderealTimeDegrees + M_TO_SIDEREAL_TIME_DEGREES_MULTIPLIER * m);
        interpolatingFactor = m + core.dateLevel.deltaTDays;
    }

    private void interpolateRightAscension() {
        rightAscension = TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                (Double) core.getBackEquatorialCoords().get(core.body.rightAscensionSubject),
                (Double) core.getCenterEquatorialCoords().get(core.body.rightAscensionSubject),
                (Double) core.getFrontEquatorialCoords().get(core.body.rightAscensionSubject),
            },
            interpolatingFactor
        );
    }

    private void interpolateDeclination() {
        declination = TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                (Double) core.getBackEquatorialCoords().get(core.body.declinationSubject),
                (Double) core.getCenterEquatorialCoords().get(core.body.declinationSubject),
                (Double) core.getFrontEquatorialCoords().get(core.body.declinationSubject),
            },
            interpolatingFactor
        );
    }

    private void calculateLocalHourAngle() {
        localHourAngle = Calcs.Angle.toNormalSignedLongtiude(siderealTime - core.getRequest().longitude - rightAscension);
    }

    private void claculateMCorrectionForLiminalPhase() {
        double cosDelta = Math.cos(declination);
        double cosPhi = Math.cos(core.getRequest().latitude);

        double altitude = Math.asin(
            Math.sin(core.getRequest().latitude) * Math.sin(declination)
            + cosPhi * cosDelta * Math.cos(localHourAngle)
        );

        mCorrection = (altitude - core.dateLevel.standardAltitude) / (
            Calcs.TURN * cosDelta * cosPhi * Math.sin(localHourAngle)
        );
    }
}
