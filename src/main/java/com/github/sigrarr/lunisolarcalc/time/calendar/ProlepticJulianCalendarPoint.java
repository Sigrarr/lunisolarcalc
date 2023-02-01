package com.github.sigrarr.lunisolarcalc.time.calendar;

/**
 * A date and time in the proleptic Julian calendar.
 *
 * This representation is normalized: inherits traits specified by {@link NormalCalendarPoint}.
 *
 * The proleptic Julian calendar ignores the Gregorian reform, applying the same rules
 * invariantly for all times, even before the historical introduction of the Julian calendar
 * in -45 (46 BCE); it is a uniform calendar, with no date gaps nor rule changes.
 */
public final class ProlepticJulianCalendarPoint extends NormalCalendarPoint implements Comparable<ProlepticJulianCalendarPoint> {

    public static final NormalCalendar CALENDAR = new NormalCalendar() {
        @Override public String getTitle() {
            return "Proleptic Julian calendar";
        }

        @Override public LeapRules getMainLeapRules() {
            return LeapRules.JULIAN;
        }

        @Override public boolean areLeapRulesConstant() {
            return true;
        }

        @Override public LeapRules getLeapRules(NormalCalendaricExpression date) {
            return LeapRules.JULIAN;
        }

        @Override public ProlepticJulianCalendarPoint point(int y, int m, double dt) {
            return new ProlepticJulianCalendarPoint(y, m, dt);
        }
    };

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, double)
     */
    public ProlepticJulianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int)
     */
    public ProlepticJulianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    /**
     * @see NormalCalendaricExpression#NormalCalendaricExpression(int, int, int, int, int, int)
     */
    public ProlepticJulianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    @Override
    public NormalCalendar getCalendar() {
        return CALENDAR;
    }

    /**
     * Compares this proleptic Julian calendar point to the other chronologically, applying the
     * {@link com.github.sigrarr.lunisolarcalc.time.Timeline#getEquivUnitDays() timeline's equivalence unit},
     * with the caveat that points of different months will never be equated.
     *
     * {@link Comparable Consistent} with {@link #equals(Object) equivalence-check}.
     *
     * To compare points of time regardless of a calendar month,
     * use {@link com.github.sigrarr.lunisolarcalc.time.TimelinePoint TimelinePoint}.
     *
     * @param point     proleptic Julian calendar point to compare to
     * @return          result of chronological comparison applying the
     *                  {@link com.github.sigrarr.lunisolarcalc.time.Timeline#getEquivUnitDays() equivalence unit}
     *                  (with the month-difference caveat,
     *                  in the {@link Comparable#compareTo(Object) parent interface's} format)
     */
    @Override
    public int compareTo(ProlepticJulianCalendarPoint point) {
        return NOMINAL_COMPARATOR.compare(this, point);
    }
}
