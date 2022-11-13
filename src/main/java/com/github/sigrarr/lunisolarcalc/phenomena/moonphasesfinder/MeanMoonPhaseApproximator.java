package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public class MeanMoonPhaseApproximator {

    private static final double BASE_ADDEND_JDE = 2451550.09766;
    private static final double EFFECTIVE_MEAN_LUNATION_DAYS = 29.530588861;
    private static final double MINUTE_TO_YEAR = 60.0 / MeanMotionApproximate.TROPICAL_YEAR.lengthSeconds;
    private static final double ARGUMENT_SHIFT_BASE_FORWARD = 1.25;
    private static final double ARGUMENT_SHIFT_BASE_BACKWARD = -0.75;
    private double gregorianY = Double.MIN_VALUE;
    private double kLunationsFromEpoch = 0.0;
    private double kTPolynomialAddend = 0.0;

    /**
     * Meeus 1998, 49.1-3, pp. 349-350
     */
    public double approximateJulianEphemerisDayAround(TimelinePoint tx, MoonPhase phase) {
        update(tx);
        return BASE_ADDEND_JDE
            + (EFFECTIVE_MEAN_LUNATION_DAYS * (this.kLunationsFromEpoch + phase.lunationFraction))
            + kTPolynomialAddend;
    }

    public double approximateJulianEphemerisDayForward(TimelinePoint tx, MoonPhase phase) {
        return approximateJulianEphemerisDayInDirection(tx, phase, ARGUMENT_SHIFT_BASE_FORWARD);
    }

    public double approximateJulianEphemerisDayBackward(TimelinePoint tx, MoonPhase phase) {
        return approximateJulianEphemerisDayInDirection(tx, phase, ARGUMENT_SHIFT_BASE_BACKWARD);
    }

    private void update(TimelinePoint tx) {
        double gregorianY = tx.toGregorianCalendarPoint().toYearWithFraction();
        if (!Calcs.equal(this.gregorianY, gregorianY, MINUTE_TO_YEAR)) {
            this.gregorianY = gregorianY;
            kLunationsFromEpoch = gregorianToKMonthsFromEpoch();
            double kT = kToCenturialT(kLunationsFromEpoch);
            double kT2 = kT * kT;
            kTPolynomialAddend = (0.00015437 * kT2)
                + (0.00000015 * kT2 * kT)
                + (0.00000000073 * kT2 * kT2);
        }
    }

    private double gregorianToKMonthsFromEpoch() {
        return Math.floor((gregorianY - 2000)
            * MeanMotionApproximate.TROPICAL_YEAR.lengthDays / EFFECTIVE_MEAN_LUNATION_DAYS);
    }

    private double kToCenturialT(double k) {
        return k / 1236.85;
    }

    private double approximateJulianEphemerisDayInDirection(TimelinePoint tx, MoonPhase phase, double shiftBase) {
        double jde = tx.convertToDynamicalTime().julianDay;
        double approximateAround = approximateJulianEphemerisDayAround(tx, phase);
        double diff = approximateAround - jde;
        if (Math.signum(diff) == Math.signum(shiftBase) || Math.abs(diff) < Time.timeToDays(0, 1, 0)) {
            return approximateAround;
        } else {
            double argumentShift = estimateDistanceToAdjacentAroundApproximationAbstractionClassCenter(phase, shiftBase, diff);
            TimelinePoint newUniversalTimeArgument = new TimelinePoint(jde + argumentShift, TimeType.DYNAMICAL).convertToUniversalTime();
            return approximateJulianEphemerisDayAround(newUniversalTimeArgument, phase);
        }
    }

    private double estimateDistanceToAdjacentAroundApproximationAbstractionClassCenter(MoonPhase phase, double shiftBase, double diff) {
        return (shiftBase - phase.lunationFraction) * MeanMotionApproximate.SYNODIC_MONTH.lengthDays + diff;
    }
}
