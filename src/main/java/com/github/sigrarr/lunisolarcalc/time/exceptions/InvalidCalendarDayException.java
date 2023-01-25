package com.github.sigrarr.lunisolarcalc.time.exceptions;

public class InvalidCalendarDayException extends IllegalArgumentException {

    private final double dt;

    public InvalidCalendarDayException(double dt, int maxDay, int month) {
        super(
            "A day number of the month number " + month
            + " should be between 1 and " + maxDay + ", " + ((int) Math.floor(dt)) + " given"
            + " (day-with-time setting: " + dt + ")."
        );
        this.dt = dt;
    }

    public double getDt() {
        return dt;
    }
}
