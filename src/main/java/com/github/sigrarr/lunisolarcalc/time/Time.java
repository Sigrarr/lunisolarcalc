package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.tables.DeltaTTable;

public class Time {

    private static final double SECONDS_TO_DAYS = 1.0 / 3600.0 / 24.0;

    protected static DeltaTResolver deltaTResolver = new DeltaTTable();

    public static void setDeltaTResolver(DeltaTResolver deltaTResolver) {
        Time.deltaTResolver = deltaTResolver;
    }

    public static int dynamicToUniversal(int td, int romanYear) {
        return td - deltaTResolver.getDeltaT(romanYear);
    }

    public static int universalToDynamic(int ut, int romanYear) {
        return ut + deltaTResolver.getDeltaT(romanYear);
    }

    public static double timeToDays(int h, int min, int s) {
        return SECONDS_TO_DAYS * (s + (60.0 * (min + (60.0 * h))));
    }
}
