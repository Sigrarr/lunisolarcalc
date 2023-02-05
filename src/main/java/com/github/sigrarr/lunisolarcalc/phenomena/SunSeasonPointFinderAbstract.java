package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinders.StageIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.time.DynamicalTimelinePoint;

abstract class SunSeasonPointFinderAbstract extends CyclicPhenomenonFinderAbstract {

    protected final SunSeasonPointApproximator approximator = new SunSeasonPointApproximator();

    public SunSeasonPointFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
        super(coreCalculator);
    }

    /**
     * Finds the occurrence of the requested Equinox/Solstice in the tropical year
     * which begins in the requested {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year.
     *
     * Note that for years before -1176, December Solstice may occur in the next calendar year after the requested.
     *
     * @param calendarYear  {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                      of the beginning of the tropical year to look in (in astronomical numbering)
     * @param point         Equinox/Solstice to look for
     * @return              found occurrence
     */
    public DynamicalOccurrence<SunSeasonPoint> find(int calendarYear, SunSeasonPoint point) {
        return new DynamicalOccurrence<>(new DynamicalTimelinePoint(findJulianEphemerisDay(calendarYear, point)), point);
    }

    /**
     * Finds time of occurrence of the requested Equinox/Solstice in the tropical year
     * which begins in the requested {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year,
     * in Julian Ephemeris Day.
     *
     * Note that for years before -1176, December Solstice may occur in the next calendar year after the requested.
     *
     * @param calendarYear  {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                      of the beginning of the tropical year to look in (in astronomical numbering)
     * @param point         Equinox/Solstice to look for
     * @return              time of occurrence, in Julian Ephemeris Day
     */
    public abstract double findJulianEphemerisDay(int calendarYear, SunSeasonPoint point);

    /**
     * Finds and streams subsequent occurrences of any Equinoxes/Solstices,
     * starting at the tropical year which begins in the requested
     * {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year.
     *
     * @param startCalendarYear     {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                              of the beginning of the tropical year to start at (in astronomical numbering)
     * @return                      unterminated {@link Stream} of found occurrences
     */
    public Stream<DynamicalOccurrence<SunSeasonPoint>> findMany(int startCalendarYear) {
        return findMany(startCalendarYear, EnumSet.allOf(SunSeasonPoint.class));
    }

    /**
     * Finds and streams subsequent occurrences of the requested Equinox/Solstice,
     * starting at the tropical year which begins in the requested
     * {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year.
     *
     * Note that for years before -1176, December Solstice may occur in the calendar year after the requested.
     *
     * @param startCalendarYear     {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                              of the beginning of the tropical year to start at (in astronomical numbering)
     * @param point                 Equinox/Solstice to look for
     * @return                      unterminated {@link Stream} of found occurrences
     */
    public Stream<DynamicalOccurrence<SunSeasonPoint>> findMany(int startCalendarYear, SunSeasonPoint point) {
        return findMany(startCalendarYear, EnumSet.of(point));
    }

    /**
     * Finds and streams subsequent occurrences of requested Equinoxes/Solstices,
     * starting at the tropical year which begins in the requested
     * {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year.
     *
     * Note that for years before -1176, December Solstice may occur in the calendar year after the requested.
     *
     * @param startCalendarYear     {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                              of the beginning of the tropical year to start at (in astronomical numbering)
     * @param points                set of Equinoxes/Solstices to look for
     * @return                      unterminated {@link Stream} of found occurrences
     */
    public Stream<DynamicalOccurrence<SunSeasonPoint>> findMany(int startCalendarYear, EnumSet<SunSeasonPoint> points) {
        return Stream.generate(new ResultSupplier(startCalendarYear, points));
    }

    /**
     * Finds and streams times of subsequent occurrences of any Equinoxes/Solstices,
     * starting at the tropical year which begins in the requested
     * {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year,
     * in Julian Ephemeris Days.
     *
     * @param startCalendarYear     {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                              of the beginning of the tropical year to start at (in astronomical numbering)
     * @return                      unterminated {@linkplain DoubleStream stream} of times of found occurrences,
     *                              in Julian Ephemeris Days
     */
    public DoubleStream findManyJulianEphemerisDays(int startCalendarYear) {
        return findManyJulianEphemerisDays(startCalendarYear, EnumSet.allOf(SunSeasonPoint.class));
    }

    /**
     * Finds and streams times of subsequent occurrences of the requested Equinoxes/Solstices,
     * starting at the tropical year which begins in the requested
     * {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year,
     * in Julian Ephemeris Days.
     *
     * Note that for years before -1176, December Solstice may occur in the calendar year after the requested.
     *
     * @param startCalendarYear     {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                              of the beginning of the tropical year to start at (in astronomical numbering)
     * @param point                 Equinox/Solstice to look for
     * @return                      unterminated {@linkplain DoubleStream stream} of times of found occurrences,
     *                              in Julian Ephemeris Days
     */
    public DoubleStream findManyJulianEphemerisDays(int startCalendarYear, SunSeasonPoint point) {
        return findManyJulianEphemerisDays(startCalendarYear, EnumSet.of(point));
    }

    /**
     * Finds and streams times of subsequent occurrences of requested Equinoxes/Solstices,
     * starting at the tropical year which begins in the requested
     * {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year,
     * in Julian Ephemeris Days.
     *
     * Note that for years before -1176, December Solstice may occur in the calendar year after the requested.
     *
     * @param startCalendarYear     {@linkplain com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint calendar} year
     *                              of the beginning of the tropical year to start at (in astronomical numbering)
     * @param points                set of Equinoxes/Solstices to look for
     * @return                      unterminated {@linkplain DoubleStream stream} of times of found occurrences,
     *                              in Julian Ephemeris Days
     */
    public DoubleStream findManyJulianEphemerisDays(int startCalendarYear, EnumSet<SunSeasonPoint> points) {
        return DoubleStream.generate(new ResultSupplier(startCalendarYear, points));
    }

    @Override
    protected MeanCycle getMeanCycle() {
        return MeanCycle.TROPICAL_YEAR;
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
