package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder.MeanSunSeasonPointApproximator;
import com.github.sigrarr.lunisolarcalc.util.*;

abstract class SunSeasonPointFinderAbstract extends CyclicPhenomenonFinderAbstract {

    public final MeanSunSeasonPointApproximator approximator = new MeanSunSeasonPointApproximator();

    public SunSeasonPointFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
        super(coreCalculator);
    }

    public double findJulianEphemerisDay(int gregorianYear, SunSeasonPoint point) {
        return findJulianEphemerisDay(gregorianYear, point, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public double findJulianEphemerisDay(int gregorianYear, SunSeasonPoint point, int meanPrecisionSeconds) {
        return findJulianEphemerisDay(gregorianYear, point, getMeanPrecisionRadians(meanPrecisionSeconds));
    }

    public Stream<FoundCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, int endGregorianYear) {
        return findMany(startGregorianYear, endGregorianYear, EnumSet.allOf(SunSeasonPoint.class), DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points) {
        return findMany(startGregorianYear, endGregorianYear, points, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, int endGregorianYear, int meanPrecisionSeconds) {
        return findMany(startGregorianYear, endGregorianYear, EnumSet.allOf(SunSeasonPoint.class), meanPrecisionSeconds);
    }

    public Stream<FoundCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
        return Stream.generate(new ResultSupplier(startGregorianYear, points, meanPrecisionSeconds))
            .limit(getLimit(startGregorianYear, endGregorianYear, points));
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, int endGregorianYear) {
        return findManyJulianEphemerisDays(startGregorianYear, endGregorianYear, EnumSet.allOf(SunSeasonPoint.class), DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points) {
        return findManyJulianEphemerisDays(startGregorianYear, endGregorianYear, points, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, int endGregorianYear, int meanPrecisionSeconds) {
        return findManyJulianEphemerisDays(startGregorianYear, endGregorianYear, EnumSet.allOf(SunSeasonPoint.class), meanPrecisionSeconds);
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
        return DoubleStream.generate(new ResultSupplier(startGregorianYear, points, meanPrecisionSeconds))
            .limit(getLimit(startGregorianYear, endGregorianYear, points));
    }

    protected abstract double findJulianEphemerisDay(int gregorianYear, SunSeasonPoint point, double meanPrecisionRadians);

    @Override
    protected CycleTemporalApproximate getCycleTemporalApproximate() {
        return MeanMotionApproximate.TROPICAL_YEAR;
    }

    protected int getLimit(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points) {
        return (endGregorianYear - startGregorianYear + 1) * points.size();
    }

    private class ResultSupplier extends ResultSupplierAbstract<SunSeasonPoint> {

        int currentYear;

        ResultSupplier(int startYear, EnumSet<SunSeasonPoint> pointsInScope, int meanPrecisionSeconds) {
            super(pointsInScope.stream().sorted().collect(Collectors.toList()), meanPrecisionSeconds);
            currentYear = startYear;
        }

        @Override
        public double getAsDouble() {
            forward();
            return findJulianEphemerisDay(currentYear, currentStage, meanPrecisionRadians);
        }

        @Override
        void rewindStage() {
            super.rewindStage();
            currentYear++;
        }
    }
}
