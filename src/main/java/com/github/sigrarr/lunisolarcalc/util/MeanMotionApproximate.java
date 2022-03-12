package com.github.sigrarr.lunisolarcalc.util;

public enum MeanMotionApproximate {
    
    TROPICAL_YEAR(365.24219),
    TROPICAL_MONTH(27.3216),
    SYNODIC_MONTH(29.5306);

    public final double lengthDays;
    public final double lengthHours;
    public final double lengthMinutes;
    public final double lengthSeconds;
    public final double arcsecondsPerTimeSecond;
    public final double arcsecondsPerTimeMilisecond;

    private MeanMotionApproximate(double lengthDays) {
        this.lengthDays = lengthDays;
        lengthHours = lengthDays * 24.0;
        lengthMinutes = lengthHours * 60.0;
        lengthSeconds = lengthMinutes * 60.0;
        arcsecondsPerTimeSecond = Calcs.toArcseconds(360.0) / lengthSeconds;
        arcsecondsPerTimeMilisecond = 0.001 * arcsecondsPerTimeSecond;
    }

    public double arcsecondsPerTimeSeconds(int seconds) {
        return arcsecondsPerTimeSecond * seconds;
    }

    public double degreesPerTimeSeconds(int seconds) {
        return Calcs.arcsecondsToDegrees(arcsecondsPerTimeSeconds(seconds));
    }

    public double arcsecondsPerTimeMiliseconds(int miliseconds) {
        return arcsecondsPerTimeMilisecond * miliseconds;
    }

    public double degreesPerTimeMiliseconds(int miliseconds) {
        return Calcs.arcsecondsToDegrees(arcsecondsPerTimeMiliseconds(miliseconds));
    }
}
