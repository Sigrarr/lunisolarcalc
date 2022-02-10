package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.tables.DeltaTTable;

public class Time {

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
}
