package com.github.sigrarr.lunisolarcalc.phenomena.global;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * An idealized phenomenal astronomical cycle.
 * Provides information such as its mean length at the distinguished
 * epochal time point, but also methods for calculating
 * mean lenght proper for a requested time point or interval,
 * and for converting between time and stage-indicating angle in the cycle's progress.
 *
 * Based on polynomial functions of time (in centurial T) for mean length at that time
 * by Laskar (for Sun, 1986) and Chapront-Touzé & Chapront (for Moon, 1988),
 * as given by L.E. Doggett.
 *
 * @see "Seidelmann 1992: Ch. 12 by L.E. Doggett, 12.11-12 (p. 576)"
 */
public enum MeanCycle {
    /**
     * Mean tropical year, a period between subsequent March Equinoxes.
     */
    TROPICAL_YEAR(
        365.2421896698,
        new double[] {-0.00000615359, -0.000000000729, 0.000000000264},
        Timeline.EPOCH_2000_TT
    ),
    /**
     * Mean lunation, a.k.a. synodic month, the cycle of the Moon's phases.
     */
    LUNATION(
        29.5305888531,
        new double[] {0.00000021621, -0.000000000364},
        Timeline.EPOCH_2000_TT
    );

    /**
     * Mean length at the {@link #epoch}, in days. May serve as a sufficient approximate
     * for any time point in the scope of this project, if no great accuracy is needed.
     */
    public final double epochalLengthDays;
    /**
     * Epoch, at wich a mean length is given as constant. Presumably {@linkplain Timeline#EPOCH_2000_TT Epoch 2000}.
     */
    public final TimelinePoint epoch;
    private final double[] lengthFunctionCTPositivePowersCoefficients;

    private MeanCycle(double epochalLengthDays, double[] lengthFunctionCTPositivePowersCoefficients, TimelinePoint epoch) {
        this.epochalLengthDays = epochalLengthDays;
        this.epoch = epoch;
        this.lengthFunctionCTPositivePowersCoefficients = lengthFunctionCTPositivePowersCoefficients;
    }

    /**
     * Calculates mean length at a given time point, in days.
     *
     * @param tx    time argument
     * @return      mean length, in days
     */
    public double calculateLengthDays(TimelinePoint tx) {
        double cTx = tx.toDynamicalTime().toCenturialT();
        double lenght = epochalLengthDays;
        for (int p = 1; p <= lengthFunctionCTPositivePowersCoefficients.length; p++)
            lenght += Math.pow(cTx, p) * lengthFunctionCTPositivePowersCoefficients[p - 1];
        return lenght;
    }

    /**
     * Calculates mean length proper for an interval between given time points,
     * in days.
     *
     * @param a     start of the interval
     * @param b     end of the interval
     * @return      mean length proper for the requested interval, in days
     */
    public double calculateLengthDaysBetween(TimelinePoint a, TimelinePoint b) {
        double cTa = a.toDynamicalTime().toCenturialT();
        double cTb = b.toDynamicalTime().toCenturialT();
        return Double.compare(cTa, cTb) == 0 ? calculateLengthDays(a) :
            (calculateIntegralOfLengthFunction(cTb) - calculateIntegralOfLengthFunction(cTa))
            / (cTb - cTa);
    }

    /**
     * Evaluates indefinite integral of the mean length function at a given time point
     * (assuming the constant of integration C=0).
     *
     * @param cTx   time argument, in centurial T (Julian centuries from the epoch)
     * @return      value of indefinite integral of the mean length function
     *              at the given time point, in days
     */
    protected double calculateIntegralOfLengthFunction(double cTx) {
        return cTx * calculateLengthDaysBetweenEpochAndTx(cTx);
    }

    /**
     * Calculates mean length proper for the interval between the epoch and a given time point Tx,
     * in days. Since T=0 at the epoch, it's equal to the mean length function's integral of Tx divided by Tx.
     *
     * @param cTx   time argument, in centurial T (Julian centuries from the epoch)
     * @return      mean length between the epoch and the given time point, in days
     */
    protected double calculateLengthDaysBetweenEpochAndTx(double cTx) {
        double lenght = epochalLengthDays;
        for (int p = 1; p <= lengthFunctionCTPositivePowersCoefficients.length; p++)
            lenght += Math.pow(cTx, p) * lengthFunctionCTPositivePowersCoefficients[p - 1] / (p + 1);
        return lenght;
    }

    /**
     * Calculates angle corresponding to given amount of time
     * in a mean cycle at the epoch; i.e. the angle value
     * by which a mean cycle progresses during given time.
     *
     * @param seconds           amount of time, in seconds
     * @return                  mean progress angle, in radians
     */
    public double radiansPerTimeSeconds(int seconds) {
        return Calcs.CircularMotion.radiansPerTimeSeconds(epochalLengthDays * Calcs.DAY_SECONDS, seconds);
    }

    /**
     * Calculates angle corresponding to given amount of time
     * in a mean cycle at the epoch; i.e. the angle value
     * by which a mean cycle progresses during given time.
     *
     * @param seconds           amount of time, in seconds
     * @return                  mean progress angle, in degrees
     */
    public double degreesPerTimeSeconds(int seconds) {
        return Calcs.CircularMotion.degreesPerTimeSeconds(epochalLengthDays * Calcs.DAY_SECONDS, seconds);
    }

    /**
     * Calculates amount of time corresponding to given angle
     * in a mean cycle at the epoch; i.e. how much time
     * does it take for a mean cycle to progress by given angle.
     *
     * @param radians           angle, in radians
     * @return                  mean time of progress, in seconds
     */
    public double secondsPerRadians(double radians) {
        return Calcs.CircularMotion.secondsPerRadians(epochalLengthDays * Calcs.DAY_SECONDS, radians);
    }

    /**
     * Calculates amount of time corresponding to given angle
     * in a mean cycle at the epoch; i.e. how much time
     * does it take for a mean cycle to progress by given angle.
     *
     * @param degrees           angle, in degrees
     * @return                  mean time of progress, in seconds
     */
    public double secondsPerDegrees(double degrees) {
        return Calcs.CircularMotion.secondsPerDegrees(epochalLengthDays * Calcs.DAY_SECONDS, degrees);
    }

    /**
     * Calculates angle corresponding to given amount of time
     * in a mean cycle at the epoch; i.e. the angle value
     * by which a mean cycle progresses during given time.
     *
     * @param miliseconds       amount of time, in miliseconds
     * @return                  mean progress angle, in degrees
     */
    public double degreesPerTimeMiliseconds(int miliseconds) {
        return Calcs.CircularMotion.degreesPerTimeMiliseconds(epochalLengthDays * Calcs.DAY_SECONDS, miliseconds);
    }

    /**
     * Calculates angle corresponding to given amount of time
     * in a mean cycle at the epoch; i.e. the angle value
     * by which a mean cycle progresses during given time.
     *
     * @param miliseconds       amount of time, in miliseconds
     * @return                  mean progress angle, in arcseconds
     */
    public double arcsecondsPerTimeMiliseconds(int miliseconds) {
        return Calcs.CircularMotion.arcsecondsPerTimeMiliseconds(epochalLengthDays * Calcs.DAY_SECONDS, miliseconds);
    }
}
