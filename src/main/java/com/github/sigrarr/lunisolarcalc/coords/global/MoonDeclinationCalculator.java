package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Transformations;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#MOON_DECLINATION the Moon's declination (δ)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class MoonDeclinationCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.MOON_DECLINATION;

    /**
     * Calculates {@linkplain GlobalCoord#MOON_DECLINATION the Moon's declination (δ)}: [-π/2, π/2].
     * Quick.
     *
     * @param moonLatitude              {@linkplain GlobalCoord#MOON_LATITUDE the Moon's latitude (β)}, in radians
     * @param moonApparentLongitude     {@linkplain GlobalCoord#MOON_APPARENT_LONGITUDE the Moon's apparent longitude (λ)}, in radians
     * @param eclipticObliquity         {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     * @return                          {@linkplain GlobalCoord#MOON_DECLINATION the Moon's declination (δ)}, in radians: [-π/2, π/2]
     */
    public double calculate(double moonLatitude, double moonApparentLongitude, double eclipticObliquity) {
        return Transformations.eclipticalToDeclination(moonLatitude, moonApparentLongitude, eclipticObliquity);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.MOON_LATITUDE, GlobalCoord.MOON_APPARENT_LONGITUDE, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.MOON_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.MOON_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
