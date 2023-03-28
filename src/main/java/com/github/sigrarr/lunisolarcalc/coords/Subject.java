package com.github.sigrarr.lunisolarcalc.coords;

/**
 * Subject of calculation supported by this package; provided quantity.
 *
 * This enum class is fit for being the main generic parameter of
 * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer compositions},
 * as in {@link CoordsCalcCompositions}.
 */
public enum Subject {
    /**
     * Aberration of the Sun's geocentric position (caused by the Earth's motion).
     * Presumably in radians.
     *
     * @see AberrationEarthSunCalculator
     */
    ABERRATION_EARTH_SUN,

    /**
     * The Earth's heliocentric latitude (B). Presumably in radians.
     *
     * @see EarthLatitudeCalculator
     */
    EARTH_LATITUDE,

    /**
     * The Earth's heliocentric longitude (L). Presumably in radians.
     *
     * @see EarthLongitudeCalculator
     */
    EARTH_LONGITUDE,

    /**
     * @see EarthNutuationElements
     */
    EARTH_NUTUATION_ELEMENTS,

    /**
     * The Earth's nutuation in longitude (Δψ). Presumably in radians.
     *
     * @see EarthNutuationInLongitudeCalculator
     */
    EARTH_NUTUATION_IN_LONGITUDE,

    /**
     * The Earht's nutuation in obliquity (Δε). Presumably in radians.
     *
     * @see EarthNutuationInObliquityCalculator
     */
    EARTH_NUTUATION_IN_OBLIQUITY,

    /**
     * The Earth's radius vector (distance to the Sun; R).
     * Presumably in Astronomical Units.
     *
     * @see EarthSunRadiusCalculator
     */
    EARTH_SUN_RADIUS,

    /**
     * The mean obliquity of the ecliptic (the mean angle between the ecliptic
     * and the celestial equator; ε0). Presumably in radians.
     *
     * @see EclipticMeanObliquityCalculator
     */
    ECLIPTIC_MEAN_OBLIQUITY,

    /**
     * The true obliquity of the ecliptic (the angle between the ecliptic
     * and the celestial equator; ε = ε0 + Δε). Presumably in radians.
     *
     * @see EclipticTrueObliquityCalculator
     */
    ECLIPTIC_TRUE_OBLIQUITY,

    /**
     * Apparent ecliptical longitude of the center of the Moon (λ).
     * Presumably in radians.
     *
     * @see MoonApparentLongitudeCalculator
     */
    MOON_APPARENT_LONGITUDE,

    /**
     * @see MoonCoordinateElements
     */
    MOON_COORDINATE_ELEMENTS,

    /**
     * The Moon's declination (δ).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see MoonDeclinationCalculator
     */
    MOON_DECLINATION,

    /**
     * Distance between the centers of the Moon and the Earth (Δ).
     * Presumably in kilometers.
     *
     * @see MoonEarthDistanceCalculator
     */
    MOON_EARTH_DISTANCE,

    /**
     * The Moon's equatorial horizontal parallax (π).
     * Presumably in radians.
     *
     * @see MoonEquatorialHorizontalParallaxCalculator
     */
    MOON_EQUATORIAL_HORIZONTAL_PARALLAX,

    /**
     * The Moon's apparent hour angle at the Greenwich meridian (H0).
     * An equatorial coordinate. Presumably in degrees.
     *
     * @see MoonHourAngleCalculator
     */
    MOON_HOUR_ANGLE,

    /**
     * Ecliptical latitude of the Moon's center (β).
     * Presumably in radians.
     *
     * @see MoonLatitudeCalculator
     */
    MOON_LATITUDE,

    /**
     * Ecliptical longitude of the Moon's center (λ).
     * Presumably in radians.
     *
     * @see MoonLongitudeCalculator
     */
    MOON_LONGITUDE,

     /**
     * Excess of the Moon's apparent longitude over the Sun's apparent longitude
     * (indicator of phases of the Moon). Presumably in radians.
     *
     * @see MoonOverSunApparentLongitudeExcessCalculator
     */
    MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS,

    /**
     * The Moon's right ascension (α).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see MoonRightAscensionCalculator
     */
    MOON_RIGHT_ASCENSION,

    /**
     * Mean sidereal time at the Greenwich meridian (θ0).
     * Presumably in degrees (1h ~ 15°).
     *
     * @see SiderealMeanTimeCalculator
     */
    SIDEREAL_MEAN_TIME,

    /**
     * Apparent sidereal time at the Greenwich meridian (θ0).
     * Presumably in degrees (1h ~ 15°).
     *
     * @see SiderealApparentTimeCalculator
     */
    SIDEREAL_APPARENT_TIME,

    /**
     * The Sun's ecliptical longitude with correction due to aberration.
     * Presumably in radians.
     *
     * @see SunAberratedLongitudeCalculator
     */
    SUN_ABERRATED_LONGITUDE,

    /**
     * The Sun's apparent ecliptical longitude (λ) (indicator of Equinoxes/Solstices).
     * Presumably in radians.
     *
     * @see SunApparentLongitudeCalculator
     */
    SUN_APPARENT_LONGITUDE,

    /**
     * The Sun's declination (δ).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see SunDeclinationCalculator
     */
    SUN_DECLINATION,

    /**
     * The Sun's geometric ecliptical longitude (☉).
     * Presumably in radians.
     *
     * @see SunGeometricLongitudeCalculator
     */
    SUN_GEOMETRIC_LONGITUDE,

    /**
     * The Sun's apparent hour angle at the Greenwich meridian (H0).
     * An equatorial coordinate. Presumably in degrees.
     *
     * @see SunHourAngleCalculator
     */
    SUN_HOUR_ANGLE,

    /**
     * The Sun's geometric ecliptical latitude (β).
     * Presumably in radians.
     *
     * @see SunLatitudeCalculator
     */
    SUN_LATITUDE,

    /**
     * The Sun's right ascension (α).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see SunRightAscensionCalculator
     */
    SUN_RIGHT_ASCENSION,
}
