package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of {@linkplain GlobalCoord#SUN_LATITUDE the Sun's geometric latitude (β)}.
 * Given required parameters, it's in itself rather quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 25 (Higher accuracy, p. 166)"
 */
public final class SunLatitudeCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * Calculates {@linkplain GlobalCoord#SUN_LATITUDE the Sun's geometric latitude (β)}.
     * Rather quick.
     *
     * @param tx                        time argument
     * @param heliocentricLatitude      {@linkplain GlobalCoord#EARTH_LATITUDE the Earth's heliocentric latitude (B)}, in radians
     * @param heliocentricLongitude     {@linkplain GlobalCoord#EARTH_LONGITUDE the Earth's heliocentric longitude (L)}, in radians
     * @return                          {@linkplain GlobalCoord#SUN_LATITUDE the Sun's geometric latitude (β)}, in radians: [-π/2, π/2]
     */
    public double calculate(TimelinePoint tx, double heliocentricLatitude, double heliocentricLongitude) {
        double basicLongitude = heliocentricLongitude + Math.PI;
        double lambdaPrim = calculateLambdaPrim(tx.toDynamicalTime().toCenturialT(), basicLongitude);
        double basicToFK5DeltaArcseconds = 0.03916 * (Math.cos(lambdaPrim) - Math.sin(lambdaPrim));
        return -heliocentricLatitude + Math.toRadians(Calcs.Angle.arcsecondsToDegrees(basicToFK5DeltaArcseconds));
    }

    protected double calculateLambdaPrim(double centurialT, double basicLongitude) {
        return basicLongitude - Math.toRadians((1.397 * centurialT) + (0.00031 * centurialT * centurialT));
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.SUN_LATITUDE;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(GlobalCoord.EARTH_LATITUDE, GlobalCoord.EARTH_LONGITUDE);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            tx,
            (Double) precalculatedValues.get(GlobalCoord.EARTH_LATITUDE),
            (Double) precalculatedValues.get(GlobalCoord.EARTH_LONGITUDE)
        );
    }
}
