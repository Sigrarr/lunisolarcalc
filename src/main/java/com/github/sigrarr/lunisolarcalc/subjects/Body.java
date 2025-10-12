package com.github.sigrarr.lunisolarcalc.subjects;

import com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord;
import com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord;
import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * A celestial body supported by this project.
 */
public enum Body implements Titled {

    MOON(
        "Moon",
        GlobalCoord.MOON_LATITUDE,
        GlobalCoord.MOON_APPARENT_LONGITUDE,
        GlobalCoord.MOON_DECLINATION,
        GlobalCoord.MOON_RIGHT_ASCENSION,
        GlobalCoord.MOON_HOUR_ANGLE,
        LocalCoord.MOON_AZIMUTH,
        LocalCoord.MOON_ALTITUDE
    ),
    SUN(
        "Sun",
        GlobalCoord.SUN_LATITUDE,
        GlobalCoord.SUN_APPARENT_LONGITUDE,
        GlobalCoord.SUN_DECLINATION,
        GlobalCoord.SUN_RIGHT_ASCENSION,
        GlobalCoord.SUN_HOUR_ANGLE,
        LocalCoord.SUN_AZIMUTH,
        LocalCoord.SUN_ALTITUDE
    );

    public final GlobalCoord latitudeCoord;
    public final GlobalCoord apparentLongitudeCoord;
    public final GlobalCoord declinationCoord;
    public final GlobalCoord rightAscensionCoord;
    public final GlobalCoord hourAngleCoord;
    public final LocalCoord azimuthCoord;
    public final LocalCoord altitudeCoord;
    private final String title;

    private Body(
        String title,
        GlobalCoord latitudeCoord,
        GlobalCoord apparentLongitudeCoord,
        GlobalCoord declinationCoord,
        GlobalCoord rightAscensionCoord,
        GlobalCoord hourAngleCoord,
        LocalCoord azimuthCoord,
        LocalCoord altitudeCoord
    ) {
        this.title = title;
        this.apparentLongitudeCoord = apparentLongitudeCoord;
        this.latitudeCoord = latitudeCoord;
        this.declinationCoord = declinationCoord;
        this.rightAscensionCoord = rightAscensionCoord;
        this.hourAngleCoord = hourAngleCoord;
        this.azimuthCoord = azimuthCoord;
        this.altitudeCoord = altitudeCoord;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
