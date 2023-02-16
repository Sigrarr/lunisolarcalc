package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.Subject;
import com.github.sigrarr.lunisolarcalc.time.TimeScaleDelta;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

class DiurnalPhaseCalcDateLevelValues {

    private final DiurnalPhaseCalcCore core;

    private boolean present;
    double standardAltitude;
    double hourAngle;
    double siderealTimeDegrees;
    double deltaTSeconds;
    double initialTransitM;

    DiurnalPhaseCalcDateLevelValues(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    public boolean areDiurnalPhasesPresent() {
        return present;
    }

    public void recalculate() {
        standardAltitude = core.getCenterStandardAltitude();
        double frontDeclination = (Double) core.getFrontCoords().get(core.body.declinationSubject);

        double cosH0 = (Math.sin(standardAltitude) - Math.sin(core.getRequest().latitude) * Math.sin(frontDeclination))
            / (Math.cos(core.getRequest().latitude) * Math.cos(frontDeclination));

        present = Double.compare(Math.abs(cosH0), 1.0) <= 0;
        if (!present)
            return;

        hourAngle = Math.acos(cosH0);
        siderealTimeDegrees = (Double) core.getCenterCoords().get(Subject.SIDEREAL_APPARENT_TIME);

        double frontAscension = (Double) core.getFrontCoords().get(core.body.rightAscensionSubject);
        initialTransitM = Calcs.Angle.normalizeLongitudinally(
            frontAscension + core.getRequest().longitude - Math.toRadians(siderealTimeDegrees)
        ) / Calcs.TURN;

        deltaTSeconds = TimeScaleDelta.getDeltaTSeconds(core.midnight.getCenter());
    }
}
