package com.github.sigrarr.lunisolarcalc.time;

public class Time {

    public static interface DeltaTProvider {
        public int getDeltaT(int romanYear);
    }

    private static final double SECONDS_TO_DAYS = 1.0 / 3600.0 / 24.0;

    protected static DeltaTProvider deltaTProvider = new DeltaTResolver();

    public static void setDeltaTProvider(DeltaTProvider deltaTProvider) {
        Time.deltaTProvider = deltaTProvider;
    }

    public static double timeToDays(int h, int min, int s) {
        return SECONDS_TO_DAYS * (s + (60.0 * (min + (60.0 * h))));
    }

    public static int getDeltaTSeconds(int romanYear) {
        return deltaTProvider.getDeltaT(romanYear);
    }

    public static int dynamicToUniversalSeconds(int tdSeconds, int romanYear) {
        return tdSeconds - getDeltaTSeconds(romanYear);
    }

    public static int universalToDynamicSeconds(int utSeconds, int romanYear) {
        return utSeconds + getDeltaTSeconds(romanYear);
    }
}
