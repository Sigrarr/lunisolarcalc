package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class MoonDiurnalPhaseFinder extends DiurnalPhaseFinderAbstract {

    public static final Body BODY = Body.MOON;

    public MoonDiurnalPhaseFinder() {
        super(new MoonDiurnalPhaseCalcCore());
    }

    private static final class MoonDiurnalPhaseCalcCore extends DiurnalPhaseCalcCore {
        private static final double STANDARD_ALTITUDE_PI_COEFFICIENT = 0.7275;
        private static final double STANDARD_ALTITUDE_FREE_TERM = Math.toRadians(Calcs.Angle.arcminutesToDegrees(-34));
        private final SingleOutputComposition<Subject, TimelinePoint> piCalc = CoordsCalcCompositions.compose(
            Subject.MOON_EQUATORIAL_HORIZONTA_PARALLAX
        );

        @Override Body specifyBody() {
            return BODY;
        }

        @Override double getCenterStandardAltitude() {
            double pi = (Double) piCalc.calculate(midnightTT.getCenter());
            return STANDARD_ALTITUDE_PI_COEFFICIENT * pi + STANDARD_ALTITUDE_FREE_TERM;
        }

        @Override boolean doesNeedNextCorrection() {
            return iterationLevel.getCorrectionsCount() < 3
                && Math.abs(iterationLevel.getLastMCorrection()) > 30 * Calcs.SECOND_TO_DAY;
        }
    }
}
