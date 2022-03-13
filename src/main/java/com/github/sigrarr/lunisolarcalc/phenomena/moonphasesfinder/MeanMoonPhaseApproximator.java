package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public class MeanMoonPhaseApproximator {

    private static final double BASE_ADDEND_JDE = 2451550.09766;
    private static final double EFFECTIVE_MEAN_LUNATION_DAYS = 29.530588861;
    private static final double MINUTE_TO_YEAR = 60.0 / MeanMotionApproximate.TROPICAL_YEAR.lengthSeconds;
    private static final double ARGUMENT_SHIFT_BASE_FORWARD = 1.25;
    private static final double ARGUMENT_SHIFT_BASE_BACKWARD = -0.75;
    private double romanY = Double.MIN_VALUE;
    private double kLunationsFromEpoch = 0.0;
    private double kTPolynomialAddend = 0.0;

    /**
     * Meeus 1998, 49.1-3, pp. 349-350
     */
    public double approximateJulianEphemerisDayAround(RomanCalendarPoint roman, MoonPhase phase) {
        update(roman);
        return BASE_ADDEND_JDE
            + (EFFECTIVE_MEAN_LUNATION_DAYS * (this.kLunationsFromEpoch + phase.lunationFraction))
            + kTPolynomialAddend;
    }

    public double approximateJulianEphemerisDayForward(RomanCalendarPoint roman, MoonPhase phase) {
        return approximateJulianEphemerisDayInDirection(roman, phase, ARGUMENT_SHIFT_BASE_FORWARD);
    }

    public double approximateJulianEphemerisDayBackward(RomanCalendarPoint roman, MoonPhase phase) {
        return approximateJulianEphemerisDayInDirection(roman, phase, ARGUMENT_SHIFT_BASE_BACKWARD);
    }

    private void update(RomanCalendarPoint roman) {
        double romanY = roman.toYearWithFraction();
        if (!Calcs.equal(this.romanY, romanY, MINUTE_TO_YEAR)) {
            this.romanY = romanY;
            kLunationsFromEpoch = romanToKMonthsFromEpoch();
            double kT = kToCenturialT(kLunationsFromEpoch);
            double kT2 = kT * kT;
            kTPolynomialAddend = (0.00015437 * kT2)
                + (0.00000015 * kT2 * kT)
                + (0.00000000073 * kT2 * kT2);
        }
    }

    private double romanToKMonthsFromEpoch() {
        return Math.floor((romanY - 2000)
            * MeanMotionApproximate.TROPICAL_YEAR.lengthDays / EFFECTIVE_MEAN_LUNATION_DAYS);
    }

    private double kToCenturialT(double k) {
        return k / 1236.85;
    }

    private double approximateJulianEphemerisDayInDirection(RomanCalendarPoint roman, MoonPhase phase, double shiftBase) {
        double jde = Timeline.romanCalendarToJulianDay(roman);
        double approximateAround = approximateJulianEphemerisDayAround(roman, phase);
        double diff = approximateAround - jde;
        if (Math.signum(diff) == Math.signum(shiftBase) || Math.abs(diff) < Time.timeToDays(0, 1, 0)) {
            return approximateAround;
        } else {
            double argumentShift = estimateDistanceToAdjacentAroundApproximationAbstractionClassCenter(phase, shiftBase, diff);
            return approximateJulianEphemerisDayAround(Timeline.julianDayToRomanCalendar(jde + argumentShift), phase);
        }
    }

    private double estimateDistanceToAdjacentAroundApproximationAbstractionClassCenter(MoonPhase phase, double shiftBase, double diff) {
        return (shiftBase - phase.lunationFraction) * MeanMotionApproximate.SYNODIC_MONTH.lengthDays + diff;
    }
}
