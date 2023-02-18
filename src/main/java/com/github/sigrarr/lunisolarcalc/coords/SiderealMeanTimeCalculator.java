package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain Subject#SIDEREAL_MEAN_TIME mean sidereal time at the Greenwich meridian (θ0)}.
 * Quick operation.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 12 (pp. 87-89)"
 */
public class SiderealMeanTimeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SIDEREAL_MEAN_TIME;

    /**
     * Calculates the {@linkplain Subject#SIDEREAL_MEAN_TIME mean sidereal time at the Greenwich meridian (θ0)},
     * expressed in degrees: [0, 360°). 15° corresponds to 1 hour.
     *
     * @param tx    time argument
     * @return      {@linkplain Subject#SIDEREAL_MEAN_TIME mean sidereal time at the Greenwich meridian (θ0)},
     *              in degrees: [0, 360°)
     */
    public double calculate(TimelinePoint tx) {
        TimelinePoint utX = tx.toUniversalTime();
        double cT = utX.toCenturialT();
        double thetaZero = 280.46061837
            + 360.98564736629 * (utX.julianDay - Timeline.EPOCH_2000_JD)
            + 0.000387933 * cT * cT
            - cT * cT * cT / 38710000.0;
        return Calcs.Angle.toNormalLongitude(thetaZero, 360.0);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.noneOf(Subject.class);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(tx);
    }
}
