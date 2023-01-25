package com.github.sigrarr.lunisolarcalc.time.calendar;

import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * Specification of a calendar based formally on the normalized Julian calendar.
 *
 * @see NormalCalendarPoint
 */
public interface NormalCalendar extends Titled {
    /**
     * Gets the "default" leap rules applied by this calendar.
     * Presumably the rules being in force today.
     *
     * @return  "default" leap rules applied by this calendar
     *          (presumably the rules being in force today)
     */
    public LeapRules getMainLeapRules();

    /**
     * Informs whether this calendar applies the same leap rules
     * for all times.
     *
     * @return  {@code true} - if this calendar applies the same
     *          leap rules for all times; {@code false} - if the leap
     *          rules applied by this calendar vary depending
     *          on the time period
     */
    public boolean areLeapRulesConstant();

    /**
     * Gets the leap rules proper for a given nominal date.
     *
     * @param nominalDate   nominal date
     * @return              leap rules proper for the given nominal date
     */
    public LeapRules getLeapRules(NormalCalendaricExpression nominalDate);

    /**
     * Validates a given calendar point versus specific rules of this calendar
     * (more specific than the general formal rules determined by {@link NormalCalendaricExpression}
     * and than confronting the point with the leap rules provided by this calendar).
     *
     * Throws an exception if those rules are violated.
     *
     * @param calendarPoint     calendar point to validate
     * @throws                  IllegalArgumentException if the calendar point violates
     *                          specific rules of this calendar
     */
    public default void validateSpecifically(NormalCalendarPoint calendarPoint) {}

    /**
     * Constructs a calendar point belonging to this calendar.
     *
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, double)
     */
    public NormalCalendarPoint makeCalendarPoint(int y, int m, double dt);
}
