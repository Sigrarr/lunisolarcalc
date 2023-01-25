package com.github.sigrarr.lunisolarcalc.time.calendar;

import java.time.*;

/**
 * A date and time in the proleptic Gregorian calendar.
 *
 * This representation is normalized: inherits traits specified by {@link NormalCalendarPoint}.
 *
 * The proleptic Gregorian calendar extends the base one by applying its rules to the dates
 * from before its historical introduction (+1582-10-15); it is a uniform calendar,
 * with no date gaps nor rule changes.
 *
 * Thanks to model similarities, conversion between this class and
 * {@link LocalDate}/{@link LocalDateTime} is supported.
 */
public final class ProlepticGregorianCalendarPoint extends NormalCalendarPoint implements Comparable<ProlepticGregorianCalendarPoint> {

    public static final NormalCalendar CALENDAR = new NormalCalendar() {
        @Override public String getTitle() {
            return "Proleptic Gregorian calendar";
        }

        @Override public LeapRules getMainLeapRules() {
            return LeapRules.GREGORIAN;
        }

        @Override public boolean areLeapRulesConstant() {
            return true;
        }

        @Override public LeapRules getLeapRules(NormalCalendaricExpression date) {
            return LeapRules.GREGORIAN;
        }

        @Override public ProlepticGregorianCalendarPoint makeCalendarPoint(int y, int m, double dt) {
            return new ProlepticGregorianCalendarPoint(y, m, dt);
        }
    };

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, double)
     */
    public ProlepticGregorianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int)
     */
    public ProlepticGregorianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int, int, int, int)
     */
    public ProlepticGregorianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    /**
     * Constructs an instance representing the current moment
     * (in {@link com.github.sigrarr.lunisolarcalc.time.TimeType#UNIVERSAL UT}).
     *
     * @return  an instance representing the current moment
     *          (in {@link com.github.sigrarr.lunisolarcalc.time.TimeType#UNIVERSAL UT})
     */
    public static ProlepticGregorianCalendarPoint ofNow() {
        return ofLocalDateTime(LocalDateTime.now());
    }

    /**
     * Constructs an instance with a given {@link LocalDateTime} object.
     *
     * @param localDateTime     {@link LocalDateTime} object
     * @return                  corresponding instance of proleptic Gregorian calendar point
     */
    public static ProlepticGregorianCalendarPoint ofLocalDateTime(LocalDateTime localDateTime) {
        return new ProlepticGregorianCalendarPoint(
            localDateTime.getYear(),
            localDateTime.getMonthValue(),
            localDateTime.getDayOfMonth(),
            localDateTime.getHour(),
            localDateTime.getMinute(),
            localDateTime.getSecond()
        );
    }

    /**
     * Constructs an instance with a given {@link LocalDate} object.
     * Time will be set to 00:00.
     *
     * @param localDate     {@link LocalDate} object
     * @return              corresponding instance of proleptic Gregorian calendar point,
     *                      with time set to 00:00
     */
    public static ProlepticGregorianCalendarPoint ofLocalDate(LocalDate localDate) {
        return new ProlepticGregorianCalendarPoint(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    /**
     * Converts this calendar point to the {@link LocalDateTime} object.
     *
     * @return  corresponding {@link LocalDateTime} object
     */
    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(y, m, getDay(), getHours(), getMinutes(), getSeconds());
    }

    /**
     * Converts this calendar point to the {@link LocalDate} object.
     * Note that this conversion passes only the date: time information will be lost.
     *
     * @return  corresponding {@link LocalDate} object
     *          (containing only the date, with no time information)
     */
    public LocalDate toLocalDate() {
        return LocalDate.of(y, m, getDay());
    }

    @Override
    public NormalCalendar getCalendar() {
        return CALENDAR;
    }

    /**
     * Compares this proleptic Gregorian calendar point to the other chronologically, applying the
     * {@link com.github.sigrarr.lunisolarcalc.time.Timeline#getEquivUnitDays() timeline's equivalence unit},
     * with the caveat that points of different months will never be equated.
     *
     * {@link Comparable Consistent} with {@link #equals(Object) equivalence-check}.
     *
     * To compare points of time regardless of a calendar month,
     * use {@link com.github.sigrarr.lunisolarcalc.time.TimelinePoint TimelinePoint}.
     *
     * @param point     proleptic Gregorian calendar point to compare to
     * @return          result of chronological comparison applying the
     *                  {@link com.github.sigrarr.lunisolarcalc.time.Timeline#getEquivUnitDays() equivalence unit}
     *                  (with the month-difference caveat),
     *                  in the {@link Comparable#compareTo(Object) parent interface's} format
     */
    @Override
    public int compareTo(ProlepticGregorianCalendarPoint point) {
        return nominalComparator().compare(this, point);
    }
}
