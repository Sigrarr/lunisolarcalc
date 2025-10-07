package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#SUN_GEOMETRIC_LONGITUDE the Sun's geometric longitude (☉)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 25 (Higher accuracy, p. 166)"
 */
public final class SunGeometricLongitudeCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.SUN_GEOMETRIC_LONGITUDE;
    public static final double BASIC_TO_FK5_DELTA = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(-0.09033));
    private static final double HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND = Math.PI + BASIC_TO_FK5_DELTA;

    /**
     * Calculates {@linkplain GlobalCoord#SUN_GEOMETRIC_LONGITUDE the Sun's geometric longitude (☉)}: [0, 2π).
     * Quick.
     *
     * @param heliocentricLongitude {@linkplain GlobalCoord#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}, in radians
     * @return                      {@linkplain GlobalCoord#SUN_GEOMETRIC_LONGITUDE the Sun's geometric longitude (☉)},
     *                              in radians: [0, 2π)
     */
    public double calculate(double heliocentricLongitude) {
        return Calcs.Angle.toNormalLongitude(heliocentricLongitude + HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND);
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.EARTH_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate((Double) precalculatedValues.get(GlobalCoord.EARTH_LONGITUDE));
    }
}
