package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Transformations;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#MOON_RIGHT_ASCENSION the Moon's right ascension (α)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class MoonRightAscensionCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.MOON_RIGHT_ASCENSION;

    /**
     * Calculates {@linkplain GlobalCoord#MOON_RIGHT_ASCENSION the Moon's right ascension (α)}: [0, 2π).
     * Quick.
     *
     * @param moonApparentLongitude     {@linkplain GlobalCoord#MOON_APPARENT_LONGITUDE the Moon's apparent longitude (λ)}, in radians
     * @param moonLatitude              {@linkplain GlobalCoord#MOON_LATITUDE the Moon's latitude (β)}, in radians
     * @param eclipticObliquity         {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     * @return                          {@linkplain GlobalCoord#MOON_RIGHT_ASCENSION the Moon's right ascension (α)}, in radians: [0, 2π)
     */
    public double calculate(double moonApparentLongitude, double moonLatitude, double eclipticObliquity) {
        return Transformations.eclipticalToRightAscension(moonApparentLongitude, moonLatitude, eclipticObliquity);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.MOON_APPARENT_LONGITUDE, GlobalCoord.MOON_LATITUDE, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.MOON_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.MOON_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
