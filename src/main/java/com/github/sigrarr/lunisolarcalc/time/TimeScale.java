package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * A time scale, type of time.
 * The enumeration contains only the scales supported by Luni-Solar Calc.
 * The scales are not just relatively shifted: they differ by the very length of a second.
 *
 * @see TimeScaleDelta
 * @see " Meeus 1998: Ch. 10 (p. 77...)
 * @see " Morrison & Stephenson 2004
 */
public enum TimeScale implements Titled {
    /**
     * Dynamical Time (TD in Meeus 1998; TT here).
     * Independent of the variabiliby of the Earth's rotation and so fit for astronomical calculations.
     * Julian Day measured in this scale is called Julian Ephemeris Day (JDE).
     *
     * The difference between Barycentric D.T. (TDB) and Terrestrial D.T. (TDT) is so small
     * that distinguishing between them in Luni-Solar Calc would be inadequate to the project's simplicity.
     * However, some components work on the assumption that this is the Terrestrial Time (TT).
     */
    DYNAMICAL("Dynamical Time", "TT", "JDE"),
    /**
     * Universal Time (UT).
     * Based on the Earth's rotation and affected by its variability.
     * The most common, civil time.
     */
    UNIVERSAL("Universal Time", "UT", "JD");

    /**
     * The main abbreviation/symbol for this time scale, to be added to date/time for clarity.
     */
    public final String mainAbbreviation;
    /**
     * The abbreviation/symbol of Julian Day measured in this scale.
     */
    public final String julianDayAbbreviation;
    private final String title;

    private TimeScale(String title, String mainAbbreviation, String julianDayAbbreviation) {
        this.title = title;
        this.mainAbbreviation = mainAbbreviation;
        this.julianDayAbbreviation = julianDayAbbreviation;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
