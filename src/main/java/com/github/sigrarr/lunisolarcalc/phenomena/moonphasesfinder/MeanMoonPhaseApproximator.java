package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

public class MeanMoonPhaseApproximator {

    private static final double BASE_ADDEND_JDE = 2451550.09766;
    private static final double EFFECTIVE_MEAN_LUNATION_DAYS = 29.530588861;
    private static final double MINUTE_TO_YEAR = 1.0 / MeanValueApproximations.TROPICAL_YEAR_MEAN_MINUTES;
    private double romanY = Double.MIN_VALUE;
    private double kLunationsFromEpoch = 0.0;
    private double kTPolynomialAddend = 0.0;

    /**
     * Meeus 1998, 49.1-3, pp. 349-350
     */
    public double approximateJulianEphemerisDay(RomanCalendarPoint roman, MoonPhase phase) {
        update(roman);
        return BASE_ADDEND_JDE
            + (EFFECTIVE_MEAN_LUNATION_DAYS * (this.kLunationsFromEpoch + phase.lunationFraction))
            + kTPolynomialAddend;
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
            * MeanValueApproximations.TROPICAL_YEAR_MEAN_DAYS / EFFECTIVE_MEAN_LUNATION_DAYS);
    }

    private double kToCenturialT(double k) {
        return k / 1236.85;
    }
}
