package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcDayValues.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;

class DiurnalPhaseCalcCoordsCombiner {

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
        // TODO use 5 values
        return TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                core.getDay(-1).getCoord(coordKey),
                core.getDay(0).getCoord(coordKey),
                core.getDay(+1).getCoord(coordKey),
            },
            vectorFromCenter
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
        return TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {
                core.getDay(dayPosition - 1).getCoord(coordKey),
                core.getDay(dayPosition).getCoord(coordKey),
                core.getDay(dayPosition + 1).getCoord(coordKey)
            },
            vectorFromNoon
        );
    }
}
