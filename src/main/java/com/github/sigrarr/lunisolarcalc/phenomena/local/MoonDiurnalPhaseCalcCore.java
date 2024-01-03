package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.OptionalDouble;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.phenomena.Body;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

final class MoonDiurnalPhaseCalcCore extends DiurnalPhaseCalcCore {

    static class StandardAltitudeCalculator {
        private static final double STANDARD_ALTITUDE_PI_COEFFICIENT = 0.7275;
        private static final double STANDARD_ALTITUDE_FREE_TERM = Math.toRadians(Calcs.Angle.arcminutesToDegrees(-34));
        private final SingleOutputComposition<Subject, TimelinePoint> parallaxCalc = CoordsCalcCompositions.compose(Subject.MOON_EQUATORIAL_HORIZONTAL_PARALLAX);

        public double calculate(TimelinePoint tx) {
            return STANDARD_ALTITUDE_PI_COEFFICIENT * (Double)parallaxCalc.calculate(tx) + STANDARD_ALTITUDE_FREE_TERM;
        }
    }

    static class DayValues extends DiurnalPhaseCalcDayValues {
        static final int COORD_STANDARD_ALTITUDE = DiurnalPhaseCalcDayValues.coordsN;
        static int coordsN = DiurnalPhaseCalcDayValues.coordsN + 1;

        DayValues(DiurnalPhaseCalcCore core, UniversalTimelinePoint noon) {
            super(core, noon);
        }

        @Override protected void loadCoords() {
            super.loadCoords();
            coordValues[COORD_STANDARD_ALTITUDE] = ((MoonDiurnalPhaseCalcCore) core).standardAltitudeCalculator.calculate(noon);
        }

        @Override protected int getCoordsN() {
            return DayValues.coordsN;
        }
    }

    class TransitResolver extends DiurnalPhaseCalcTransitResolver {
        TransitResolver(DiurnalPhaseCalcCore core) {
            super(core);
        }

        @Override OptionalDouble findCentralNoonToTransitVector() {
            double lha = getDay(0).getNoonCoord(DayValues.COORD_LOCAL_HOUR_ANGLE);
            double backLha = getDay(-1).getNoonCoord(DayValues.COORD_LOCAL_HOUR_ANGLE);
            double frontLha = getDay(+1).getNoonCoord(DayValues.COORD_LOCAL_HOUR_ANGLE);
            boolean absent = (
                Math.signum(lha) != Math.signum(backLha)
                && Double.compare(Math.abs(lha - backLha), Math.PI) > 0
                && Double.compare(Math.abs(lha), Math.abs(backLha)) > 0
            ) || (
                Math.signum(lha) != Math.signum(frontLha)
                && Double.compare(Math.abs(lha - frontLha), Math.PI) > 0
                && Double.compare(Math.abs(lha), Math.abs(frontLha)) >= 0
            );
            return absent ? OptionalDouble.empty() : super.findCentralNoonToTransitVector();
        }
    }

    class ExtremeApproximator extends DiurnalPhaseCalcExtremeApproximator {
        ExtremeApproximator(DiurnalPhaseCalcCore core) {
            super(core);
        }

        @Override protected OptionalDouble approximateVectorFromTransitToExtremePhase(int direction) {
            DiurnalPhaseCalcDayValues adjacent = getDay(direction);
            if (!(adjacent.hasFinalizedTransit() && adjacent.getFinalTransit().isPresent()) && isTransitCloserToCenterNoonThanToItsOwn(direction))
                redirectAdjacentShellTransitToFurtherInDirection(adjacent, direction);
            return super.approximateVectorFromTransitToExtremePhase(direction);
        }

        private boolean isTransitCloserToCenterNoonThanToItsOwn(int direction) {
            double adjacentNoonToTransit = getCloseNoonToTransitVector(direction);
            return Double.compare(Math.abs(direction + adjacentNoonToTransit), Math.abs(adjacentNoonToTransit)) < 0;
        }

        private void redirectAdjacentShellTransitToFurtherInDirection(DiurnalPhaseCalcDayValues adjacent, int direction) {
            DiurnalPhaseCalcDayValues further = getDay(direction*2);
            double futherNoonToItsTransit = further.has(DayValues.NOON_TO_TRANSIT_VECTOR) ? further.get(DayValues.NOON_TO_TRANSIT_VECTOR, null)
                : transitResolver.approximateNoonToTransitVector(direction*2);
            adjacent.set(DayValues.NOON_TO_TRANSIT_VECTOR, direction + futherNoonToItsTransit);
            adjacent.clear(DayValues.TRANSIT_EXTREME_LOCAL_HOUR_ANGLE_COS);
        }
    }

    class ExtremeFinder extends DiurnalPhaseCalcExtremeFinder {
        ExtremeFinder(DiurnalPhaseCalcCore core) {
            super(core);
        }

        @Override protected double resolveCentralStandardAltitude(double vectorFromCenter) {
            return coordsCombiner.interpolateCentralCoord(DayValues.COORD_STANDARD_ALTITUDE, vectorFromCenter);
        }
    }

    final StandardAltitudeCalculator standardAltitudeCalculator = new StandardAltitudeCalculator();

    @Override
    protected Body prepareBody() {
        return Body.MOON;
    }

    @Override
    protected DiurnalPhaseCalcTransitResolver prepareTransitResolver() {
        return new TransitResolver(this);
    }

    @Override
    protected DiurnalPhaseCalcExtremeApproximator prepareExtremeApproximator() {
        return new ExtremeApproximator(this);
    }

    @Override
    protected DiurnalPhaseCalcExtremeFinder prepareExtremeFinder() {
        return new ExtremeFinder(this);
    }

    @Override
    protected DiurnalPhaseCalcDayValues prepareDayValues(UniversalTimelinePoint noon) {
        return new DayValues(this, noon);
    }

    @Override
    protected double getNoonStandardAltitude(int dayPosition) {
        return getDay(dayPosition).getNoonCoord(DayValues.COORD_STANDARD_ALTITUDE);
    }
}
