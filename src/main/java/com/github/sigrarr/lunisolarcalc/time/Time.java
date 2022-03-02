package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.tables.DeltaTTable;

public class Time {

    private static final double SECONDS_TO_DAYS = 1.0 / 3600.0 / 24.0;

    protected static DeltaTResolver deltaTResolver = new DeltaTTable();

    public static void setDeltaTResolver(DeltaTResolver deltaTResolver) {
        Time.deltaTResolver = deltaTResolver;
    }

    public static double timeToDays(int h, int min, int s) {
        return SECONDS_TO_DAYS * (s + (60.0 * (min + (60.0 * h))));
    }

    public static int getDeltaTSeconds(int romanYear) {
        return deltaTResolver.getDeltaT(romanYear);
    }

    public static int dynamicToUniversalSeconds(int tdSeconds, int romanYear) {
        return tdSeconds - getDeltaTSeconds(romanYear);
    }

    public static int universalToDynamicSeconds(int utSeconds, int romanYear) {
        return utSeconds + getDeltaTSeconds(romanYear);
    }

    public static double julianDayToEphemeris(double jd, int romanYear) {
        return jd + timeToDays(0, 0, getDeltaTSeconds(romanYear));
    }

    public static double julianEphemerisDaysToUniversal(double jde, int romanYear) {
        return jde - timeToDays(0, 0, getDeltaTSeconds(romanYear));
    }

    public static double julianDayToEphemeris(double jd) {
        return julianDayToEphemeris(jd, Timeline.julianDayToRomanCalendar(jd).y);
    }

    public static double julianEphemerisDaysToUniversal(double jde) {
        return julianEphemerisDaysToUniversal(jde, Timeline.julianDayToRomanCalendar(jde).y);
    }
}
