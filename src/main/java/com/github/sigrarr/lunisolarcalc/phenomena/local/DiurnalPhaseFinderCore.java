package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

abstract class DiurnalPhaseFinderCore {

    private static final double M_TO_SIDEREAL_TIME_DEGREES_MULTIPLIER = 360.985647;

    final Body body;
    final MultiOutputComposition<Subject, TimelinePoint> equatorialCoordsCalc;

    final FlexTriadBuffer<UniversalTimelinePoint> midnight = new FlexTriadBuffer<>();
    final FlexTriadBuffer<Map<Subject, ?>> equatorialCoords = new FlexTriadBuffer<>();
    double longitude;
    double latitude;
    List<DiurnalPhase> orderedPhasesInScope;
    Iterator<DiurnalPhase> phaseIt;
    boolean startFlag = true;

    DiurnalPhase currentPhase;
    boolean hasValue;
    double centerStandardAltitude;
    double centerHourAngle;
    double centerSiderealTimeDegrees;
    double centerDeltaTSeconds;

    double transitM;
    double mSiderealTime;
    double mInterpolatingFactor;
    double mRightAscension;
    double mDeclination;
    double mHourAngle;
    double mAltitude;

    DiurnalPhaseFinderCore(Body body) {
        this.body = body;
        equatorialCoordsCalc = CoordsCalcCompositions.compose(EnumSet.of(
            body.rightAscensionSubject, body.declinationSubject
        ));
    }

    public UniversalTimelinePoint tryResolveNext() {
        if (!pullStartFlag())
            forward();
        if (hasValue) {
            double m = 0.0;
            switch (currentPhase) {
                case RISING:
                    m = calculateRising();
                    break;
                case TRANSIT:
                    m = calculateTransit();
                    break;
                case SETTING:
                    m = calculateSetting();
                    break;
            }
            return midnight.getCenter().add(m);
        }
        return null;
    }

    private void recalculateDateBaseValues() {
        double centerDelta = (Double) getCenterEquatorialCoords().get(body.declinationSubject);
        centerStandardAltitude = resolveAltitude();

        double cosH0 = (Math.sin(centerStandardAltitude) - Math.sin(latitude) * Math.sin(centerDelta))
            / (Math.cos(latitude) * Math.cos(centerDelta));

        if (Double.compare(Math.abs(cosH0), 1.0) > 0) {
            hasValue = false;
            return;
        }

        centerHourAngle = Math.cos(cosH0);
        centerSiderealTimeDegrees = resolveSiderealTime();
        double frontAlpha = (Double) getFrontEquatorialCoords().get(body.rightAscensionSubject);
        transitM = Calcs.Angle.normalizeLongitudinally(
            frontAlpha + longitude - Math.toRadians(resolveSiderealTime())
        ) / Calcs.TURN;
        centerDeltaTSeconds = TimeScaleDelta.getDeltaTSeconds(midnight.getCenter());
    }

    private double calculateTransit() {
        calculateMSiderealTimeAndInterpolatingFactor(transitM);
        interpolateAlphaAndCalculateLocalHourAngle();
        double mCorrection = -mHourAngle/Calcs.TURN;
        return transitM + mCorrection;
    }

    private double calculateRising() {
        double risingM = transitM - centerHourAngle / Calcs.TURN;
        calculateMSiderealTimeAndInterpolatingFactor(risingM);
        interpolateAlphaAndCalculateLocalHourAngle();
        interpolateDelta();
        double mCorrection = claculateLiminalPhaseMCorrection();
        return risingM + mCorrection;
    }

    private double calculateSetting() {
        double settingM = transitM + centerHourAngle / Calcs.TURN;
        calculateMSiderealTimeAndInterpolatingFactor(settingM);
        interpolateAlphaAndCalculateLocalHourAngle();
        interpolateDelta();
        double mCorrection = claculateLiminalPhaseMCorrection();
        return settingM + mCorrection;
    }

    private void calculateMSiderealTimeAndInterpolatingFactor(double m) {
        mSiderealTime = Math.toRadians(centerSiderealTimeDegrees + M_TO_SIDEREAL_TIME_DEGREES_MULTIPLIER * m);
        mInterpolatingFactor = m + Calcs.SECOND_TO_DAY*centerDeltaTSeconds;
    }

    private void interpolateAlphaAndCalculateLocalHourAngle() {
        mRightAscension = Interpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                (Double) getBackEquatorialCoords().get(body.rightAscensionSubject),
                (Double) getCenterEquatorialCoords().get(body.rightAscensionSubject),
                (Double) getFrontEquatorialCoords().get(body.rightAscensionSubject),
            },
            mInterpolatingFactor
        );
        mHourAngle = mSiderealTime - longitude - mRightAscension;
    }

    private void interpolateDelta() {
        mDeclination = Interpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                (Double) getBackEquatorialCoords().get(body.declinationSubject),
                (Double) getCenterEquatorialCoords().get(body.declinationSubject),
                (Double) getFrontEquatorialCoords().get(body.declinationSubject),
            },
            mInterpolatingFactor
        );
    }

    private double claculateLiminalPhaseMCorrection() {
        double cosDelta = Math.cos(mDeclination);
        double cosPhi = Math.cos(latitude);

        double altitude = Math.asin(
            Math.sin(latitude) * Math.sin(mDeclination)
            + cosPhi * cosDelta * Math.cos(mHourAngle)
        );

        return (altitude - centerStandardAltitude) / (
            Calcs.TURN * cosDelta * cosPhi * Math.sin(mHourAngle)
        );
    }

    void reset(UniversalTimelinePoint midnightDate, double longitude, double latitude, EnumSet<DiurnalPhase> phases) {
        midnight.setBack(midnightDate.add(-1.0));
        midnight.setCenter(midnightDate);
        midnight.setFront(midnightDate.add(+1.0));
        equatorialCoords.clear();
        this.longitude = longitude;
        this.latitude = latitude;
        orderedPhasesInScope = phases.stream().sorted().collect(Collectors.toList());
        phaseIt = orderedPhasesInScope.listIterator();
        currentPhase = phaseIt.next();
        startFlag = true;
        recalculateDateBaseValues();
    }

    private void forward() {
        if (!phaseIt.hasNext()) {
            phaseIt = orderedPhasesInScope.listIterator();
            midnight.push(midnight.getFront().add(1.0));
            equatorialCoords.push(null);
            recalculateDateBaseValues();
        }
        currentPhase = phaseIt.next();
    }

    abstract protected double resolveAltitude();

    abstract protected double resolveSiderealTime();

    private Map<Subject, ?> getBackEquatorialCoords() {
        if (!equatorialCoords.hasBack())
            equatorialCoords.setBack(equatorialCoordsCalc.calculate(midnight.getBack()));
        return equatorialCoords.getBack();
    }

    private Map<Subject, ?> getCenterEquatorialCoords() {
        if (!equatorialCoords.hasCenter())
            equatorialCoords.setCenter(equatorialCoordsCalc.calculate(midnight.getCenter()));
        return equatorialCoords.getCenter();
    }

    private Map<Subject, ?> getFrontEquatorialCoords() {
        if (!equatorialCoords.hasFront())
            equatorialCoords.setFront(equatorialCoordsCalc.calculate(midnight.getFront()));
        return equatorialCoords.getFront();
    }

    private boolean pullStartFlag() {
        boolean flag = startFlag;
        startFlag = false;
        return flag;
    }
}
