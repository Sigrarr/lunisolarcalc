package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#SIDEREAL_APPARENT_TIME_0 apparent sidereal time at the Greenwich meridian (θ0)}.
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 12 (pp. 87-89)"
 */
public class SiderealApparentTimeCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * Calculates the {@linkplain GlobalCoord#SIDEREAL_APPARENT_TIME_0 apparent sidereal time at the Greenwich meridian (θ0)}.
     *
     * @param meanSiderealTime      {@linkplain GlobalCoord#SIDEREAL_MEAN_TIME_0 mean sidereal time (θ0)}, in radians
     * @param nutuationInLongitude  {@linkplain GlobalCoord#EARTH_NUTUATION_IN_LONGITUDE nutuation in longitude (Δψ)}, in radians
     * @param eclipticObliquity     {@linkplain GlobalCoord#ECLIPTIC_TRUE_OBLIQUITY true obliquity of the ecliptic (ε)}, in radians
     * @return                      {@linkplain GlobalCoord#SIDEREAL_APPARENT_TIME_0 apparent sidereal time at the Greenwich meridian (θ0)},
     *                              in radians: [0, 2π)
     */
    public double calculate(double meanSiderealTime, double nutuationInLongitude, double eclipticObliquity) {
        return Calcs.Angle.toNormalLongitude(
            meanSiderealTime + calculateNutuationInRightAscension(nutuationInLongitude, eclipticObliquity)
        );
    }

    protected double calculateNutuationInRightAscension(double deltaPsi, double epsilon) {
        return deltaPsi * Math.cos(epsilon);
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.SIDEREAL_APPARENT_TIME_0;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.of(
            GlobalCoord.SIDEREAL_MEAN_TIME_0,
            GlobalCoord.EARTH_NUTUATION_IN_LONGITUDE,
            GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(GlobalCoord.SIDEREAL_MEAN_TIME_0),
            (Double) precalculatedValues.get(GlobalCoord.EARTH_NUTUATION_IN_LONGITUDE),
            (Double) precalculatedValues.get(GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
