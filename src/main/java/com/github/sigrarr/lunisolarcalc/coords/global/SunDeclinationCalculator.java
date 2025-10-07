package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.coords.Transformations;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#SUN_DECLINATION the Sun's declination (δ)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Transformations
 */
public final class SunDeclinationCalculator extends Transformations implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.SUN_DECLINATION;

    /**
     * Calculates {@linkplain GlobalCoord#SUN_DECLINATION the Sun's declination (δ)}: [-π/2, π/2].
     * Quick.
     *
     * @param sunLatitude           {@linkplain GlobalCoord#SUN_LATITUDE the Sun's latitude (β)}, in radians
     * @param sunApparentLongitude  {@linkplain GlobalCoord#SUN_APPARENT_LONGITUDE the Sun's apparent longitude (λ)}, in radians
     * @param eclipticObliquity     {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     * @return                      {@linkplain GlobalCoord#SUN_DECLINATION the Sun's declination (δ)}, in radians: [-π/2, π/2]
     */
    public double calculate(double sunLatitude, double sunApparentLongitude, double eclipticObliquity) {
        return Transformations.eclipticalToDeclination(sunLatitude, sunApparentLongitude, eclipticObliquity);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.SUN_LATITUDE, GlobalCoord.SUN_APPARENT_LONGITUDE, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SUN_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.SUN_APPARENT_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
