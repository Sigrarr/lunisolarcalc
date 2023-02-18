package com.github.sigrarr.lunisolarcalc.phenomena.global;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

/**
 * A tool for approximating time of the Moon's phases. Calculates a "mean" value,
 * sufficient and effective as the initial step in a {@linkplain MoonPhaseFinder more precise search}.
 * Uses an original method, inspired by the one of Meeus, but more dependable for distant past.
 * Utilizes the base New Moon instant given by him and {@link MeanCycle#LUNATION}.
 * Stateless. Quick.
 *
 * @see "Meeus 1998: Ch. 49, p. 349"
 */
public final class MoonPhaseApproximator {

    protected static final double BASE_NEW_MOON_JDE = 2451550.09766;

    /**
     * Calculates approximate "mean" time of the requested Moon's phase
     * as close to the requested time as possible, in Julian Ephemeris Day.
     * Note that the result may be found before or after the time argument.
     *
     * @param tx        time argument to look around
     * @param phase     Moon's phase to look for
     * @return          approximate "mean" time, in Julian Ephemeris Day
     */
    public double approximateJulianEphemerisDayAround(TimelinePoint tx, MoonPhase phase) {
        tx = tx.toDynamicalTime();

        /**
         * Theoretically, we would calculate it for the interval between the base New Moon (Tb) and Tx.
         * However, substituting Tb with Epoch 2000 (T0 = 0) simplifies the formula noticeably
         * while the difference between T0 and Tb is so small, that it affects the resulting mean L by less than 2 microseconds (!)
         * and yields the difference in target Moon phase approximation around 0.12 s at max. (a neglectible difference).
         */
        double meanL = MeanCycle.LUNATION.calculateLengthDaysBetweenEpochAndTx(tx.toCenturialT());
        double lunationCapacityVectorFromBaseToX = (tx.julianDay - BASE_NEW_MOON_JDE) / meanL;
        double meanFloorNewMoon = BASE_NEW_MOON_JDE + (Math.floor(lunationCapacityVectorFromBaseToX) * meanL);

        /**
         * Theoretically, from this step on we would get a better result if we used
         * a value of mean L calculated for Tx, not for an interval.
         * However, in the scope of this project it would improve accuracy by lesser than 1 s,
         * which is neglectible - for we're only approximating here.
         */
        double approximate = meanFloorNewMoon + (phase.lunationFraction * meanL);

        double distanceFromApproximateToX = tx.julianDay - approximate;
        if (Math.abs(distanceFromApproximateToX) > 0.5 * meanL) {
            approximate += Math.signum(distanceFromApproximateToX) * meanL;
        }

        return approximate;
    }
}
