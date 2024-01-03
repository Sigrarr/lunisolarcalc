package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.phenomena.Body;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

final class SunDiurnalPhaseCalcCore extends DiurnalPhaseCalcCore {

    static final double STANDARD_ALTITUDE = Math.toRadians(Calcs.Angle.arcminutesToDegrees(-50));

    @Override
    protected Body prepareBody() {
        return Body.SUN;
    }

    @Override
    protected DiurnalPhaseCalcExtremeFinder prepareExtremeFinder() {
        return new DiurnalPhaseCalcExtremeFinder(this) {
            @Override protected double resolveCentralStandardAltitude(double vectorFromCenter) {
                return STANDARD_ALTITUDE;
            }
        };
    }

    @Override
    protected double getNoonStandardAltitude(int dayPosition) {
        return STANDARD_ALTITUDE;
    }
}
