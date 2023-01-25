package com.github.sigrarr.lunisolarcalc.time.exceptions;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

public class InvalidCalendarLeapDayException extends IllegalArgumentException {

    private final LeapRules leapRules;
    private final NormalCalendar calendarType;
    private final int year;

    public InvalidCalendarLeapDayException(LeapRules leapRules, NormalCalendar calendarType, int year) {
        super(
            "The year " + year + " of the " + leapRules.getTitle() + " calendar (" + calendarType.getTitle() + ")"
            + " is a common year, so it does not have the 29th of February."
        );
        this.leapRules = leapRules;
        this.calendarType = calendarType;
        this.year = year;
    }

    public LeapRules getLeapRules() {
        return leapRules;
    }

    public NormalCalendar getCalendarType() {
        return calendarType;
    }

    public int getYear() {
        return year;
    }
}
