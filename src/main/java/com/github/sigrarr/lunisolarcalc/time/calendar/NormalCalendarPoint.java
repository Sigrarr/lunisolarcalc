package com.github.sigrarr.lunisolarcalc.time.calendar;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.exceptions.*;

/**
 * A date with time in a calendar formally based on the normalized Julian Calendar.
 *
 * This representation inherits the assumptions made by {@link NormalCalendaricExpression}
 * and extends them with the following:
 * A year is either common or leap, with the length of 365 or 366 days respectively.
 * The month number 2 has 28 days in a common year and 29 days in a leap year.
 *
 * It is not recommended to expect of this class a time precision better than 1 second.
 * This representation does not store any {@linkplain TimeScale time scale} or time zone
 * information: time values should be understood as fit for the prime meridian (0°),
 * or as "local" - disregarding time zones at all.
 *
 * @see TimelinePoint
 */
public abstract class NormalCalendarPoint extends NormalCalendaricExpression {

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Universal");
    protected static final Integer[] BASE_MONTH_DAYS = {null, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, double)
     */
    public NormalCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
        validateLeapDay(this);
        getCalendar().validateSpecifically(this);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int)
     */
    public NormalCalendarPoint(int y, int m, int d) {
        this(y, m, (double) d);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int, int, int, int)
     */
    public NormalCalendarPoint(int y, int m, int d, int h, int min, int s) {
        this(y, m, (double) d + validateTimeAndConvertToDayFraction(h, min, s));
    }

    /**
     * Gets an object representing the calendar which this point belongs to
     * (based formally on the normalized Julian calendar).
     *
     * @return  object representing the calendar this point belongs to
     *          (based formally on the normalized Julian calendar)
     */
    public abstract NormalCalendar getCalendar();

    /**
     * Gets the calendar's rules of determining leap years, proper for this date.
     *
     * @return  rules of determining leap years, proper for this date
     */
    public LeapRules getLeapRules() {
        return getCalendar().getLeapRules(this);
    }

    /**
     * Checks whether the year of this date is a leap year.
     *
     * @return  {@code true} - if the year is leap, {@code false} - if common
     */
    public boolean isYearLeap() {
        return getLeapRules().test(y);
    }

    /**
     * Gets the number of days in the year of this date (365 or 366).
     *
     * @return  number of days in the year of this date (365 or 366)
     */
    public int getNumberOfDaysInYear() {
        return isYearLeap() ? 366 : 365;
    }

    /**
     * Gets the number of days in the month of this date (1-31).
     *
     * @return  number of days in the month of this date (1-31)
     */
    public int getNumberOfDaysInMonth() {
        return m == 2 && isYearLeap() ? BASE_MONTH_DAYS[m] + 1 : BASE_MONTH_DAYS[m];
    }

    /**
     * Gets the number of this date in the whole year,
     * regardless of months (1-366).
     *
     * @return  number of this date in the whole year,
     *          regardless of months (1-366)
     */
    public int getDayOfYear() {
        int floorMonthsSum = m > 2 && isYearLeap() ? 1 : 0;
        for (int mCode = 1; mCode < m; mCode++)
            floorMonthsSum += BASE_MONTH_DAYS[mCode];
        return floorMonthsSum + getDay();
    }

    /**
     * Equivalence check: checks whether the other object (NormalCalendarPoint)
     * is an equivalent of this, i.e. whether they represent the same date and time
     * in the same calendar. Applies the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit},
     * with the caveat that points of different months will never be equated.
     * To check equivalence of points of time regardless of a calendar month, use {@link TimelinePoint}.
     *
     * @param o     other object (NormalCalendarPoint), to check its equivalence with this
     * @return      {@code true} - if the other point and this represent the same date and time
     *              in the same calendar (with regard to the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit},
     *              with the month-difference caveat); {@code false } - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NormalCalendarPoint)) {
            return false;
        }
        NormalCalendarPoint point = (NormalCalendarPoint) o;
        return getCalendar().equals(point.getCalendar())
            && y == point.y
            && m == point.m
            && Timeline.equal(dt, point.dt);
    }

    /**
     * Equivalence check: generates a {@linkplain Object#hashCode() hash code} identifying
     * the combination of this point's {@linkplain NormalCalendar calendar type},
     * year, month and date-with-time. Applies the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit},
     * with the caveat that points of different months will never be equated.
     * To check equivalence of points of time regardless of a calendar month, use {@link TimelinePoint}.
     *
     * @return  {@linkplain Object#hashCode() hash code} identifying the combination
     *          of this point's {@linkplain NormalCalendar calendar type}, year, month and date-with-time,
     *          applying the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit}
     *          (with the month-difference caveat)
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCalendar(), y, m, Timeline.equivUnitHashCode(dt));
    }

    protected static void validateLeapDay(NormalCalendarPoint date) {
        if (date.m == 2 && date.getDay() == MAX_MONTH_DAYS[2] && !date.isYearLeap())
            throw new InvalidCalendarLeapDayException(date.getLeapRules(), date.getCalendar(), date.y);
    }
}
