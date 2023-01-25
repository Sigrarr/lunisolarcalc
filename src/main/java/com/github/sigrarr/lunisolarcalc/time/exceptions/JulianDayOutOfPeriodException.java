package com.github.sigrarr.lunisolarcalc.time.exceptions;

import com.github.sigrarr.lunisolarcalc.time.Timeline;

public class JulianDayOutOfPeriodException extends IllegalArgumentException {

    private final double julianDay;

    public JulianDayOutOfPeriodException(double julianDay) {
        super(
            "Luni-Solar Calc supports only the current Julian Period, Julian Day " + julianDay + " given."
            + " " + getCurrentJulianPeriodDescription()
        );
        this.julianDay = julianDay;
    }

    public static String getCurrentJulianPeriodDescription() {
        return "The current Julian Period is: [" + String.format("%.1f", 0.0) + "; " + String.format("%.1f", Timeline.JULIAN_PERIOD_END_JD) + ") JD"
            + " (from " + Timeline.JULIAN_PERIOD_START_UT.toCalendarPoint().formatDateTimeToMinutes()
                + ", " + Timeline.JULIAN_PERIOD_START_UT.toCalendarPoint().getLeapRules().getTitle() + " calendar"
            + "; to " + Timeline.JULIAN_PERIOD_END_UT.toCalendarPoint().formatDateTimeToMinutes()
                + ", " + Timeline.JULIAN_PERIOD_END_UT.toCalendarPoint().getLeapRules().getTitle() + " calendar"
            + ").";
    }

    public double getJulianDay() {
        return julianDay;
    }
}
