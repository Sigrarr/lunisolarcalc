package com.github.sigrarr.lunisolarcalc.time;

public enum TimeType {

    DYNAMICAL(1),
    UNIVERSAL(-1);

    public final int deltaTAddendSign;
    private TimeType other;

    private TimeType(int deltaTAddendSign) {
        this.deltaTAddendSign = deltaTAddendSign;
    }

    public TimeType getOther() {
        return other;
    }

    static {
        DYNAMICAL.other = UNIVERSAL;
        UNIVERSAL.other = DYNAMICAL;
    }
}
