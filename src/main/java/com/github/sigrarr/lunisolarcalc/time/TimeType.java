package com.github.sigrarr.lunisolarcalc.time;

public enum TimeType {

    DYNAMICAL(1),
    UNIVERSAL(-1);

    public final int deltaTAddendSign;

    private TimeType(int deltaTAddendSign) {
        this.deltaTAddendSign = deltaTAddendSign;
    }
}
