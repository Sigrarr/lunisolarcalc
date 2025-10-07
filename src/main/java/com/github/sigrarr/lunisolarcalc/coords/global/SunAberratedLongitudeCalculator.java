package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#SUN_ABERRATED_LONGITUDE the Sun's longitude with correction due to aberration}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 25 (Higher accuracy, p. 167)"
 */
public final class SunAberratedLongitudeCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.SUN_ABERRATED_LONGITUDE;

    /**
     * Calculates {@linkplain GlobalCoord#SUN_ABERRATED_LONGITUDE the Sun's longitude with correction due to aberration}: [0, 2π).
     * Quick.
     *
     * @param geometricLongitude    {@linkplain GlobalCoord#SUN_GEOMETRIC_LONGITUDE the Sun's geometric longitude (☉)}, in radians
     * @param aberration            {@linkplain GlobalCoord#ABERRATION_EARTH_SUN aberration of the Sun's geocentric position}, in radians
     * @return                      {@linkplain GlobalCoord#SUN_ABERRATED_LONGITUDE the Sun's longitude with correction due to aberration},
     *                              in radians: [0, 2π)
     */
    public double calculate(double geometricLongitude, double aberration) {
        return Calcs.Angle.toNormalLongitude(geometricLongitude + aberration);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.SUN_GEOMETRIC_LONGITUDE, GlobalCoord.ABERRATION_EARTH_SUN);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SUN_GEOMETRIC_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.ABERRATION_EARTH_SUN)
        );
    }
}
