package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.function.Function;

import com.github.sigrarr.lunisolarcalc.Body;
import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Key;
import com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords;
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
        (gc) -> new AltitudeCalculator(Body.MOON, gc)
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
        (gc) -> new AzimuthCalculator(Body.MOON, gc)
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
        (gc) -> new AltitudeCalculator(Body.SUN, gc)
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
        (gc) -> new AzimuthCalculator(Body.SUN, gc)
    );

    private final Function<GeoCoords, Provider<Key, TimelinePoint>> providerMaker;

    LocalCoord(
        Function<GeoCoords, Provider<Key, TimelinePoint>> providerMaker
    ) {
        this.providerMaker = providerMaker;
    }

    /**
     * Gets a provider: a calculator of values of this quantity.
     *
     * @param geoCoords     geo. coordinates of the observer
     * @return              provider: a calculator of values of this quantity
     */
    public Provider<Key, TimelinePoint> getProvider(GeoCoords geoCoords) {
        return providerMaker.apply(geoCoords);
    }

    @Override
    public Key key() {
        return Key.ofLocalCoord(this);
    }
}
