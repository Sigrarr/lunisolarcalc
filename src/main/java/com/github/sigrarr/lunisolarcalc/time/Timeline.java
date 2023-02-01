package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;
import com.github.sigrarr.lunisolarcalc.time.exceptions.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * An abstract representation of the timeline: constants,
 * methods converting different expressions of time between each other
 * and management of {@link #getEquivUnitDays() time equivalence unit}.
 * For the representation of an individual point of the timeline, see {@link TimelinePoint}.
 *
 * The time interval supported by Luni-Solar Calc is the current Julian Period:
 * from -4712.01.01 12:00 of the Julian calendar to +3268-01-23 12:00 of the Gregorian calendar
 * (a Julian Period's length is defined as 7980 {@link #JULIAN_YEAR_DAYS Julian years},
 * so the end point is equal to +3268-01-01 12:00 of the proleptic Julian calendar).
 * Some algorithms used in this package might give quite imprecise results outside of this scope,
 * or even don't work at all.
 *
 * The Luni-Solar Calc's leading time unit is Julian Day: number of days from the beginning
 * of the current Julian Period.
 *
 * @see " Meeus 1998: Ch. 7 (p. 59...)
 * @see " Seidelmann 1992: Ch. 12 by L.E. Doggett, 12.23 (p. 583), 12.7 (p. 600)
 */
public abstract class Timeline {
    /**
     * A time unit equal to the mean length of a year in the Julian Calendar, in days.
     */
    public static final double JULIAN_YEAR_DAYS = 365.25;
    /**
     * 100 {@link #JULIAN_YEAR_DAYS Julian years}, in days.
     */
    public static final double JULIAN_CENTURY_DAYS = 100.0 * JULIAN_YEAR_DAYS;
    /**
     * 1000 {@link #JULIAN_YEAR_DAYS Julian years}, in days.
     */
    public static final double JULIAN_MILLENIUM_DAYS = 1000.0 * JULIAN_YEAR_DAYS;
    /**
     * The length of a Julian Period expressed in {@link #JULIAN_YEAR_DAYS Julian years}.
     */
    public static final int JULIAN_PERIOD_YEARS = 7980;

    /**
     * Julian Day of the beginning of the current Julian Period (0 by definiton).
     */
    public static final double JULIAN_PERIOD_START_JD = 0.0;
    /**
     * The beginning of the current Julian Period, in {@link TimeScale#DYNAMICAL Dynamical Time}.
     */
    public static final DynamicalTimelinePoint JULIAN_PERIOD_START_TT = new DynamicalTimelinePoint(JULIAN_PERIOD_START_JD);
    /**
     * The beginning of the current Julian Period, in {@link TimeScale#UNIVERSAL Universal Time}.
     */
    public static final UniversalTimelinePoint JULIAN_PERIOD_START_UT = new UniversalTimelinePoint(JULIAN_PERIOD_START_JD);
    /**
     * Julian day of the beginning of the next Julian Period (the upper limit of the Luni-Solar Calc's scope).
     */
    public static final double JULIAN_PERIOD_END_JD = JULIAN_PERIOD_YEARS * JULIAN_YEAR_DAYS;
    /**
     * The beginning of the next Julian Period (the upper limit of the Luni-Solar Calc's scope).
     * In {@link TimeScale#DYNAMICAL Dynamical Time}.
     */
    public static final DynamicalTimelinePoint JULIAN_PERIOD_END_TT = new DynamicalTimelinePoint(JULIAN_PERIOD_END_JD);
    /**
     * The beginning of the next Julian Period (the upper limit of the Luni-Solar Calc's scope).
     * In {@link TimeScale#UNIVERSAL Universal Time}.
     */
    public static final UniversalTimelinePoint JULIAN_PERIOD_END_UT = new UniversalTimelinePoint(JULIAN_PERIOD_END_JD);

    /**
     * Julian Day of the Epoch 2000 (+2000-01-01 12:00 of the Gregorian calendar).
     */
    public static final double EPOCH_2000_JD = 2451545.0;
    /**
     * The Epoch 2000 (+2000-01-01 12:00 of the Gregorian calendar).
     * In {@link TimeScale#DYNAMICAL Dynamical Time}.
     */
    public static final DynamicalTimelinePoint EPOCH_2000_TT = new DynamicalTimelinePoint(EPOCH_2000_JD);
    /**
     * The Epoch 2000 (+2000-01-01 12:00 of the Gregorian calendar).
     * In {@link TimeScale#UNIVERSAL Universal Time}.
     */
    public static final UniversalTimelinePoint EPOCH_2000_UT = new UniversalTimelinePoint(EPOCH_2000_JD);

    /**
     * Julian Day of the {@link CalendarPoint#GREGORIAN_RULES_START start point of the Gregorian calendar}.
     */
    public static final double GREGORIAN_CALENDAR_START_JD = 2299160.5;
    /**
     * The {@link CalendarPoint#GREGORIAN_RULES_START start point of the Gregorian calendar}
     * (in {@link TimeScale#UNIVERSAL Universal Time}).
     */
    public static final UniversalTimelinePoint GREGORIAN_CALENDAR_START = new UniversalTimelinePoint(2299160.5);
    private static final double GREGORIAN_CALENDAR_START_DAY_NOON_JD = GREGORIAN_CALENDAR_START_JD + 0.5;

    /**
     * The default value of {@link #getEquivUnitDays() equivalence unit}, in days (1 second).
     */
    public static final double DEFAULT_EQUIV_UNIT_DAYS = Calcs.SECOND_TO_DAY;
    /**
     * Minimal allowed value of {@link #getEquivUnitDays() equivalence unit}, in days (1 milisecond).
     */
    public static final double MIN_EQUIV_UNIT_DAYS = 0.001 * Calcs.SECOND_TO_DAY;

    private static final double JD_MONTH_FACTOR = 30.6 + Calcs.EPSILON;
    private static double equivUnitDays = DEFAULT_EQUIV_UNIT_DAYS;

    /**
     * Converts a calendar point (of a calendar based formally on the Julian calendar) to Julian Day.
     * Implementation of the algorithm given by Meeus.
     *
     * @param normalCalendarPoint   calendar point (of a calendar based formally on the Julian Calendar),
     *                              corresponding to Julian Day of non-negative value
     * @return                      Julian Day
     * @see                         " Meeus 1998: 7.1, p. 61
     */
    public static double normalCalendarToJulianDay(NormalCalendarPoint normalCalendarPoint) {
        int y = normalCalendarPoint.y;
        int m = normalCalendarPoint.m;
        if (m <= 2) {
            m += 12;
            y--;
        }

        double b = 0.0;
        if (normalCalendarPoint.getLeapRules() == LeapRules.GREGORIAN) {
            double a = Math.floor(y / 100.0);
            b = 2.0 - a + Math.floor(a / 4.0);
        }

        return Math.floor(JULIAN_YEAR_DAYS * (y + 4716))
            + Math.floor(JD_MONTH_FACTOR * (m + 1))
            + normalCalendarPoint.dt
            + b
            - 1524.5;
    }

    /**
     * Converts Julian Day to the main calendar.
     * Implementation of the algorithm given by Meeus.
     *
     * @param jd    Julian Day to convert, non-negative
     * @return      calendar point (of the main calendar)
     * @see         " Meeus 1998, Ch. 7, p. 63
     */
    public static CalendarPoint julianDayToCalendar(double jd) {
        return (CalendarPoint) julianDayToNormalCalendar(jd, CalendarPoint.CALENDAR);
    }

    /**
     * Converts Julian Day to the proleptic Gregorian calendar.
     * Based on the algorithm given by Meeus, slightly modified to support this calendar.
     *
     * @param jd    Julian Day to convert, non-negative
     * @return      proleptic Gregorian calendar point
     * @see         " Meeus 1998, Ch. 7, p. 63
     */
    public static ProlepticGregorianCalendarPoint julianDayToProlepticGregorianCalendar(double jd) {
        return (ProlepticGregorianCalendarPoint) julianDayToNormalCalendar(jd, ProlepticGregorianCalendarPoint.CALENDAR);
    }

    /**
     * Converts Julian Day to the proleptic Julian calendar.
     * Based on the algorithm given by Meeus, slightly modified to support this calendar.
     *
     * @param jd    Julian Day to convert, non-negative
     * @return      proleptic Julian calendar point
     * @see         " Meeus 1998, Ch. 7, p. 63
     */
    public static ProlepticJulianCalendarPoint julianDayToProlepticJulianCalendar(double jd) {
        return (ProlepticJulianCalendarPoint) julianDayToNormalCalendar(jd, ProlepticJulianCalendarPoint.CALENDAR);
    }

    /**
     * Converts Julian Day to the requested calendar (based formally on the Julian calendar).
     * Based on the algorithm given by Meeus, slightly modified to support calendar variation.
     *
     * @param jd                Julian Day to convert, non-negative
     * @param targetCalendar    object representing the calendar to convert to
     *                          (based formally on the Julian calendar)
     * @return                  calendar point in the requested calendar
     * @see                     " Meeus 1998, Ch. 7, p. 63
     */
    protected static NormalCalendarPoint julianDayToNormalCalendar(double jd, NormalCalendar targetCalendar) {
        double jdPlusHalf = jd + 0.5;
        double z = Math.floor(jdPlusHalf);
        double f = jdPlusHalf - z;
        double a;
        boolean julianCalendarRules = targetCalendar.areLeapRulesConstant() ?
            targetCalendar.getMainLeapRules() == LeapRules.JULIAN
            : z < GREGORIAN_CALENDAR_START_DAY_NOON_JD;
        if (julianCalendarRules) {
            a = z;
        } else {
            double alpha = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1.0 + alpha - Math.floor(alpha / 4);
        }
        double b = a + 1524.0;
        double c = Math.floor((b - 122.1) / JULIAN_YEAR_DAYS);
        double d = Math.floor(JULIAN_YEAR_DAYS * c);
        double e = Math.floor((b - d) / JD_MONTH_FACTOR);

        double dt = b - d - Math.floor(JD_MONTH_FACTOR * e) + f;
        int m = (int) (e - (e < 14 ? 1.0 : 13.0));
        int y = (int) (c - (m > 2 ? 4716 : 4715));

        return targetCalendar.point(y, m, dt);
    }

    /**
     * Converts Julian Day to {@link TimelinePoint#toMillenialTau() millenial τ} (tau).
     *
     * @param jd    Julian Day to convert
     * @return      {@link TimelinePoint#toMillenialTau() millenial τ} (tau)
     * @see         " Meeus 1998: 32.1, p. 218
     */
    public static double julianDayToMillenialTau(double jd) {
        return (jd - EPOCH_2000_JD) / JULIAN_MILLENIUM_DAYS;
    }

    /**
     * Converts {@link TimelinePoint#toMillenialTau() millenial τ} (tau) to Julian Day.
     *
     * @param millenialTau  {@link TimelinePoint#toMillenialTau() millenial τ} (tau) to convert
     * @return              Julian Day
     * @see                 #julianDayToMillenialTau(double)
     */
    public static double millenialTauToJulianDay(double millenialTau) {
        return millenialTau * JULIAN_MILLENIUM_DAYS + EPOCH_2000_JD;
    }

    /**
     * Converts Julian Day to {@link TimelinePoint#toCenturialT() centurial T}.
     *
     * @param jd    Julian Day to convert
     * @return      {@link TimelinePoint#toCenturialT() centurial T}
     * @see         " Meeus 1998: 22.1, p. 143
     */
    public static double julianDayToCenturialT(double jd) {
        return (jd - EPOCH_2000_JD) / JULIAN_CENTURY_DAYS;
    }

    /**
     * Converts {@link TimelinePoint#toCenturialT() centurial T} to Julian Day.
     *
     * @param centurialT    {@link TimelinePoint#toCenturialT() centurial T} to convert
     * @return              Julian Day
     * @see                 #julianDayToCenturialT(double)
     */
    public static double centurialTToJulianDay(double centurialT) {
        return centurialT * JULIAN_CENTURY_DAYS + EPOCH_2000_JD;
    }

    /**
     * Converts {@link TimelinePoint#toMillenialTau() millenial τ} (tau)
     * to {@link TimelinePoint#toCenturialT() centurial T}.
     *
     * @param tau   {@link TimelinePoint#toMillenialTau() millenial τ} (tau) to convert
     * @return      {@link TimelinePoint#toCenturialT() centurial T}
     */
    public static double millenialTauToCenturialT(double tau) {
        return 10.0 * tau;
    }

    /**
     * Converts {@link TimelinePoint#toCenturialT() centurial T}
     * to {@link TimelinePoint#toMillenialTau() millenial τ} (tau).
     *
     * @param cT    {@link TimelinePoint#toCenturialT() centurial T} to convert
     * @return      {@link TimelinePoint#toMillenialTau() millenial τ} (tau)
     */
    public static double centurialTToMillenialTau(double cT) {
        return 0.1 * cT;
    }

    /**
     * Gets the equivalence unit - the frame size indicating the timeline's division
     * such that all points belonging to the same frame are considered equal
     * in the terms of {@link #equals(Object) equivalence checks} and {@link Comparable comparisons}.
     *
     * Equivalence-checking, comparing and ordering performed on instances of {@link TimelinePoint}
     * and {@link NormalCalendaricExpression} (and their dependants in these regards)
     * depend on this setting.
     *
     * Given in days.
     *
     * @return  the equivalence unit - the frame size indicating the timeline's division
     *          such that all points belonging to the same frame are considered equal
     *          in the terms of {@link #equals(Object) equivalence checks} and {@link Comparable comparisons},
     *          in days
     * @see     #DEFAULT_EQUIV_UNIT_DAYS
     */
    public static double getEquivUnitDays() {
        return equivUnitDays;
    }

    /**
     * Sets the {@link #getEquivUnitDays() equivalence unit}.
     *
     * Affects {@link #equals(Object) equivalence-checking}, {@link Comparable comparing} and ordering
     * performed on instances of {@link TimelinePoint} and {@link NormalCalendaricExpression}
     * (and their dependants in these regards).
     *
     * @param equivUnitDays     new value of {@link #getEquivUnitDays() equivalence unit}, in days,
     *                          not lesser than {@link #MIN_EQUIV_UNIT_DAYS}
     * @see                     #DEFAULT_EQUIV_UNIT_DAYS
     */
    public static void setEquivUnit(double equivUnitDays) {
        validateEquivUnitDays(equivUnitDays);
        Timeline.equivUnitDays = equivUnitDays;
    }

    /**
     * Generates a {@link #hashCode() hash code} identifying the {@link #getEquivUnitDays() equivalence unit}
     * of the given time value.
     *
     * @param days  time value to identify, in days
     * @return      {@link #hashCode() hash code} identifying the {@link #getEquivUnitDays() equivalence unit}
     *              of the given time value
     */
    public static int equivUnitHashCode(double days) {
        return Double.hashCode(roundToEquivUnit(days));
    }

    /**
     * Determines whether two given time values are equal, i.e.
     * whether they belong to the same {@link #getEquivUnitDays() equivalence unit}.
     *
     * @param days1     time value, in days
     * @param days2     another time value, in days
     * @return          {@code true} - if the given time values are equal (when they belong
     *                  to the same {@link #getEquivUnitDays() equivalence unit}); {@code false} - otherwise
     */
    public static boolean equal(double days1, double days2) {
        return Calcs.equal(roundToEquivUnit(days1), roundToEquivUnit(days2), Calcs.EPSILON);
    }

    /**
     * Compares two given time values, applying the {@link #getEquivUnitDays() equivalence unit}.
     * Consistent with {@link #equal(double, double)} (this method returns 0
     * iff that method returns {@code true}).
     *
     * @param days1     time value, in days
     * @param days2     another time value, in days
     * @return          result of comparison applying the {@link #getEquivUnitDays() equivalence unit},
     *                  in the {@link java.util.Comparator#compare(Object, Object) Comparator's format}
     */
    public static int compare(double days1, double days2) {
        return Calcs.compare(roundToEquivUnit(days1), roundToEquivUnit(days2), Calcs.EPSILON);
    }

    protected static double roundToEquivUnit(double days) {
        return Calcs.roundToDelta(days, equivUnitDays);
    }

    protected static void validateJulianDayVersusSupportedScope(double julianDay) {
        if (julianDay < 0.0 || julianDay > JULIAN_PERIOD_END_JD)
            throw new JulianDayOutOfPeriodException(julianDay);
    }

    private static void validateEquivUnitDays(double idUnitDays) {
        if (idUnitDays < MIN_EQUIV_UNIT_DAYS)
            throw new EquivUnitTooSmallException(idUnitDays, MIN_EQUIV_UNIT_DAYS);
    }
}
