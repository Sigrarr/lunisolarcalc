package com.github.sigrarr.lunisolarcalc.time.exceptions;

public class InvalidCalendarTimeException extends IllegalArgumentException {

    private final int h;
    private final int min;
    private final int s;

    public InvalidCalendarTimeException(int h, int min, int s) {
        super(
            "Invalid time setting for a calendar point: "
            + String.format("%02d:%02d:%02d", h, min, s)
            + " [hours, minutes, seconds]. Should be: [0-23]:[0-59]:[0-59]."
        );
        this.h = h;
        this.min = min;
        this.s = s;
    }

    public int getHours() {
        return h;
    }

    public int getMinutes() {
        return min;
    }

    public int getSeconds() {
        return s;
    }
}
