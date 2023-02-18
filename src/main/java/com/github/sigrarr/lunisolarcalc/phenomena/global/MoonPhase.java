package com.github.sigrarr.lunisolarcalc.phenomena.global;

import com.github.sigrarr.lunisolarcalc.util.*;

/**
 * A principal phase of the Moon.
 * A distinguished stage of the lunation cycle.
 * Indicated by excess of the Moon's apparent longitude over the Sun's apparent longitude.
 */
public enum MoonPhase implements Titled {

    NEW_MOON(0.0, "New Moon"),
    FIRST_QUARTER(0.25, "First Quarter"),
    FULL_MOON(0.5, "Full Moon"),
    THIRD_QUARTER(0.75, "Third Quarter");

    /**
     * The lunation's angular progress point: [0.0, 1.0).
     */
    public final double lunationFraction;
    /**
     * Excess of the Moon's apparent longitude over the Sun's apparent longitude
     * which indicates this stage, in radians.
     */
    public final double moonOverSunApparentLongitudeExcess;
    private final String title;

    private MoonPhase(double lunationFraction, String title) {
        this.lunationFraction = lunationFraction;
        this.moonOverSunApparentLongitudeExcess = lunationFraction * Calcs.TURN;
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
