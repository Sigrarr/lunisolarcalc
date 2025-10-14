package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.function.Supplier;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.Body;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

/**
 * A spatial quantity calculable by this project, of the "global" type,
 * i.e. independent of the observer on Earth.
 *
 * This enum class is fit for being the key generic parameter of
 * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer calc. compositions},
 * as in {@link CoordsCalcCompositions}.
 *
 * Note that if you want to calculate values of these quantities together with
 * {@link com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord LocalCoords} in one composition,
 * you should use the {@link Key coords.Key} class as a generic key parameter ({@link #key()}).
 */
public enum GlobalCoord implements Key.QuantityIndetifier {
    /**
     * Aberration of the Sun's geocentric position (caused by the Earth's motion).
     * Presumably in radians.
     *
     * @see AberrationEarthSunCalculator
     */
    ABERRATION_EARTH_SUN(
        AberrationEarthSunCalculator.class
    ),

    /**
     * The Earth's heliocentric latitude (B). Presumably in radians.
     *
     * @see EarthLatitudeCalculator
     */
    EARTH_LATITUDE(
        EarthLatitudeCalculator.class
    ),

    /**
     * The Earth's heliocentric longitude (L). Presumably in radians.
     *
     * @see EarthLongitudeCalculator
     */
    EARTH_LONGITUDE(
        EarthLongitudeCalculator.class
    ),

    /**
     * @see EarthNutuationElements
     */
    EARTH_NUTUATION_ELEMENTS(
        EarthNutuationElements.class,
        EarthNutuationElements::makeUnevaluatedInstance
    ),

    /**
     * The Earth's nutuation in longitude (Δψ). Presumably in radians.
     *
     * @see EarthNutuationInLongitudeCalculator
     */
    EARTH_NUTUATION_IN_LONGITUDE(
        EarthNutuationInLongitudeCalculator.class
    ),

    /**
     * The Earht's nutuation in obliquity (Δε). Presumably in radians.
     *
     * @see EarthNutuationInObliquityCalculator
     */
    EARTH_NUTUATION_IN_OBLIQUITY(
        EarthNutuationInObliquityCalculator.class
    ),

    /**
     * The Earth's radius vector (distance to the Sun; R).
     * Presumably in Astronomical Units.
     *
     * @see EarthSunRadiusCalculator
     */
    EARTH_SUN_RADIUS(
        EarthSunRadiusCalculator.class
    ),

    /**
     * The mean obliquity of the ecliptic (the mean angle between the ecliptic
     * and the celestial equator; ε0). Presumably in radians.
     *
     * @see EclipticMeanObliquityCalculator
     */
    ECLIPTIC_MEAN_OBLIQUITY(
        EclipticMeanObliquityCalculator.class
    ),

    /**
     * The true obliquity of the ecliptic (the angle between the ecliptic
     * and the celestial equator; ε = ε0 + Δε). Presumably in radians.
     *
     * @see EclipticTrueObliquityCalculator
     */
    ECLIPTIC_TRUE_OBLIQUITY(
        EclipticTrueObliquityCalculator.class
    ),

    /**
     * Apparent ecliptical longitude of the center of the Moon (λ).
     * Presumably in radians.
     *
     * @see MoonApparentLongitudeCalculator
     */
    MOON_APPARENT_LONGITUDE(
        MoonApparentLongitudeCalculator.class
    ),

    /**
     * @see MoonCoordinateElements
     */
    MOON_COORDINATE_ELEMENTS(
        MoonCoordinateElements.class,
        MoonCoordinateElements::makeUnevaluatedInstance
    ),

    /**
     * The Moon's declination (δ).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see DeclinationCalculator
     */
    MOON_DECLINATION(
        DeclinationCalculator.class,
        () -> new DeclinationCalculator(Body.MOON)
    ),

    /**
     * Distance between the centers of the Moon and the Earth (Δ).
     * Presumably in kilometers.
     *
     * @see MoonEarthDistanceCalculator
     */
    MOON_EARTH_DISTANCE(
        MoonEarthDistanceCalculator.class
    ),

    /**
     * The Moon's apparent hour angle at the Greenwich meridian (H0).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see HourAngleCalculator
     */
    MOON_HOUR_ANGLE_0(
        HourAngleCalculator.class,
        () -> new HourAngleCalculator(Body.MOON)
    ),

    /**
     * Ecliptical latitude of the Moon's center (β).
     * Presumably in radians.
     *
     * @see MoonLatitudeCalculator
     */
    MOON_LATITUDE(
        MoonLatitudeCalculator.class
    ),

    /**
     * Ecliptical longitude of the Moon's center (λ).
     * Presumably in radians.
     *
     * @see MoonLongitudeCalculator
     */
    MOON_LONGITUDE(
        MoonLongitudeCalculator.class
    ),

    /**
     * Excess of the Moon's apparent longitude over the Sun's apparent longitude
     * (indicator of phases of the Moon). Presumably in radians.
     *
     * @see MoonOverSunApparentLongitudeExcessCalculator
     */
    MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS(
        MoonOverSunApparentLongitudeExcessCalculator.class
    ),

    /**
     * The Moon's equatorial horizontal parallax (π).
     * Presumably in radians.
     *
     * @see ParallaxCalculator
     */
    MOON_PARALLAX(
        ParallaxCalculator.class,
        () -> new ParallaxCalculator(Body.MOON)
    ),

    /**
     * The sine of
     * {@linkplain #MOON_PARALLAX the Moon's equatorial horizontal parallax}
     * (sin(π)).
     *
     * @see MoonParallaxSineCalculator
     */
    MOON_PARALLAX_SINE(
        MoonParallaxSineCalculator.class
    ),

    /**
     * The Moon's right ascension (α).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see RightAscensionCalculator
     */
    MOON_RIGHT_ASCENSION(
        RightAscensionCalculator.class,
        () -> new RightAscensionCalculator(Body.MOON)
    ),

    /**
     * The geocentric elongation of the Moon from the Sun (ψ).
     * I.e. the angular separation between the Moon's and the Sun's centers,
     * a.k.a. the arc of light.
     * Presumably in radians.
     *
     * @see MoonSunElongationCalculator
     */
    MOON_SUN_ELONGATION(
        MoonSunElongationCalculator.class
    ),

    /**
     * Mean sidereal time at the Greenwich meridian (θ0).
     * Presumably in degrees (1h ~ 15°).
     *
     * @see SiderealMeanTimeCalculator
     */
    SIDEREAL_MEAN_TIME_0(
        SiderealMeanTimeCalculator.class
    ),

    /**
     * Apparent sidereal time at the Greenwich meridian (θ0).
     * Presumably in degrees (1h ~ 15°).
     *
     * @see SiderealApparentTimeCalculator
     */
    SIDEREAL_APPARENT_TIME_0(
        SiderealApparentTimeCalculator.class
    ),

    /**
     * The Sun's ecliptical longitude with correction due to aberration.
     * Presumably in radians.
     *
     * @see SunAberratedLongitudeCalculator
     */
    SUN_ABERRATED_LONGITUDE(
        SunAberratedLongitudeCalculator.class
    ),

    /**
     * The Sun's apparent ecliptical longitude (λ) (indicator of Equinoxes/Solstices).
     * Presumably in radians.
     *
     * @see SunApparentLongitudeCalculator
     */
    SUN_APPARENT_LONGITUDE(
        SunApparentLongitudeCalculator.class
    ),

    /**
     * The Sun's declination (δ).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see DeclinationCalculator
     */
    SUN_DECLINATION(
        DeclinationCalculator.class,
        () -> new DeclinationCalculator(Body.SUN)
    ),

    /**
     * The Sun's geometric ecliptical longitude (☉).
     * Presumably in radians.
     *
     * @see SunGeometricLongitudeCalculator
     */
    SUN_GEOMETRIC_LONGITUDE(
        SunGeometricLongitudeCalculator.class
    ),

    /**
     * The Sun's apparent hour angle at the Greenwich meridian (H0).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see HourAngleCalculator
     */
    SUN_HOUR_ANGLE_0(
        HourAngleCalculator.class,
        () -> new HourAngleCalculator(Body.SUN)
    ),

    /**
     * The Sun's geometric ecliptical latitude (β).
     * Presumably in radians.
     *
     * @see SunLatitudeCalculator
     */
    SUN_LATITUDE(
        SunLatitudeCalculator.class
    ),

    /**
     * The Sun's equatorial horizontal parallax (π).
     * Presumably in radians.
     *
     * @see ParallaxCalculator
     */
    SUN_PARALLAX(
        ParallaxCalculator.class,
        () -> new ParallaxCalculator(Body.SUN)
    ),

    /**
     * The sine of
     * {@linkplain #SUN_PARALLAX the Sun's equatorial horizontal parallax}
     * (sin(π)).
     *
     * @see SunParallaxSineCalculator
     */
    SUN_PARALLAX_SINE(
        SunParallaxSineCalculator.class
    ),

    /**
     * The Sun's right ascension (α).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see RightAscensionCalculator
     */
    SUN_RIGHT_ASCENSION(
        RightAscensionCalculator.class,
        () -> new RightAscensionCalculator(Body.SUN)
    );

    protected final Class<? extends Provider<GlobalCoord, TimelinePoint>> providerClass;
    private final Supplier<Provider<GlobalCoord, TimelinePoint>> providerSupplier;

    GlobalCoord(
        Class<? extends Provider<GlobalCoord, TimelinePoint>> providerClass,
        Supplier<Provider<GlobalCoord, TimelinePoint>> providerSupplier
    ) {
        this.providerClass = providerClass;
        this.providerSupplier = providerSupplier;
    }

    GlobalCoord(
        Class<? extends Provider<GlobalCoord, TimelinePoint>> providerClass
    ) {
        this.providerClass = providerClass;
        this.providerSupplier = null;
    }

    /**
     * Gets a provider: a calculator of values of this quantity.
     *
     * @return  provider (calculator of values of this quantity)
     */
    public Provider<GlobalCoord, TimelinePoint> getProvider() {
        try {
            return providerSupplier == null ? providerClass.newInstance() : providerSupplier.get();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException(name() + " is ill-prepared.", e);
        }
    }

    @Override
    public Key key() {
        return Key.ofGlobalCoord(this);
    }
}
