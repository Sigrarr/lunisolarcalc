package com.github.sigrarr.lunisolarcalc.time.timescaledelta;

import static com.github.sigrarr.lunisolarcalc.time.timescaledelta.BasisMinus700ToPlus2000Resolver.EPOCH_1820_JD;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

class Util {

    static final double SECOND_TO_CENTURY = Calcs.SECOND_TO_DAY / Timeline.JULIAN_CENTURY_DAYS;

    static double julianDayToCenturialVector(double julianDay) {
        return (julianDay - EPOCH_1820_JD) / Timeline.JULIAN_CENTURY_DAYS;
    }

    static double yearStartToJulianDay(int claendarYear) {
        return Timeline.normalCalendarToJulianDay(new CalendarPoint(claendarYear, 1, 1.0));
    }

    static double yearStartToCenturialVector(int calendarYear) {
        return julianDayToCenturialVector(yearStartToJulianDay(calendarYear));
    }
}
