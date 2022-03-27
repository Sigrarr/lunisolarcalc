package com.github.sigrarr.lunisolarcalc.time;

public enum TimeType {
    
    DYNAMICAL(1),
    UNIVERSAL(-1);

    public final int deltaTSignumForConversionTo;

    private TimeType(int deltaTSignumForConversionTo) {
        this.deltaTSignumForConversionTo = deltaTSignumForConversionTo;
    }
}
