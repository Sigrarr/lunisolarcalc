package com.github.sigrarr.lunisolarcalc.time.timeline;

import com.github.sigrarr.lunisolarcalc.time.Timeline;

public class JulianDayOutOfPeriodException extends IllegalArgumentException {

    private final double julianDay;

    public JulianDayOutOfPeriodException(double julianDay) {
        super(
            "Only the current Julian Period is supported:"
            + " [" + String.format("%1f", Timeline.JULIAN_PERIOD_START_JD) + "; " + String.format("%1f", Timeline.JULIAN_PERIOD_END_JD) + "]"
            + ", " + julianDay + " given."
        );
        this.julianDay = julianDay;
    }

    public double getJulianDay() {
        return julianDay;
    }
}
