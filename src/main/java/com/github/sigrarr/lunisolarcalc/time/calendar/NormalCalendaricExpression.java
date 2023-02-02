package com.github.sigrarr.lunisolarcalc.time.calendar;

import java.util.Comparator;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.exceptions.*;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Time.*;

/**
 * A calendaric date-time expression compliant with the most general
 * formal characteristics of the normalized Julian calendar.
 *
 * An instance is immutable and its main purpose is to store and transfer
 * a calendaric expression rather than to operate on it.
 *
 * Note thtat Luni-Solar Calc uses {@link #y astronomical numbering of years}.
 *
 * This representation makes the following assumpsions:
 * A year is divided into 12 months, numbered from 1 to 12, with maximal lengths of
 * 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30 and 31 days respectively.
 * Days in a month are counted from 1.
 * A day is divided into 24 hours numbered from 0 to 23
 * (an hour into 60 minutes numbered 0-59, and a minute into 60 seconds numbered 0-59).
 *
 * {@link Comparable Natural ordering} of unspecified calendaric expressions is not defined,
 * but there is an available {@link #nominalComparator() comparator}.
 *
 * @see NormalCalendarPoint
 * @see TimelinePoint
 */
public class NormalCalendaricExpression {

    protected static final Integer[] MAX_MONTH_DAYS = {null, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    protected static final Comparator<NormalCalendaricExpression> NOMINAL_COMPARATOR = (a, b) -> {
        int cmpY = Integer.compare(a.y, b.y);
        if (cmpY != 0)
            return cmpY;
        int cmpM = Integer.compare(a.m, b.m);
        if (cmpM != 0)
            return cmpM;
        return Timeline.compare(a.dt, b.dt);
    };

    /**
     * The year number, in astronomical numbering (0 for 1 BCE, −1 for 2 BCE, ...).
     */
    public final int y;
    /**
     * The month code number (between 1 - for January, and 12 - for December).
     */
    public final int m;
    /**
     * The day-with-time number: the day of month number + time as a fraction
     * (e.g. 1.5 for the 1st day, 12:00).
     */
    public final double dt;

    /**
     * Constucts an instance with given year, month code and day-with-time numbers.
     *
     * @param y     {@link #y year number}
     * @param m     {@link #m month code number}
     * @param dt    {@link #dt day-with-time number} (counting from 1.0)
     */
    public NormalCalendaricExpression(int y, int m, double dt) {
        this.y = y;
        this.m = m;
        this.dt = dt;
        validateDate(this);
    }

    /**
     * Constucts an instance with given year, month code and day numbers.
     * Time will be set to 00:00.
     *
     * @param y     {@link #y year number}
     * @param m     {@link #m month code number}
     * @param d     day of month number (between 1 and the month's maximum)
     */
    public NormalCalendaricExpression(int y, int m, int d) {
        this(y, m, (double) d);
    }

    /**
     * Constucts an instance with given year, month code, day number
     * and numbers of hours, minutes and seconds.
     *
     * @param y     {@link #y year number}
     * @param m     {@link #m month code number}
     * @param d     day of month number (between 1 and the month's maximum)
     * @param h     hour of day number (0-23)
     * @param min   minute of hour number (0-59)
     * @param s     second of minute number (0-59)
     */
    public NormalCalendaricExpression(int y, int m, int d, int h, int min, int s) {
        this(y, m, (double) d + validateTimeAndConvertToDayFraction(h, min, s));
    }

    /**
     * Gets the day of month number of this date (1-31).
     *
     * @return  day of month number of this date (1-31)
     */
    public int getDay() {
        return (int) dt;
    }

    /**
     * Gets the time value of this point, a fraction of day: [0.0, 1.0).
     *
     * @return  time value of this point, a fraction of day: [0.0, 1.0)
     */
    public double getTime() {
        return dt - getDay();
    }

    /**
     * Gets the hour of day number of this point (0-23).
     *
     * @return  hour of day number of this point (0-23)
     */
    public int getHours() {
        return dayFractionToWholeHours(dt);
    }

    /**
     * Gets the minute of hour number of this point (0-59).
     *
     * @return  minute of hour number of this point (0-59)
     */
    public int getMinutes() {
        return dayFractionToWholeMinutes(dt);
    }

    /**
     * Gets the second of minute number of this point (0-59).
     *
     * @return  second of minute number of this point (0-59)
     */
    public int getSeconds() {
        return dayFractionToWholeSeconds(dt);
    }

    /**
     * Obtains the comparator of calendaric expressions ordering them
     * by nominal value (regardless of any relations with actual time points),
     * applying the {@link Timeline#getEquivUnitDays() timeline's equivalence unit},
     * with the caveat that points of different months will never be equated.
     *
     * @return      nominal comparator of calendaric expressions,
     *              applying the {@link Timeline#getEquivUnitDays() equivalence unit}
     *              (with the month-difference caveat)
     */
    public static Comparator<NormalCalendaricExpression> nominalComparator() {
        return NOMINAL_COMPARATOR;
    }

    /**
     * Represents this date textually in the format: ±YYYY-MM-DD
     * (using {@link #y astronomical year numbering}),
     * compliant with the ISO 8601 guidelines for representing
     * dates before +1582 (CE) and before the year 0 (1 BCE).
     *
     * E.g.: −0045-12-24 for the 25th December 46 BCE,
     * +1582-10-15 the 15th October 1582 (CE).
     *
     * @return  textual representation of this date
     *          in the format ±YYYY-MM-DD
     */
    public String formatDate() {
        return String.format("%c%04d-%02d-%02d",
            y < 0 ? '−' : '+',
            Math.abs(y),
            m,
            getDay()
        );
    }

    /**
     * Represents this date-time textually in the format: ±YYYY-MM-DD hh:mm
     * (for the date part, see {@link #formatDate()}), allowed by the ISO 8601
     * time-representation perscriptions. Hours of day are numbered from 0 to 23
     * (no AM/PM).
     *
     * E.g.: −0045-12-24 12:00 for the noon of the 25th December 46 BCE,
     * +1582-10-15 23:45 for the 15th October 1582 (CE), quarter to midnight.
     *
     * @return  textual representation of this date
     *          in the format ±YYYY-MM-DD hh:mm
     */
    public String formatDateTimeToMinutes() {
        return String.format("%s %02d:%02d", formatDate(), getHours(), getMinutes());
    }

    /**
     * Represents this date-time textually in the format: ±YYYY-MM-DD hh:mm:ss
     * ({@link #formatDateTimeToMinutes()} extended by seconds).
     *
     * E.g.: −0045-12-24 12:15:03 for the 25th December 46 BCE,
     * 15 minutes and 3 seconds after the noon.
     *
     * @return  textual representation of this date
     *          in the format ±YYYY-MM-DD hh:mm:ss
     */
    public String formatDateTimeToSeconds() {
        return String.format("%s:%02d", formatDateTimeToMinutes(), getSeconds());
    }

    @Override
    public String toString() {
        return String.format("(%s) [%d, %d, %f]", getClass().getSimpleName(), y, m, dt);
    }

    protected static void validateDate(NormalCalendaricExpression date) {
        if (date.m < 1 || date.m > 12)
            throw new InvalidCalendarMonthException(date.m);
        if (date.getDay() < 1 || date.getDay() > MAX_MONTH_DAYS[date.m])
            throw new InvalidCalendarDayException(date.dt, MAX_MONTH_DAYS[date.m], date.m);
    }

    protected static double validateTimeAndConvertToDayFraction(int h, int min, int s) {
        if (h < 0 || h > 23 || min < 0 || min > 59 || s < 0 || s > 59)
            throw new InvalidCalendarTimeException(h, min, s);
        return timeToDays(h, min, s);
    }
}
