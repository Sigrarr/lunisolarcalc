package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Transformations;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#SUN_RIGHT_ASCENSION the Sun's right ascension (α)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class SunRightAscensionCalculator extends Transformations implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.SUN_RIGHT_ASCENSION;

    /**
     * Calculates {@linkplain GlobalCoord#SUN_RIGHT_ASCENSION the Sun's right ascension (α)}: [0, 2π).
     * Quick.
     *
     * @param sunApparentLongitude  {@linkplain GlobalCoord#SUN_APPARENT_LONGITUDE the Sun's apparent longitude (λ)}, in radians
     * @param sunLatitude           {@linkplain GlobalCoord#SUN_LATITUDE the Sun's latitude (β)}, in radians
     * @param eclipticObliquity     {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     * @return                      {@linkplain GlobalCoord#SUN_RIGHT_ASCENSION the Sun's right ascension (α)}, in radians: [0, 2π)
     */
    public double calculate(double sunApparentLongitude, double sunLatitude, double eclipticObliquity) {
        return Transformations.eclipticalToRightAscension(sunApparentLongitude, sunLatitude, eclipticObliquity);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.SUN_APPARENT_LONGITUDE, GlobalCoord.SUN_LATITUDE, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SUN_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.SUN_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
