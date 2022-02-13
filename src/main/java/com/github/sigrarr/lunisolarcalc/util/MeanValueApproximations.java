package com.github.sigrarr.lunisolarcalc.util;

public class MeanValueApproximations {
    
    public static final double TROPICAL_YEAR_MEAN_DAYS = 365.24219;
    public static final double TROPICAL_YEAR_MEAN_HOURS = TROPICAL_YEAR_MEAN_DAYS * 24.0;
    public static final double TROPICAL_YEAR_MEAN_MINUTES = TROPICAL_YEAR_MEAN_HOURS * 60.0;
    public static final double TROPICAL_YEAR_MEAN_SECONDS = TROPICAL_YEAR_MEAN_MINUTES * 60.0;

    public static class SunEarthRelativeMotion {
        public static double ARCECONDS_PER_TIME_SECOND = Calcs.toArcseconds(360.0) / TROPICAL_YEAR_MEAN_SECONDS;
        public static double ARCECONDS_PER_TIME_MILISECOND = 0.001 * ARCECONDS_PER_TIME_SECOND;
        public static double arcsecondsPerTimeMiliseconds(int miliseconds) {
            return ARCECONDS_PER_TIME_MILISECOND * miliseconds;
        }
        public static double degreesPerTimeMiliseconds(int miliseconds) {
            return Calcs.arcsecondsToDegrees(arcsecondsPerTimeMiliseconds(miliseconds));
        }
    }
}
