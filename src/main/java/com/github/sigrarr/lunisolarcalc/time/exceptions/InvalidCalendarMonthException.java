package com.github.sigrarr.lunisolarcalc.time.exceptions;

public class InvalidCalendarMonthException extends IllegalArgumentException {

    private final int m;

    public InvalidCalendarMonthException(int m) {
        super(
            "A month code should be a number between 1 (for January) and 12 (for December),"
            + " " + m + " given."
        );
        this.m = m;
    }

    public int getMonth() {
        return m;
    }
}
