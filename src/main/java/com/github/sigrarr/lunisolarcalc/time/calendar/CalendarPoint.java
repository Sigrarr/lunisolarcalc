package com.github.sigrarr.lunisolarcalc.time.calendar;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianGregorianCalendarSkippedDateException;

/**
 * A date with time in a normalized proleptic version of the actual calendar used in Europe (Rome);
 * i.e. the Julian calendar up to +1582-10-04 and the Gregorian calendar from +1582-10-15 on
 * (with the deletion of dates inbetween).
 *
 * This is the Luni-Solar Calc's main and default calendar class.
 *
 * Despite historical variation, this representation generalizes the Gregorian reform
 * as it was proceeded in Rome (and in territories that adopted it without delay) and inhertis traits
 * specified by {@link NormalCalendarPoint}. The prolepsis mentioned above means that this model
 * supports dates from before the historical introduction of the Julian calendar in -45 (46 BCE).
 *
 * Thanks to model similarities, conversion between this class and {@link java.util.GregorianCalendar}
 * is supported.
 *
 * @see com.github.sigrarr.lunisolarcalc.time.TimelinePoint
 */
public final class CalendarPoint extends NormalCalendarPoint implements Comparable<CalendarPoint> {

    public static final NormalCalendar CALENDAR = new NormalCalendar() {
        @Override public String getTitle() {
            return "Julian/Gregorian calendar";
        }

        @Override public LeapRules getMainLeapRules() {
            return LeapRules.GREGORIAN;
        }

        @Override public boolean areLeapRulesConstant() {
            return false;
        }

        @Override public LeapRules getLeapRules(NormalCalendaricExpression date) {
            return NOMINAL_COMPARATOR.compare(date, GREGORIAN_RULES_START) < 0 ? LeapRules.JULIAN : LeapRules.GREGORIAN;
        }

        @Override public void validateSpecifically(NormalCalendarPoint date) {
            if (date.y == REFORM_Y && date.m == REFORM_M && date.getDay() > JULIAN_END_D && date.getDay() < GREGORIAN_START_D)
                throw new JulianGregorianCalendarSkippedDateException(date.getDay());
        }

        @Override public CalendarPoint point(int y, int m, double dt) {
            return new CalendarPoint(y, m, dt);
        }
    };

    protected static final int REFORM_Y = 1582;
    protected static final int REFORM_M = 10;
    protected static final int GREGORIAN_START_D = 15;
    protected static final int JULIAN_END_D = 4;

    /**
     * The date of introduction of the Gregorian calendar (+1582-10-15 00:00).
     */
    public static final CalendarPoint GREGORIAN_RULES_START = new CalendarPoint(REFORM_Y, REFORM_M, GREGORIAN_START_D);
    /**
     * The day (24 hours) before the {@link GREGORIAN_RULES_START introduction of the Gregorian calendar}.
     */
    public static final CalendarPoint JULIAN_RULES_END_DATE_MIDNIGHT = new CalendarPoint(REFORM_Y, REFORM_M, JULIAN_END_D);
    private static final Date LEGACY_GREGORIAN_CHANGE_DATE = GREGORIAN_RULES_START.toLegacyGregorianCalendar().getGregorianChange();

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, double)
     */
    public CalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int)
     */
    public CalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int, int, int, int)
     */
    public CalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    /**
     * Constructs an instance representing the current moment
     * (in {@link com.github.sigrarr.lunisolarcalc.time.TimeScale#UNIVERSAL UT}).
     *
     * @return  an instance representing the current moment
     *          (in {@link com.github.sigrarr.lunisolarcalc.time.TimeScale#UNIVERSAL UT})
     */
    public static CalendarPoint ofNow() {
        return ofLegacyGregorianCalendar(new GregorianCalendar());
    }

    /**
     * Constructs an instance with a given {@link GregorianCalendar} object.
     *
     * Note that the new calendar point won't keep the time zone
     * of the passed GregorianCalendar object, nor reflect its custom
     * {@link GregorianCalendar#getGregorianChange() Gregorian change} setting.
     *
     * @param own     {@link GregorianCalendar} object
     * @return                      corresponding instance of calendar point
     */
    public static CalendarPoint ofLegacyGregorianCalendar(GregorianCalendar gregorianCalendar) {
        GregorianCalendar ownLegacyObject = (GregorianCalendar) gregorianCalendar.clone();
        ownLegacyObject.setTimeZone(TIME_ZONE);
        ownLegacyObject.setGregorianChange(LEGACY_GREGORIAN_CHANGE_DATE);

        int y = ownLegacyObject.get(GregorianCalendar.YEAR);
        return new CalendarPoint(
            ownLegacyObject.get(GregorianCalendar.ERA) == GregorianCalendar.AD ? y : 1 - y,
            ownLegacyObject.get(GregorianCalendar.MONTH) + 1,
            ownLegacyObject.get(GregorianCalendar.DAY_OF_MONTH),
            ownLegacyObject.get(GregorianCalendar.HOUR_OF_DAY),
            ownLegacyObject.get(GregorianCalendar.MINUTE),
            ownLegacyObject.get(GregorianCalendar.SECOND)
        );
    }

    /**
     * Converts this calendar point to the {@link GregorianCalendar} object.
     * The new object's time zone will be set to "Universal".
     *
     * @return  corresponding {@link GregorianCalendar} object
     *          (with the "Universal" time zone)
     */
    public GregorianCalendar toLegacyGregorianCalendar() {
        GregorianCalendar legacyObject = new GregorianCalendar(TIME_ZONE);
        legacyObject.set(GregorianCalendar.SECOND, getSeconds());
        legacyObject.set(GregorianCalendar.MINUTE, getMinutes());
        legacyObject.set(GregorianCalendar.HOUR_OF_DAY, getHours());
        legacyObject.set(GregorianCalendar.DAY_OF_MONTH, getDay());
        legacyObject.set(GregorianCalendar.MONTH, m - 1);
        legacyObject.set(GregorianCalendar.YEAR, y);
        return legacyObject;
    }

    @Override
    public NormalCalendar getCalendar() {
        return CALENDAR;
    }

    /**
     * Compares this calendar point to the other chronologically, applying the
     * {@link com.github.sigrarr.lunisolarcalc.time.Timeline#getEquivUnitDays() timeline's equivalence unit},
     * with the caveat that points of different months will never be equated.
     *
     * {@link Comparable Consistent} with {@link #equals(Object) equivalence-check}.
     *
     * To compare points of time regardless of a calendar month,
     * use {@link com.github.sigrarr.lunisolarcalc.time.TimelinePoint TimelinePoint}.
     *
     * @param point     calendar point to compare to
     * @return          result of chronological comparison applying the
     *                  {@link com.github.sigrarr.lunisolarcalc.time.Timeline#getEquivUnitDays() equivalence unit}
     *                  (with the month-difference caveat;
     *                  in the {@link Comparable#compareTo(Object) parent interface's} format)
     */
    @Override
    public int compareTo(CalendarPoint point) {
        return NOMINAL_COMPARATOR.compare(this, point);
    }
}
