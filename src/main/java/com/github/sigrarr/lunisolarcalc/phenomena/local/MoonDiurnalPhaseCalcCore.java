package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.OptionalDouble;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.phenomena.global.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

final class MoonDiurnalPhaseCalcCore extends DiurnalPhaseCalcCore {

    private static class DayValues extends DiurnalPhaseCalcDayValues {
        static final int COORD_NOON_STANDARD_ALTITUDE = DiurnalPhaseCalcDayValues.coordsN;
        static int coordsN = DiurnalPhaseCalcDayValues.coordsN + 1;

        private static final double STANDARD_ALTITUDE_PI_COEFFICIENT = 0.7275;
        private static final double STANDARD_ALTITUDE_FREE_TERM = Math.toRadians(Calcs.Angle.arcminutesToDegrees(-34));

        DayValues(DiurnalPhaseCalcCore core, UniversalTimelinePoint noon) {
            super(core, noon);
        }

        @Override protected void loadCoords() {
            super.loadCoords();
            double pi = (Double) ((MoonDiurnalPhaseCalcCore) core).parallaxCalc.calculate(noon);
            coordValues[COORD_NOON_STANDARD_ALTITUDE] = STANDARD_ALTITUDE_PI_COEFFICIENT * pi + STANDARD_ALTITUDE_FREE_TERM;
        }

        @Override protected int getCoordsN() {
            return DayValues.coordsN;
        }
    }

    final SingleOutputComposition<Subject, TimelinePoint> parallaxCalc = CoordsCalcCompositions.compose(Subject.MOON_EQUATORIAL_HORIZONTAL_PARALLAX);
    final MoonPhaseApproximator moonPhaseApproximator = new MoonPhaseApproximator();

    @Override
    protected Body prepareBody() {
        return Body.MOON;
    }

    @Override
    protected DiurnalPhaseCalcTransitResolver prepareTransitResolver() {
        return new DiurnalPhaseCalcTransitResolver(this) {
            @Override OptionalDouble findCentralNoonToTransitVector() {
                double lha = getDay(0).getCoord(DayValues.COORD_NOON_LOCAL_HOUR_ANGLE);
                double backLha = getDay(-1).getCoord(DayValues.COORD_NOON_LOCAL_HOUR_ANGLE);
                double frontLha = getDay(+1).getCoord(DayValues.COORD_NOON_LOCAL_HOUR_ANGLE);
                boolean absent = (
                    Math.signum(lha) != Math.signum(backLha)
                    && Double.compare(Math.abs(lha), Math.abs(backLha)) > 0
                ) || (
                    Math.signum(lha) != Math.signum(frontLha)
                    && Double.compare(Math.abs(lha), Math.abs(frontLha)) >= 0
                );
                return absent ? OptionalDouble.empty() : super.findCentralNoonToTransitVector();
            }
        };
    }

    @Override
    protected DiurnalPhaseCalcExtremeFinder prepareExtremeFinder() {
        return new DiurnalPhaseCalcExtremeFinder(this) {
            @Override protected double resolveCentralStandardAltitude(double vectorFromCenter) {
                return coordsCombiner.interpolateCentralCoord(DayValues.COORD_NOON_STANDARD_ALTITUDE, vectorFromCenter);
            }
        };
    }

    @Override
    protected DiurnalPhaseCalcDayValues prepareDayValues(UniversalTimelinePoint noon) {
        return new DayValues(this, noon);
    }

    @Override
    protected double getNoonStandardAltitude(int dayPosition) {
        return getDay(dayPosition).getCoord(DayValues.COORD_NOON_STANDARD_ALTITUDE);
    }
}
