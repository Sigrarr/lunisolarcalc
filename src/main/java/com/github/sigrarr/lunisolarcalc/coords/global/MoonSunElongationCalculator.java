package com.github.sigrarr.lunisolarcalc.coords.global;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.hav;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#MOON_SUN_ELONGATION the geocentric elongation of the Moon from the Sun (ψ)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 17 (pp. 109, 115), Ch. 48 (p. 345)"
 */
public class MoonSunElongationCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.MOON_SUN_ELONGATION;

    /**
     * Calculates {@linkplain GlobalCoord#MOON_SUN_ELONGATION the geocentric elongation of the Moon from the Sun (ψ)}:
     * [0, π].
     *
     * You can use either ecliptical or equatorial coordinates as arguments,
     * but all arguments have to belong to the same system.
     *
     * Quick operation.
     *
     * @param moonLatitudalCoord    the Moon's {@linkplain GlobalCoord#MOON_LATITUDE latitude (β)}
     *                              or {@linkplain GlobalCoord#MOON_DECLINATION declination (δ)}, in radians
     * @param moonLongitudalCoord   the Moon's {@linkplain GlobalCoord#MOON_APPARENT_LONGITUDE apparent longitude (λ)},
     *                              or {@linkplain GlobalCoord#MOON_RIGHT_ASCENSION right ascension (α)}, in radians
     * @param sunLatitudalCoord     the Sun's {@linkplain GlobalCoord#SUN_LATITUDE latitude (β)}
     *                              or {@linkplain GlobalCoord#SUN_DECLINATION declination (δ)}, in radians
     * @param sunLongitudalCoord    the Sun's {@linkplain GlobalCoord#SUN_APPARENT_LONGITUDE apparent longitude (λ)},
     *                              or {@linkplain GlobalCoord#SUN_RIGHT_ASCENSION right ascension (α)}, in radians
     * @return                      {@linkplain GlobalCoord#MOON_SUN_ELONGATION the geocentric elongation of the Moon from the Sun (ψ)},
     *                              in radians: [0, π]
     */
    public double calculate(
        double moonLatitudalCoord,
        double moonLongitudalCoord,
        double sunLatitudalCoord,
        double sunLongitudalCoord
    ) {
        return 2.0 * Math.asin(Math.sqrt(
            hav(moonLatitudalCoord - sunLatitudalCoord)
            + Math.cos(moonLatitudalCoord) * Math.cos(sunLatitudalCoord) * hav(moonLongitudalCoord - sunLongitudalCoord)
        ));
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(
            GlobalCoord.MOON_LATITUDE,
            GlobalCoord.MOON_APPARENT_LONGITUDE,
            GlobalCoord.SUN_LATITUDE,
            GlobalCoord.SUN_APPARENT_LONGITUDE
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.MOON_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.MOON_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.SUN_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.SUN_APPARENT_LONGITUDE)
        );
    }
}
