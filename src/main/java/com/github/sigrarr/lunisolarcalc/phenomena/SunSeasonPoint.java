package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * Equinox/Solstice - a beginning of an astronomical season.
 * A distinguished stage of the tropical year cycle.
 * Indicated by the Sun's apparent longitude (λ).
 */
public enum SunSeasonPoint implements Titled {

    MARCH_EQUINOX(0.0, "March Equinox", "Spring Equinox", "Autumn Equinox"),
    JUNE_SOLSTICE(0.5 * Math.PI, "June Solstice", "Summer Solstice", "Winter Solstice"),
    SEPTEMBER_EQUINOX(Math.PI, "September Equinox", "Autumn Equinox", "Spring Equinox"),
    DECEMBER_SOLSTICE(1.5 * Math.PI, "December Sosltice", "Winter Solstice", "Summer Solstice");

    /**
     * The value of the Sun's apparent longitude (λ) which indicates this stage, in radians.
     */
    public final double apparentLongitude;
    /**
     * The name relating to the Julian/Gregorian calendar, independent of localization,
     * apropriate for a wide span of time which started several centuries before the medieval period
     * and will outlast the current Julian Period.
     */
    public final String calendaricName;
    /**
     * The season-related name appropriate for the Northern Hemisphere.
     */
    public final String northernName;
    /**
     * The season-realted name appropriate for the Southern Hemisphere.
     */
    public final String southernName;

    private SunSeasonPoint(double apparentLongitude, String calendaricName, String northernName, String southernName) {
        this.apparentLongitude = apparentLongitude;
        this.calendaricName = calendaricName;
        this.northernName = northernName;
        this.southernName = southernName;
    }

    @Override
    public String getTitle() {
        return calendaricName;
    }
}
