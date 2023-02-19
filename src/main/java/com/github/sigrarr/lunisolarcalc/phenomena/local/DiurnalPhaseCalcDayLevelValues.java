package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.time.TimeScaleDelta;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

class DiurnalPhaseCalcDayLevelValues {

    private final DiurnalPhaseCalcCore core;

    private boolean present;
    double standardAltitude;
    double hourAngle;
    double deltaTDays;
    double utSiderealTimeDegrees;
    double initialTransitM;

    DiurnalPhaseCalcDayLevelValues(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    boolean areDiurnalPhasesPresent() {
        return present;
    }

    void recalculate() {
        standardAltitude = core.getCenterStandardAltitude();
        double frontDeclination = (Double) core.getFrontEquatorialCoords().get(core.body.declinationSubject);

        double cosH0 = (Math.sin(standardAltitude) - Math.sin(core.getRequest().latitude) * Math.sin(frontDeclination))
            / (Math.cos(core.getRequest().latitude) * Math.cos(frontDeclination));

        present = Double.compare(Math.abs(cosH0), 1.0) <= 0;
        if (!present)
            return;

        hourAngle = Math.acos(cosH0);
        deltaTDays = Calcs.SECOND_TO_DAY * TimeScaleDelta.getDeltaTSeconds(core.midnightTT.getCenter());
        utSiderealTimeDegrees = core.getCenterUniversalMidnightSiderealTimeDegrees();

        double frontAscension = (Double) core.getFrontEquatorialCoords().get(core.body.rightAscensionSubject);
        initialTransitM = Calcs.Angle.toNormalLongitude(
            frontAscension + core.getRequest().longitude - Math.toRadians(utSiderealTimeDegrees)
        ) / Calcs.TURN;
    }

    double calculateInitialRiseM() {
        return initialTransitM - hourAngle / Calcs.TURN;
    }

    double calculateInitialSetM() {
        return initialTransitM + hourAngle / Calcs.TURN;
    }
}
