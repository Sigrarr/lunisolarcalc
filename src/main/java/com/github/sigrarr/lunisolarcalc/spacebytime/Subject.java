package com.github.sigrarr.lunisolarcalc.spacebytime;

/**
 * Subject of calculation supported by this package; provided quantity.
 *
 * This enum class is fit for being the main generic parameter of
 * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer compositions},
 * as in {@link SpaceByTimeCalcCompositions}.
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
     * Apparent longitude of the center of the Moon (λ + Δψ).
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
     * Distance between the centers of Earth and Moon (Δ).
     * Presumably in kilometers.
     *
     * @see MoonEarthDistanceCalculator
     */
    MOON_EARTH_DISTANCE,

    /**
     * Latitude of the Moon's center (β). Presumably in radians.
     *
     * @see MoonLatitudeCalculator
     */
    MOON_LATITUDE,

    /**
     * Longitude of the Moon's center (λ). Presumably in radians.
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
     * The Sun's longitude with correction due to aberration. Presumably in radians.
     *
     * @see SunAberratedLongitudeCalculator
     */
    SUN_ABERRATED_LONGITUDE,

    /**
     * The Sun's apparent longitude (λ) (indicator of Equinoxes/Solstices).
     * Presumably in radians.
     *
     * @see SunApparentLongitudeCalculator
     */
    SUN_APPARENT_LONGITUDE,

    /**
     * The Sun's geometric longitude (☉). Presumably in radians.
     *
     * @see SunGeometricLongitudeCalculator
     */
    SUN_GEOMETRIC_LONGITUDE,

    /**
     * The Sun's geometric latitude (β). Presumably in radians.
     *
     * @see SunLatitudeCalculator
     */
    SUN_LATITUDE,
}
