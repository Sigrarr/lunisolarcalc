package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.time.DeltaTResolverUsingTableForMinus700ToPlus2000;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class Time {

    public static interface DeltaTResolver {
        public int resolveDeltaT(int gregorianYear);
    }

    protected static DeltaTResolver deltaTResolver = new DeltaTResolverUsingTableForMinus700ToPlus2000();

    public static void setDeltaTProvider(DeltaTResolver deltaTProvider) {
        Time.deltaTResolver = deltaTProvider;
    }

    public static double timeToDays(int h, int min, int s) {
        return Calcs.SECOND_TO_DAY * (s + (60.0 * (min + (60.0 * h))));
    }

    public static int getDeltaTSeconds(int gregorianYear) {
        return deltaTResolver.resolveDeltaT(gregorianYear);
    }

    public static double getDeltaTDays(int gregorianYear) {
        return Calcs.SECOND_TO_DAY * deltaTResolver.resolveDeltaT(gregorianYear);
    }

    public static int shiftSecondsToTimeType(int seconds, TimeType timeType, int gregorianYear) {
        return seconds + (timeType.deltaTAddendSign * getDeltaTSeconds(gregorianYear));
    }

    public static double shiftDaysToTimeType(double days, TimeType timeType, int gregorianYear) {
        return days + (timeType.deltaTAddendSign * getDeltaTDays(gregorianYear));
    }

}
