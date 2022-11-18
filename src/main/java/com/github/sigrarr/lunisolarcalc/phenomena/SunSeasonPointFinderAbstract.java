package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder.MeanSunSeasonPointApproximator;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;

abstract class SunSeasonPointFinderAbstract extends CyclicPhenomenonFinderAbstract {

    public final MeanSunSeasonPointApproximator approximator = new MeanSunSeasonPointApproximator();

    public SunSeasonPointFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
        super(coreCalculator);
    }

    public ResultCyclicPhenomenon<SunSeasonPoint> find(int gregorianYear, SunSeasonPoint point) {
        return new ResultCyclicPhenomenon<>(TimelinePoint.ofJulianEphemerisDay(findJulianEphemerisDay(gregorianYear, point)), point);
    }

    public abstract double findJulianEphemerisDay(int gregorianYear, SunSeasonPoint point);

    public Stream<ResultCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear) {
        return findMany(startGregorianYear, EnumSet.allOf(SunSeasonPoint.class));
    }

    public Stream<ResultCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, EnumSet<SunSeasonPoint> points) {
        return Stream.generate(new ResultSupplier(startGregorianYear, points));
    }

    public Stream<ResultCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, int endGregorianYear) {
        EnumSet<SunSeasonPoint> points = EnumSet.allOf(SunSeasonPoint.class);
        return findMany(startGregorianYear, points)
            .limit(getLimit(startGregorianYear, endGregorianYear, points));
    }

    public Stream<ResultCyclicPhenomenon<SunSeasonPoint>> findMany(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points) {
        return findMany(startGregorianYear, points)
            .limit(getLimit(startGregorianYear, endGregorianYear, points));
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear) {
        return findManyJulianEphemerisDays(startGregorianYear, EnumSet.allOf(SunSeasonPoint.class));
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, EnumSet<SunSeasonPoint> points) {
        return DoubleStream.generate(new ResultSupplier(startGregorianYear, points));
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, int endGregorianYear) {
        EnumSet<SunSeasonPoint> points = EnumSet.allOf(SunSeasonPoint.class);
        return findManyJulianEphemerisDays(startGregorianYear, points)
            .limit(getLimit(startGregorianYear, endGregorianYear, points));
    }

    public DoubleStream findManyJulianEphemerisDays(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points) {
        return findManyJulianEphemerisDays(startGregorianYear, points)
            .limit(getLimit(startGregorianYear, endGregorianYear, points));
    }

    @Override
    protected CycleTemporalApproximate getCycleTemporalApproximate() {
        return MeanMotionApproximate.TROPICAL_YEAR;
    }

    protected int getLimit(int startGregorianYear, int endGregorianYear, EnumSet<SunSeasonPoint> points) {
        return (endGregorianYear - startGregorianYear + 1) * points.size();
    }

    private class ResultSupplier extends ResultSupplierAbstract<SunSeasonPoint> {

        int currentYear;

        ResultSupplier(int startYear, EnumSet<SunSeasonPoint> pointsInScope) {
            super(pointsInScope.stream().sorted().collect(Collectors.toList()));
            currentYear = startYear;
        }

        @Override
        public double getAsDouble() {
            forward();
            return findJulianEphemerisDay(currentYear, currentStage);
        }

        @Override
        void rewindStage() {
            super.rewindStage();
            currentYear++;
        }
    }
}
