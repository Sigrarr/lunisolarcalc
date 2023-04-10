package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

class MoonDiurnalPhaseCalcCore extends DiurnalPhaseCalcCore {

    private static class DayValues extends DiurnalPhaseCalcDayValues {
        static final int COORD_NOON_EQUATORIAL_HORIZONTAL_PARALLAX = DiurnalPhaseCalcDayValues.coordsN;
        static int coordsN = DiurnalPhaseCalcDayValues.coordsN + 1;

        DayValues(DiurnalPhaseCalcCore core, UniversalTimelinePoint noon) {
            super(core, noon);
        }

        @Override protected void loadCoords() {
            super.loadCoords();
            coordValues[COORD_NOON_EQUATORIAL_HORIZONTAL_PARALLAX] = (Double) ((MoonDiurnalPhaseCalcCore) core).parallaxCalc.calculate(noon);
        }

        @Override protected int getCoordsN() {
            return DayValues.coordsN;
        }
    }

    private static final double STANDARD_ALTITUDE_PI_COEFFICIENT = 0.7275;
    private static final double STANDARD_ALTITUDE_FREE_TERM = Math.toRadians(Calcs.Angle.arcminutesToDegrees(-34));
    final SingleOutputComposition<Subject, TimelinePoint> parallaxCalc = CoordsCalcCompositions.compose(Subject.MOON_EQUATORIAL_HORIZONTAL_PARALLAX);

    @Override
    protected Body prepareBody() {
        return Body.MOON;
    }

    @Override
    protected DiurnalPhaseCalcExtremeFinder prepareExtremeFinder() {
        return new DiurnalPhaseCalcExtremeFinder(this) {
            @Override protected double resolveCentralStandardAltitude(double vectorFromCenter) {
                return TabularInterpolation.interpolateFromFiveValuesAndFactor(
                    new double[] {
                        getNoonStandardAltitude(-2),
                        getNoonStandardAltitude(-1),
                        getNoonStandardAltitude(0),
                        getNoonStandardAltitude(+1),
                        getNoonStandardAltitude(+2),
                    },
                    vectorFromCenter
                );
            }
        };
    }

    @Override
    protected DiurnalPhaseCalcDayValues prepareDayValues(UniversalTimelinePoint noon) {
        return new DayValues(this, noon);
    }

    @Override
    protected double getNoonStandardAltitude(int dayPosition) {
        double pi = getDay(dayPosition).getCoord(DayValues.COORD_NOON_EQUATORIAL_HORIZONTAL_PARALLAX);
        return STANDARD_ALTITUDE_PI_COEFFICIENT * pi + STANDARD_ALTITUDE_FREE_TERM;
    }
}
