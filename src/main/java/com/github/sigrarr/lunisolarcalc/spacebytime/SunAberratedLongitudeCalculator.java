package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the Sun's longitude with correction due to aberration.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link SpaceByTimeCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 25 (Higher accuracy, p. 167)"
 */
public final class SunAberratedLongitudeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SUN_ABERRATED_LONGITUDE;

    /**
     * Calculates the Sun's longitude with correction due to aberration: [0, 2π).
     * Quick.
     *
     * @param geometricLongitude    {@linkplain SunGeometricLongitudeCalculator the Sun's geometric longitude} (☉), in radians
     * @param aberration            {@linkplain AberrationEarthSunCalculator aberration of the Sun's geocentric position}, in radians
     * @return                      the Sun's longitude with correction due to aberration: [0, 2π)
     */
    public double calculate(double geometricLongitude, double aberration) {
        return Calcs.Angle.normalizeLongitudinally(geometricLongitude + aberration);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.SUN_GEOMETRIC_LONGITUDE, Subject.ABERRATION_EARTH_SUN);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.SUN_GEOMETRIC_LONGITUDE),
            (Double) precalculatedValues.get(Subject.ABERRATION_EARTH_SUN)
        );
    }
}
