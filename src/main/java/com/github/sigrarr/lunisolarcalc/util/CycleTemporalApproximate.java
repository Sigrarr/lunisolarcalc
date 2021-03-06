package com.github.sigrarr.lunisolarcalc.util;

public interface CycleTemporalApproximate {

    public double getLengthSeconds();

    public default double getLengthDays() {
        return getLengthSeconds() * Calcs.SECOND_TO_DAY;
    }

    public default double radiansPerTimeSeconds(int seconds) {
        return (seconds / getLengthSeconds()) * Calcs.ROUND;
    }

    public default double degreesPerTimeSeconds(int seconds) {
        return (seconds / getLengthSeconds()) * 360;
    }

}
