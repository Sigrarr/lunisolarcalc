package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder.MeanSunSeasonPointApproximator;
import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion;;

abstract class SunSeasonPointFinderAbstract extends PhenomenonFinderAbstract {

    public final MeanSunSeasonPointApproximator approximator = new MeanSunSeasonPointApproximator();

    public SunSeasonPointFinderAbstract(InstantIndicatingAngleCalculator coreCalculator) {
        super(coreCalculator);
    }

    public double findJulianEphemerisDay(int romanYear, SunSeasonPoint point) {
        return findJulianEphemerisDay(romanYear, point, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, int meanPrecisionSeconds) {
        return findJulianEphemerisDay(romanYear, point, getMeanPrecisionRadians(meanPrecisionSeconds));
    }

    public Stream<FoundPhenomenon<SunSeasonPoint>> findMany(int startRomanYear, int endRomanYear) {
        return findMany(startRomanYear, endRomanYear, EnumSet.allOf(SunSeasonPoint.class), DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundPhenomenon<SunSeasonPoint>> findMany(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points) {
        return findMany(startRomanYear, endRomanYear, points, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundPhenomenon<SunSeasonPoint>> findMany(int startRomanYear, int endRomanYear, int meanPrecisionSeconds) {
        return findMany(startRomanYear, endRomanYear, EnumSet.allOf(SunSeasonPoint.class), meanPrecisionSeconds);
    }

    public Stream<FoundPhenomenon<SunSeasonPoint>> findMany(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
        return Stream.generate(new ResultSupplier(startRomanYear, points, meanPrecisionSeconds))
            .limit(getLimit(startRomanYear, endRomanYear, points));
    }

    public DoubleStream findManyJulianEphemerisDays(int startRomanYear, int endRomanYear) {
        return findManyJulianEphemerisDays(startRomanYear, endRomanYear, EnumSet.allOf(SunSeasonPoint.class), DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points) {
        return findManyJulianEphemerisDays(startRomanYear, endRomanYear, points, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(int startRomanYear, int endRomanYear, int meanPrecisionSeconds) {
        return findManyJulianEphemerisDays(startRomanYear, endRomanYear, EnumSet.allOf(SunSeasonPoint.class), meanPrecisionSeconds);
    }

    public DoubleStream findManyJulianEphemerisDays(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
        return DoubleStream.generate(new ResultSupplier(startRomanYear, points, meanPrecisionSeconds))
            .limit(getLimit(startRomanYear, endRomanYear, points));
    }

    protected abstract double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, double meanPrecisionRadians);

    protected double getMeanPrecisionRadians(int seconds) {
        return Math.toRadians(SunEarthRelativeMotion.degreesPerTimeMiliseconds(1000 * seconds));
    }

    protected int getLimit(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points) {
        return (endRomanYear - startRomanYear + 1) * points.size();
    }

    private class ResultSupplier extends ResultSupplierAbstract<SunSeasonPoint> {

        int currentYear;

        ResultSupplier(int startYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
            super(points.stream().sorted().collect(Collectors.toList()), meanPrecisionSeconds);
            currentYear = startYear;
        }

        @Override
        public double getAsDouble() {
            forward();
            return findJulianEphemerisDay(currentYear, currentInstant, meanPrecisionRadians);
        }

        @Override
        void rewindInstant() {
            super.rewindInstant();
            currentYear++;
        }
    }
}
