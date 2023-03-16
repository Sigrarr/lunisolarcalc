package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class SunDiurnalPhaseFinder extends DiurnalPhaseFinderAbstract {

    public static final Body BODY = Body.SUN;

    public SunDiurnalPhaseFinder() {
        super(new SunDiurnalPhaseCalcCore());
    }

    private static final class SunDiurnalPhaseCalcCore extends DiurnalPhaseCalcCore {
        private static final double STANDARD_ALTITUDE = Math.toRadians(Calcs.Angle.arcminutesToDegrees(-50));

        @Override Body specifyBody() {
            return BODY;
        }

        @Override double getCenterStandardAltitude() {
            return STANDARD_ALTITUDE;
        }

        @Override boolean doesNeedNextCorrection() {
            return iterationLevel.getCorrectionsCount() < 1;
        }
    }
}
