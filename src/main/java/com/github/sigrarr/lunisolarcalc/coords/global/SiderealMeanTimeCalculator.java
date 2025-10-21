package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#SIDEREAL_MEAN_TIME_0 mean sidereal time at the Greenwich meridian (θ0)}.
 * Quick operation.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 12 (pp. 87-89)"
 */
public class SiderealMeanTimeCalculator implements Provider<GlobalCoord, TimelinePoint> {
    /**
     * Calculates the {@linkplain GlobalCoord#SIDEREAL_MEAN_TIME_0 mean sidereal time at the Greenwich meridian (θ0)}.
     *
     * @param tx    time argument
     * @return      {@linkplain GlobalCoord#SIDEREAL_MEAN_TIME_0 mean sidereal time at the Greenwich meridian (θ0)},
     *              in radians: [0, 2π)
     */
    public double calculate(TimelinePoint tx) {
        TimelinePoint utx = tx.toUniversalTime();
        double cT = utx.toCenturialT();
        double thetaZeroDegrees = 280.46061837
            + 360.98564736629 * (utx.julianDay - Timeline.EPOCH_2000_JD)
            + 0.000387933 * cT * cT
            - cT * cT * cT / 38710000.0;
        return Calcs.Angle.toNormalLongitude(Math.toRadians(thetaZeroDegrees));
    }

    @Override
    public GlobalCoord provides() {
        return GlobalCoord.SIDEREAL_MEAN_TIME_0;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.noneOf(GlobalCoord.class);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(tx);
    }
}
