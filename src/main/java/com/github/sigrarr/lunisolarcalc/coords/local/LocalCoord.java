package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.function.Function;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Key;
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
     * The ρ*cos(φ′) quantity, useful for several topocentric calculations,
     * where ρ is the ratio of the observer's geocentric radius
     * to the Earth equatorial radius.
     * Presumably in radians.
     *
     * @see RhoCosPhiPrimCalculator
     */
    RHO_COS_PHI_PRIM(
        (gp) -> new RhoCosPhiPrimCalculator(gp)
    ),

    /**
     * The ρ*sin(φ′) quantity, useful for several topocentric calculations,
     * where ρ is the ratio of the observer's geocentric radius
     * to the Earth equatorial radius.
     * Presumably in radians.
     *
     * @see RhoSinPhiPrimCalculator
     */
    RHO_SIN_PHI_PRIM(
        (gp) -> new RhoSinPhiPrimCalculator(gp)
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
