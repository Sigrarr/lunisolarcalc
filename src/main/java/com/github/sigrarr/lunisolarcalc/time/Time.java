package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.time.DeltaTResolverFitForMinus700ToPlus2000;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Operations concerning time types/scales, based on the equation
 * ΔT = {@link TimeType#DYNAMICAL TT} - {@link TimeType#UNIVERSAL UT}.
 * By default ΔT is evaluated according to the method of Morrison & Stephenson (2004),
 * which is best fit for years -700 to 2000, but applicable outside this interval too.
 * You can also {@link #setDeltaTResolver set your own resolver of ΔT}.
 *
 * @see TimeType
 * @see DeltaTResolverFitForMinus700ToPlus2000
 * @see " Morrison & Stephenson 2004
 */
public abstract class Time {
    /**
     * Resolver of ΔT = {@link TimeType TT - UT}.
     */
    public static interface DeltaTResolver {
        /**
         * Resolves the value of ΔT = {@link TimeType TT - UT}
         * for the requested calendar year.
         *
         * @param calendarYear  calendar year (Julian/Gregorian, in astronomical numbering)
         * @return              ΔT = {@link TimeType TT - UT}
         *                      for that year, in seconds
         */
        public int resolveDeltaTSeconds(int calendarYear);
    }

    private static DeltaTResolver deltaTResolver = new DeltaTResolverFitForMinus700ToPlus2000();

    /**
     * Sets a custom resolver of ΔT = {@link TimeType TT - UT}.
     * Results of time scale conversions will obviously depend on the resolver's accuracy.
     *
     * @param deltaTResolver    new resolver of ΔT = {@link TimeType TT - UT}
     */
    public static void setDeltaTResolver(DeltaTResolver deltaTResolver) {
        Time.deltaTResolver = deltaTResolver;
    }

    /**
     * Gets the value of ΔT = {@link TimeType TT - UT} for the requested calendar year.
     *
     * @param calendarYear  calendar year (Julian/Gregorian, in astronomical numbering)
     * @return              ΔT = {@link TimeType TT - UT} for that year, in seconds
     */
    public static int getDeltaTSeconds(int calendarYear) {
        return deltaTResolver.resolveDeltaTSeconds(calendarYear);
    }

    /**
     * Gets the value of ΔT = {@link TimeType TT - UT} for the requested calendar year.
     *
     * @param calendarYear  calendar year (Julian/Gregorian, in astronomical numbering)
     * @return              ΔT = {@link TimeType TT - UT} for that year, in days
     */
    public static double getDeltaTDays(int calendarYear) {
        return Calcs.SECOND_TO_DAY * deltaTResolver.resolveDeltaTSeconds(calendarYear);
    }

    /**
     * Converts time value between {@link TimeType the supported scales}.
     * The result does not scale a span of time, but shifts a time point.
     *
     * E.g.: assuming that in a given year 12:00:00 UT corresponds to 12:00:13 TT,
     * this method will add 13 or -13 seconds shifting to TT or UT respectively.
     *
     * @param seconds           time value to be shifted, in seconds
     * @param targetTimeType    target type/scale of time, to shift time value to
     * @param calendarYear      calendar year (Julian/Gregorian, in astronomical numbering)
     * @return                  time value in the target time scale corresponding to the argument,
     *                          in seconds
     */
    public static int shiftSecondsToTimeType(int seconds, TimeType targetTimeType, int calendarYear) {
        return seconds + (targetTimeType.conversionDeltaTAddendSign * getDeltaTSeconds(calendarYear));
    }

    /**
     * Converts time value between {@link TimeType the supported scales}.
     * The result does not scale a span of time, but shifts a time point.
     *
     * E.g.: assuming that in a given year 12:00 UT corresponds to 12:20 TT,
     * this method will add 1/72 or -1/72 of a day shifting to TT or UT respectively.
     *
     * @param days              time value to be shifted, in days
     * @param targetTimeType    target type/scale of time, to shift time value to
     * @param calendarYear      calendar year (Julian/Gregorian, in astronomical numbering)
     * @return                  time value in the target time scale corresponding to the argument,
     *                          in days
     */
    public static double shiftDaysToTimeType(double days, TimeType targetTimeType, int calendarYear) {
        return days + (targetTimeType.conversionDeltaTAddendSign * getDeltaTDays(calendarYear));
    }
}
