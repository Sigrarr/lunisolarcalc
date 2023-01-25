package com.github.sigrarr.lunisolarcalc.time.calendar;

import java.util.function.IntPredicate;

import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * Rules for testing whether a given calendar year is a leap year
 * or a common year, in a calendar based formally on the Julian Calendar.
 */
public enum LeapRules implements IntPredicate, Titled {

    JULIAN("Julian") {
        @Override public boolean test(int calendarYear) {
            return calendarYear % 4 == 0;
        }
    },

    GREGORIAN("Gregorian") {
        @Override public boolean test(int calendarYear) {
            return JULIAN.test(calendarYear) && (
                calendarYear % 100 != 0 || calendarYear % 400 == 0
            );
        }
    };

    private final String title;

    private LeapRules(String title) {
        this.title = title;
    }

    /**
     * Tests whether a requested calendar year is a leap year.
     *
     * @param calendarYear  calendar year
     * @return              {@code true} - if the year is leap, {@code false} - if common
     */
    abstract public boolean test(int calendarYear);

    @Override
    public String getTitle() {
        return title;
    }
}
