package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.function.Function;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

/**
 * A spatial quantity calculable by this project, of the "local" type,
 * i.e. dependent on the observer's position on Earth.
 *
 * You can calculate values of these quantities using
 * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer calc. compositions},
 * as in {@link CoordsCalcCompositions}. Note that in general they depend on
 * {@link com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord GlobalCoords},
 * so this enum is NOT fit for being the key generic parameter of calc. composition classes,
 * the {@link Key coords.Key} class should be used instead ({@link #key()}).
 */
public enum LocalCoord implements Key.QuantityIndetifier {
    /**
     * Altitude of the Moon, i.e. angular distance of its center from the horizon (h).
     * A horizontal coordinate.
     * Airless and geocentric (without correction for refraction or parallax).
     * Presumably in radians.
     *
     * @see AltitudeCalculator
     */
    MOON_ALTITUDE(
        (gp) -> new AltitudeCalculator(Body.MOON, gp)
    ),

    /**
     * Azimuth of the Moon, measured from the South (A).
     * A horizontal coordinate.
     * Airless and geocentric (without correction for refraction or parallax).
     * Presumably in radians.
     *
     * @see AzimuthCalculator
     */
    MOON_AZIMUTH(
        (gp) -> new AzimuthCalculator(Body.MOON, gp)
    ),

    /**
     * The Moon's apparent geocentric local hour angle (H).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see LocalHourAngleCalculator
     */
    MOON_LOCAL_HOUR_ANGLE(
        (gp) -> new LocalHourAngleCalculator(Body.MOON, gp)
    ),

    /**
     * The parallax in right ascension of the Moon (Δα),
     * i.e. the difference between the Moon's topocentric and geocentric
     * right ascension (α′ − α).
     * Presumably in radians.
     *
     * @see ParallaxInRightAscensionCalculator
     */
    MOON_PARALLAX_IN_RIGHT_ASCENSION(
        (gp) -> new ParallaxInRightAscensionCalculator(Body.MOON)
    ),

    /**
     * Topocentric altitude of the Moon, i.e. angular distance of its center
     * from the horizon (h′). A horizontal coordinate.
     * Presumably in radians.
     *
     * @see TopocentricAltitudeCalculator
     * @see Topo#approximateTopocentricAltitude(double, double, double) Topo.approximateTopocentricAltitude
     */
    MOON_TOPOCENTRIC_ALTITUDE(
        (gp) -> new TopocentricAltitudeCalculator(Body.MOON, gp)
    ),

    /**
     * Topocentric azimuth of the Moon, measured from the South (A′).
     * A horizontal coordinate.
     * Presumably in radians.
     *
     * @see TopocentricAzimuthCalculator
     */
    MOON_TOPOCENTRIC_AZIMUTH(
        (gp) -> new TopocentricAzimuthCalculator(Body.MOON, gp)
    ),

    /**
     * The Moon's topocentric declination (δ′).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see TopocentricDeclinationCalculator
     */
    MOON_TOPOCENTRIC_DECLINATION(
        (gp) -> new TopocentricDeclinationCalculator(Body.MOON)
    ),

    /**
     * The Moon's topocentric local hour angle (H′).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see TopocentricLocalHourAngleCalculator
     */
    MOON_TOPOCENTRIC_LOCAL_HOUR_ANGLE(
        (gp) -> new TopocentricLocalHourAngleCalculator(Body.MOON)
    ),

    /**
     * The Moon's topocentric right ascension (α′).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see TopocentricRightAscensionCalculator
     */
    MOON_TOPOCENTRIC_RIGHT_ASCENSION(
        (gp) -> new TopocentricRightAscensionCalculator(Body.MOON)
    ),

    /**
     * The ρ*cos(φ′) quantity, where ρ is the the observer's geocentric radius
     * as a fraction of the Earth's equatorial radius
     * and φ′ is the geocentric latitude.
     *
     * @see RhoCosPhiPrimeCalculator
     */
    RHO_COS_PHI_PRIME(
        (gp) -> new RhoCosPhiPrimeCalculator(gp)
    ),

    /**
     * The ρ*sin(φ′) quantity, where ρ is the the observer's geocentric radius
     * as a fraction of the Earth's equatorial radius
     * and φ′ is the geocentric latitude.
     *
     * @see RhoSinPhiPrimeCalculator
     */
    RHO_SIN_PHI_PRIME(
        (gp) -> new RhoSinPhiPrimeCalculator(gp)
    ),

    /**
     * Altitude of the Sun, i.e. angular distance of its center from the horizon (h).
     * A horizontal coordinate.
     * Airless and geocentric (without correction for refraction or parallax).
     * Presumably in radians.
     *
     * @see AltitudeCalculator
     */
    SUN_ALTITUDE(
        (gp) -> new AltitudeCalculator(Body.SUN, gp)
    ),

    /**
     * Azimuth of the Sun, measured from the South (A).
     * A horizontal coordinate.
     * Airless and geocentric (without correction for refraction or parallax).
     * Presumably in radians.
     *
     * @see AzimuthCalculator
     */
    SUN_AZIMUTH(
        (gp) -> new AzimuthCalculator(Body.SUN, gp)
    ),

    /**
     * The Sun's apparent geocentric local hour angle (H).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see LocalHourAngleCalculator
     */
    SUN_LOCAL_HOUR_ANGLE(
        (gp) -> new LocalHourAngleCalculator(Body.SUN, gp)
    ),

    /**
     * The parallax in right ascension of the Sun (Δα),
     * i.e. the difference between the Sun's topocentric and geocentric
     * right ascension (α′ − α).
     * Presumably in radians.
     *
     * @see ParallaxInRightAscensionCalculator
     */
    SUN_PARALLAX_IN_RIGHT_ASCENSION(
        (gp) -> new ParallaxInRightAscensionCalculator(Body.SUN)
    ),

    /**
     * Topocentric altitude of the Sun, i.e. angular distance of its center
     * from the horizon (h′). A horizontal coordinate.
     * Presumably in radians.
     *
     * @see TopocentricAltitudeCalculator
     * @see Topo#approximateTopocentricAltitude(double, double, double) Topo.approximateTopocentricAltitude
     */
    SUN_TOPOCENTRIC_ALTITUDE(
        (gp) -> new TopocentricAltitudeCalculator(Body.SUN, gp)
    ),

    /**
     * Topocentric azimuth of the Sun, measured from the South (A′).
     * A horizontal coordinate.
     * Presumably in radians.
     *
     * @see TopocentricAzimuthCalculator
     */
    SUN_TOPOCENTRIC_AZIMUTH(
        (gp) -> new TopocentricAzimuthCalculator(Body.SUN, gp)
    ),

    /**
     * The Sun's topocentric declination (δ′).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see TopocentricDeclinationCalculator
     */
    SUN_TOPOCENTRIC_DECLINATION(
        (gp) -> new TopocentricDeclinationCalculator(Body.SUN)
    ),

    /**
     * The Sun's topocentric local hour angle (H′).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see TopocentricLocalHourAngleCalculator
     */
    SUN_TOPOCENTRIC_LOCAL_HOUR_ANGLE(
        (gp) -> new TopocentricLocalHourAngleCalculator(Body.SUN)
    ),

    /**
     * The Sun's topocentric right ascension (α′).
     * An equatorial coordinate. Presumably in radians.
     *
     * @see TopocentricRightAscensionCalculator
     */
    SUN_TOPOCENTRIC_RIGHT_ASCENSION(
        (gp) -> new TopocentricRightAscensionCalculator(Body.SUN)
    );

    private final Function<GeoPosition, Provider<Key, TimelinePoint>> providerMaker;

    LocalCoord(
        Function<GeoPosition, Provider<Key, TimelinePoint>> providerMaker
    ) {
        this.providerMaker = providerMaker;
    }

    /**
     * Gets a provider: a calculator of values of this quantity.
     *
     * @param geoPosition   the observer's position on Earth
     * @return              provider: a calculator of values of this quantity
     */
    public Provider<Key, TimelinePoint> getProvider(GeoPosition geoPosition) {
        return providerMaker.apply(geoPosition);
    }

    @Override
    public Key key() {
        return Key.ofLocalCoord(this);
    }
}
