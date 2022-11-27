package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.MeanMotionApproximate;

public class MoonPhaseApproximator {

    /**
     * Meeus 1998; Ch. 49, p. 349
     */
    public static final double BASE_NEW_MOON_JDE = 2451550.09766;
    public static final double BASE_NEW_MOON_CENTURIAL_T = Timeline.julianDayToCenturialT(BASE_NEW_MOON_JDE);
    private static final double MEAN_LUNATION_LENGTH_CENTURIAL_T_COEFFICIENT = 0.00000021621;
    private static final double MEAN_LUNATION_LENGTH_CENTURIAL_T_SQUARE_COEFFICIENT = -0.000000000364;

    /**
     * ESAA: 12.11-2, p. 576
     */
    public static double calculateMeanLunationLengthDays(TimelinePoint tx) {
        double cTx = tx.toDynamicalTime().toCenturialT();
        return MeanMotionApproximate.SYNODIC_MONTH.lengthDays
            + (cTx * MEAN_LUNATION_LENGTH_CENTURIAL_T_COEFFICIENT)
            + (cTx * cTx * MEAN_LUNATION_LENGTH_CENTURIAL_T_SQUARE_COEFFICIENT);
    }

    public static double calculateMeanLunationLengthDaysBetween(TimelinePoint a, TimelinePoint b) {
        double cTa = a.toDynamicalTime().toCenturialT();
        double cTb = b.toDynamicalTime().toCenturialT();
        return Double.compare(cTa, cTb) == 0 ? calculateMeanLunationLengthDays(a) :
            (calculateIntegralOfMeanLunationLengthDays(cTb) - calculateIntegralOfMeanLunationLengthDays(cTa))
            / (cTb - cTa);
    }

    public double approximateJulianEphemerisDayAround(TimelinePoint tx, MoonPhase phase) {
        tx = tx.toDynamicalTime();

        /**
         * Theoretically, we would calculate it for the interval between Base New Moon (Tb) and Tx.
         * However, substituting Tb with Epoch 2000 (T0 = 0) simplifies the formula significantly
         * and the difference between T0 and Tb is so small, that it affects the resulting mean L by 1~2 microseconds (!),
         * which yields the difference in target Moon phase approximation around 0.12 s at max. (a neglectible difference).
         */
        double meanL = calculateMeanLunationLengthDaysFromEpoch2000ToTx(tx.toCenturialT());
        double lunationCapacityVectorFromBaseToX = (tx.julianDay - BASE_NEW_MOON_JDE) / meanL;
        double meanNewMoonBeforeXJde = BASE_NEW_MOON_JDE + (Math.floor(lunationCapacityVectorFromBaseToX) * meanL);

        /**
         * Theoretically, from this step on we would get a better result if we used
         * a value of mean L calculated for Tx, not for an interval.
         * However, in the scope of this project it would give a precision gain lesser than 1 s,
         * which is neglectible - for we're only approximating here.
         */
        double approximate = meanNewMoonBeforeXJde + (phase.lunationFraction * meanL);

        double distanceFromApproximateToX = tx.julianDay - approximate;
        if (Math.abs(distanceFromApproximateToX) > 0.5 * meanL) {
            approximate += Math.signum(distanceFromApproximateToX) * meanL;
        }

        return approximate;
    }

    protected static double calculateMeanLunationLengthDaysFromEpoch2000ToTx(double cTx) {
        return MeanMotionApproximate.SYNODIC_MONTH.lengthDays
            + (cTx * MEAN_LUNATION_LENGTH_CENTURIAL_T_COEFFICIENT / 2.0)
            + (cTx * cTx * MEAN_LUNATION_LENGTH_CENTURIAL_T_SQUARE_COEFFICIENT / 3.0);
    }

    protected static double calculateIntegralOfMeanLunationLengthDays(double cTx) {
        return cTx * MeanMotionApproximate.SYNODIC_MONTH.lengthDays
            + (cTx * cTx * MEAN_LUNATION_LENGTH_CENTURIAL_T_COEFFICIENT / 2.0)
            + (cTx * cTx * cTx * MEAN_LUNATION_LENGTH_CENTURIAL_T_SQUARE_COEFFICIENT / 3.0);
    }

}
