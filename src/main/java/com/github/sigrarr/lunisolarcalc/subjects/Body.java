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
        GlobalCoord.MOON_HOUR_ANGLE_0,
        LocalCoord.MOON_LOCAL_HOUR_ANGLE,
        LocalCoord.MOON_ALTITUDE,
        LocalCoord.MOON_AZIMUTH,
        LocalCoord.MOON_TOPOCENTRIC_DECLINATION,
        LocalCoord.MOON_TOPOCENTRIC_RIGHT_ASCENSION,
        LocalCoord.MOON_TOPOCENTRIC_LOCAL_HOUR_ANGLE,
        LocalCoord.MOON_TOPOCENTRIC_ALTITUDE,
        LocalCoord.MOON_TOPOCENTRIC_AZIMUTH,
        GlobalCoord.MOON_PARALLAX,
        GlobalCoord.MOON_PARALLAX_SINE,
        LocalCoord.MOON_PARALLAX_IN_RIGHT_ASCENSION
    ),

    SUN(
        "Sun",
        GlobalCoord.SUN_LATITUDE,
        GlobalCoord.SUN_APPARENT_LONGITUDE,
        GlobalCoord.SUN_DECLINATION,
        GlobalCoord.SUN_RIGHT_ASCENSION,
        GlobalCoord.SUN_HOUR_ANGLE_0,
        LocalCoord.SUN_LOCAL_HOUR_ANGLE,
        LocalCoord.SUN_ALTITUDE,
        LocalCoord.SUN_AZIMUTH,
        LocalCoord.SUN_TOPOCENTRIC_DECLINATION,
        LocalCoord.SUN_TOPOCENTRIC_RIGHT_ASCENSION,
        LocalCoord.SUN_TOPOCENTRIC_LOCAL_HOUR_ANGLE,
        LocalCoord.SUN_TOPOCENTRIC_ALTITUDE,
        LocalCoord.SUN_TOPOCENTRIC_AZIMUTH,
        GlobalCoord.SUN_PARALLAX,
        GlobalCoord.SUN_PARALLAX_SINE,
        LocalCoord.SUN_PARALLAX_IN_RIGHT_ASCENSION
    );

    /**
     * Identifier of ecliptical latitude of this celestial body (β).
     *
     * @see GlobalCoord#MOON_LATITUDE
     * @see GlobalCoord#SUN_LATITUDE
     */
    public final GlobalCoord latitudeCoord;

    /**
     * Identifier of apparent ecliptical longitude of this celestial body (λ).
     *
     * @see GlobalCoord#MOON_APPARENT_LONGITUDE
     * @see GlobalCoord#SUN_APPARENT_LONGITUDE
     */
    public final GlobalCoord apparentLongitudeCoord;

    /**
     * Identifier of declination of this celestial body (δ).
     *
     * @see GlobalCoord#MOON_DECLINATION
     * @see GlobalCoord#SUN_DECLINATION
     */
    public final GlobalCoord declinationCoord;

    /**
     * Identifier of right ascension of this celestial body (α).
     *
     * @see GlobalCoord#MOON_RIGHT_ASCENSION
     * @see GlobalCoord#SUN_RIGHT_ASCENSION
     */
    public final GlobalCoord rightAscensionCoord;

    /**
     * Identifier of hour angle of this celestial body (H0).
     *
     * @see GlobalCoord#MOON_HOUR_ANGLE_0
     * @see GlobalCoord#SUN_HOUR_ANGLE_0
     */
    public final GlobalCoord hourAngle0Coord;

    /**
     * Identifier of local hour angle of this celestial body (H).
     *
     * @see LocalCoord#MOON_LOCAL_HOUR_ANGLE
     * @see LocalCoord#SUN_LOCAL_HOUR_ANGLE
     */
    public final LocalCoord localHourAngleCoord;

    /**
     * Identifier of altitude of this celestial body (h).
     *
     * @see LocalCoord#MOON_ALTITUDE
     * @see LocalCoord#SUN_ALTITUDE
     */
    public final LocalCoord altitudeCoord;

    /**
     * Identifier of azimuth of this celestial body (A).
     *
     * @see LocalCoord#MOON_AZIMUTH
     * @see LocalCoord#SUN_AZIMUTH
     */
    public final LocalCoord azimuthCoord;

    /**
     * Identifier of topocentric declination of this celestial body (δ′).
     *
     * @see LocalCoord#MOON_TOPOCENTRIC_DECLINATION
     * @see LocalCoord#SUN_TOPOCENTRIC_DECLINATION
     */
    public final LocalCoord topocentricDeclinationCoord;

    /**
     * Identifier of topocentric right ascension of this celestial body (α′).
     *
     * @see LocalCoord#MOON_TOPOCENTRIC_RIGHT_ASCENSION
     * @see LocalCoord#SUN_TOPOCENTRIC_RIGHT_ASCENSION
     */
    public final LocalCoord topocentricRightAscensionCoord;

    /**
     * Identifier of topocentric local hour angle of this celestial body (H′).
     *
     * @see LocalCoord#MOON_TOPOCENTRIC_LOCAL_HOUR_ANGLE
     * @see LocalCoord#SUN_TOPOCENTRIC_LOCAL_HOUR_ANGLE
     */
    public final LocalCoord topocentricLocalHourAngleCoord;

    /**
     * Identifier of topocentric altitude of this celestial body (h′).
     *
     * @see LocalCoord#MOON_TOPOCENTRIC_ALTITUDE
     * @see LocalCoord#SUN_TOPOCENTRIC_ALTITUDE
     */
    public final LocalCoord topocentricAltitudeCoord;

    /**
     * Identifier of topocentric azimuth of this celestial body (A′).
     *
     * @see LocalCoord#MOON_TOPOCENTRIC_AZIMUTH
     * @see LocalCoord#SUN_TOPOCENTRIC_AZIMUTH
     */
    public final LocalCoord topocentricAzimuthCoord;

    /**
     * Identifier of equatorial horizontal parallax of this celestial body (π).
     *
     * @see GlobalCoord#MOON_PARALLAX
     * @see GlobalCoord#SUN_PARALLAX
     */
    public final GlobalCoord parallaxCoord;

    /**
     * Identifier of the sine of equatorial horizontal parallax of this celestial body (sin(π)).
     *
     * @see GlobalCoord#MOON_PARALLAX_SINE
     * @see GlobalCoord#SUN_PARALLAX_SINE
     */
    public final GlobalCoord parallaxSineCoord;

    /**
     * Identifier of parallax in right ascension of this celestial body (Δα).
     *
     * @see LocalCoord#MOON_PARALLAX_IN_RIGHT_ASCENSION
     * @see LocalCoord#SUN_PARALLAX_IN_RIGHT_ASCENSION
     */
    public final LocalCoord parallaxInRightAscensionCoord;
    private final String title;

    private Body(
        String title,
        GlobalCoord latitudeCoord,
        GlobalCoord apparentLongitudeCoord,
        GlobalCoord declinationCoord,
        GlobalCoord rightAscensionCoord,
        GlobalCoord hourAngleCoord,
        LocalCoord localHourAngleCoord,
        LocalCoord altitudeCoord,
        LocalCoord azimuthCoord,
        LocalCoord topocentricDeclinationCoord,
        LocalCoord topocentricRightAscensionCoord,
        LocalCoord topocentricLocalHourAngleCoord,
        LocalCoord topocentricAltitudeCoord,
        LocalCoord topocentricAzimuthCoord,
        GlobalCoord parallaxCoord,
        GlobalCoord parallaxSineCoord,
        LocalCoord parallaxInRightAscensionCoord
    ) {
        this.title = title;
        this.apparentLongitudeCoord = apparentLongitudeCoord;
        this.latitudeCoord = latitudeCoord;
        this.declinationCoord = declinationCoord;
        this.rightAscensionCoord = rightAscensionCoord;
        this.hourAngle0Coord = hourAngleCoord;
        this.localHourAngleCoord = localHourAngleCoord;
        this.altitudeCoord = altitudeCoord;
        this.azimuthCoord = azimuthCoord;
        this.topocentricDeclinationCoord = topocentricDeclinationCoord;
        this.topocentricRightAscensionCoord = topocentricRightAscensionCoord;
        this.topocentricLocalHourAngleCoord = topocentricLocalHourAngleCoord;
        this.topocentricAltitudeCoord = topocentricAltitudeCoord;
        this.topocentricAzimuthCoord = topocentricAzimuthCoord;
        this.parallaxCoord = parallaxCoord;
        this.parallaxSineCoord = parallaxSineCoord;
        this.parallaxInRightAscensionCoord = parallaxInRightAscensionCoord;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
