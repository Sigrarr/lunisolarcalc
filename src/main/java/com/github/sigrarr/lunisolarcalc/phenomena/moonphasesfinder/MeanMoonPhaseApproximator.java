package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations;

public class MeanMoonPhaseApproximator {

    /**
     * Meeus 1998, 49.1-2, pp. 349-350
     */
    public double approximateJulianEphemerisDay(RomanCalendarPoint roman, MoonPhase phase) {
        double k = Math.floor((roman.toYearWithFraction() - 2000)
            * MeanValueApproximations.TROPICAL_YEAR_MEAN_DAYS / MeanValueApproximations.SYNODIC_MONTH_MEAN_DAYS);
        double kT = kToCenturialT(k);

        return 2451550.09766 + (29.530588861 * (k + phase.lunationFraction))
            + (0.00015437 * kT * kT)
            + (0.00000015 * kT * kT * kT)
            + (0.00000000073 * kT * kT * kT * kT);
    }

    /**
     * Meeus 1998, 49.3, p. 350
     */
    protected double kToCenturialT(double k) {
        return k / 1236.85;
    }
}
