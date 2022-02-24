package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion;;

abstract class SunSeasonPointFinderAbstract {

    public static final int DEFAULT_MEAN_PRECISION_SECONDS = 15;

    public double findJulianEphemerisDay(int romanYear, SunSeasonPoint point) {
        return findJulianEphemerisDay(romanYear, point, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, int meanPrecisionSeconds) {
        return findJulianEphemerisDay(romanYear, point, getMeanPrecisionRadians(meanPrecisionSeconds));
    }

    public Stream<FoundSunSeasonPoint> findMany(int startRomanYear, int endRomanYear) {
        return findMany(startRomanYear, endRomanYear, EnumSet.allOf(SunSeasonPoint.class), DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundSunSeasonPoint> findMany(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points) {
        return findMany(startRomanYear, endRomanYear, points, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundSunSeasonPoint> findMany(int startRomanYear, int endRomanYear, int meanPrecisionSeconds) {
        return findMany(startRomanYear, endRomanYear, EnumSet.allOf(SunSeasonPoint.class), meanPrecisionSeconds);
    }

    public Stream<FoundSunSeasonPoint> findMany(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
        return Stream.
            generate(new ResultSupplier(startRomanYear, points, meanPrecisionSeconds))
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
        return DoubleStream
            .generate(new ResultSupplier(startRomanYear, points, meanPrecisionSeconds))
            .limit(getLimit(startRomanYear, endRomanYear, points));
    }

    protected abstract double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, double meanPrecisionRadians);

    protected double getMeanPrecisionRadians(int seconds) {
        return Math.toRadians(SunEarthRelativeMotion.degreesPerTimeMiliseconds(1000 * seconds));
    }

    protected int getLimit(int startRomanYear, int endRomanYear, EnumSet<SunSeasonPoint> points) {
        return (endRomanYear - startRomanYear + 1) * points.size();
    }

    protected class ResultSupplier implements DoubleSupplier, Supplier<FoundSunSeasonPoint> {
        protected final List<SunSeasonPoint> points;
        protected final double meanPrecisionRadians;
        protected Iterator<SunSeasonPoint> pIterator;
        protected SunSeasonPoint currentPoint;
        protected int currentYear;

        ResultSupplier(int startYear, EnumSet<SunSeasonPoint> points, int meanPrecisionSeconds) {
            this.points = points.stream().collect(Collectors.toList());
            meanPrecisionRadians = getMeanPrecisionRadians(meanPrecisionSeconds);
            this.pIterator = this.points.listIterator();
            currentYear = startYear;
        }

        @Override
        public double getAsDouble() {
            forward();
            return findJulianEphemerisDay(currentYear, currentPoint, meanPrecisionRadians);
        }

        @Override
        public FoundSunSeasonPoint get() {
            return new FoundSunSeasonPoint(getAsDouble(), currentPoint);
        }

        protected void forward() {
            if (!pIterator.hasNext()) {
                pIterator = points.listIterator();
                currentYear++;
            }
            currentPoint = pIterator.next();
        }
    }
}
