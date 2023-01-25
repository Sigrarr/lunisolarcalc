package com.github.sigrarr.lunisolarcalc.time;

/**
 * Type of time, a time scale.
 * The enumeration contains only the types/scales supported by Luni-Solar Calc.
 * The scales are not just relatively shifted: they differ by the very length of a second.
 *
 * @see Time
 * @see " Meeus 1998: Ch. 10 (p. 77...)
 * @see " Morrison & Stephenson 2004
 */
public enum TimeType {
    /**
     * Dynamical Time (TD), more often called Terrestrial Time (TT).
     * Independent of the variabiliby of the Earth's rotation and so fit for astronomical calculations.
     * Julian Day measured in this scale is called Julian Ephemeris Day (JDE).
     */
    DYNAMICAL(1, "TT", "JDE"),
    /**
     * Universal Time (UT). Based on the Earth's rotation and affected by its variability.
     * The most common, civil time.
     */
    UNIVERSAL(-1, "UT", "JD");

    /**
     * +1 or -1 depending or whether you add or subtract ΔT in order to convert time to this scale.
     */
    public final int conversionDeltaTAddendSign;
    /**
     * The main abbreviation/symbol for this time scale, to be added to date/time for clarity.
     */
    public final String mainAbbreviation;
    /**
     * The abbreviation/symbol of Julian Day measured in this scale.
     */
    public final String julianDayAbbreviation;
    private TimeType other;

    private TimeType(int conversionDeltaTAddendSign, String mainAbbreviation, String julianDayAbbreviation) {
        this.conversionDeltaTAddendSign = conversionDeltaTAddendSign;
        this.mainAbbreviation = mainAbbreviation;
        this.julianDayAbbreviation = julianDayAbbreviation;
    }

    protected TimeType getOther() {
        return other;
    }

    static {
        DYNAMICAL.other = UNIVERSAL;
        UNIVERSAL.other = DYNAMICAL;
    }
}
