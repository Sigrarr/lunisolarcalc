package com.github.sigrarr.lunisolarcalc.util;

public enum MeanMotionApproximate implements CycleTemporalApproximate {
    
    TROPICAL_YEAR(365.24219),
    TROPICAL_MONTH(27.3216),
    SYNODIC_MONTH(29.5306);

    public final double lengthDays;
    public final double lengthSeconds;
    public final double radiansPerTimeSecond;
    public final double degreesPerTimeSecond;
    public final double degreesPerTimeMilisecond;
    public final double arcsecondsPerTimeMilisecond;

    private MeanMotionApproximate(double lengthDays) {
        this.lengthDays = lengthDays;
        lengthSeconds = lengthDays * Calcs.DAY_SECONDS;
        radiansPerTimeSecond = Calcs.ROUND / lengthSeconds;
        degreesPerTimeSecond = 360.0 / lengthSeconds;
        degreesPerTimeMilisecond = 0.001 * degreesPerTimeSecond;
        arcsecondsPerTimeMilisecond = (Calcs.DEGREE_TO_ARC_SECOND * 360) / (1000.0 * lengthSeconds);
    }

    @Override
    public double getLengthSeconds() {
        return lengthSeconds;
    }

    @Override
    public double getLengthDays() {
        return lengthDays;
    }

    @Override
    public double degreesPerTimeSeconds(int seconds) {
        return seconds * degreesPerTimeSecond;
    }

    @Override
    public double radiansPerTimeSeconds(int seconds) {
        return seconds * radiansPerTimeSecond;
    }

    public double degreesPerTimeMiliseconds(int miliseconds) {
        return miliseconds * degreesPerTimeMilisecond;
    }

    public double arcsecondsPerTimeMiliseconds(int miliseconds) {
        return miliseconds * arcsecondsPerTimeMilisecond;
    }
}
