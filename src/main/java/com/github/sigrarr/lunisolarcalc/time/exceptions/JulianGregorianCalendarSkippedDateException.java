package com.github.sigrarr.lunisolarcalc.time.exceptions;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

public class JulianGregorianCalendarSkippedDateException extends IllegalArgumentException {

    private final int day;

    public JulianGregorianCalendarSkippedDateException(int day) {
        super(
            "Cannot create the date " + formatOct1582Date(day) + " in the " + CalendarPoint.CALENDAR.getTitle() + ":"
            + " one aspect of the Gregorian reform was that "
            + CalendarPoint.JULIAN_RULES_END_DATE_MIDNIGHT.formatDate() + " (the last day of the Julian rules)"
            + " was follwed immediately by "
            + CalendarPoint.GREGORIAN_RULES_START.formatDate() + " (the first day of the Gregorian rules)."
            + " If you need the dates of this period to progress continuously,"
            + " use the " + ProlepticGregorianCalendarPoint.CALENDAR.getTitle()
            + " or the " + ProlepticJulianCalendarPoint.CALENDAR.getTitle() + "."
        );
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return CalendarPoint.GREGORIAN_RULES_START.m;
    }

    public int getYear() {
        return CalendarPoint.GREGORIAN_RULES_START.y;
    }

    private static String formatOct1582Date(int day) {
        return "+" + CalendarPoint.GREGORIAN_RULES_START.y
            + "-" + CalendarPoint.GREGORIAN_RULES_START.m
            + "-" + String.format("%02d", day);
    }
}
