package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.timescaledelta.BasisMinus700ToPlus2000Resolver;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Operations concerning time scales, based on the equation
 * ΔT = {@linkplain TimeScale#DYNAMICAL TT} - {@linkplain TimeScale#UNIVERSAL UT}.
 * The default method of ΔT evaluation is based on Morrison & Stephenson (2004);
 * it is best fit for years -700 to +2000, but applicable outside this interval too.
 *
 * Remember that ΔT bears serious uncertainty, regarding both the future and the distant past.
 *
 * You can {@linkplain #setResolver set your own resolver of ΔT}.
 *
 * @see TimeScale
 * @see BasisMinus700ToPlus2000Resolver
 * @see "Morrison & Stephenson 2004"
 */
public abstract class TimeScaleDelta {
    /**
     * Resolver of ΔT = {@linkplain TimeScale TT - UT}.
     */
    public static interface Resolver {
        /**
         * Resolves the value of ΔT = {@linkplain TimeScale TT - UT}
         * for the requested Julian Day.
         *
         * @param julianDay         Julian Day for which ΔT is being looked for
         * @param argumentTimeScale time scale of the given Julian Day
         * @return                  ΔT = {@linkplain TimeScale TT - UT}, in seconds
         */
        public double resolveDeltaTSeconds(double julianDay, TimeScale argumentTimeScale);

        /**
         * Resolves the value of ΔT = {@linkplain TimeScale TT - UT}
         * for the requested Julian Day (given in {@linkplain TimeScale#UNIVERSAL Universal Time}).
         *
         * @param julianDay         Julian Day for which ΔT is being looked for
         *                          (in {@linkplain TimeScale#UNIVERSAL Universal Time})
         * @return                  ΔT = {@linkplain TimeScale TT - UT}, in seconds
         */
        public default double resolveDeltaTSeconds(double julianDay) {
            return resolveDeltaTSeconds(julianDay, TimeScale.UNIVERSAL);
        }

        /**
         * Resolves the value of ΔT = {@linkplain TimeScale TT - UT}
         * for the requested timeline point.
         *
         * @param timelinePoint     timeline point for which ΔT is being looked for
         * @return                  ΔT = {@linkplain TimeScale TT - UT}, in seconds
         */
        public default double resolveDeltaTSeconds(TimelinePoint timelinePoint) {
            return resolveDeltaTSeconds(timelinePoint.julianDay, timelinePoint.getTimeScale());
        }

        /**
         * Converts Julian Day to Julian Ephemeris Day
         * (to {@linkplain TimeScale#DYNAMICAL Dynamical Time}).
         *
         * @param julianDay     Julian Day to convert
         *                      (in {@linkplain TimeScale#UNIVERSAL Universal Time})
         * @return              corresponding Julian Ephemeris Day
         *                      (in {@linkplain TimeScale#DYNAMICAL Dynamical Time})
         */
        public default double convertJulianDayToDynamicalTime(double julianDay) {
            return julianDay + Calcs.SECOND_TO_DAY * resolveDeltaTSeconds(julianDay, TimeScale.UNIVERSAL);
        }

        /**
         * Converts Julian Ephemeris Day to Julian Day
         * (to {@linkplain TimeScale#UNIVERSAL Universal Time}).
         *
         * @param julianEphemerisDay    Julian Ephemeris Day to convert
         *                              (in {@linkplain TimeScale#DYNAMICAL Dynamical Time})
         * @return                      corresponding Julian Day
         *                              (in {@linkplain TimeScale#UNIVERSAL Universal Time})
         */
        public default double convertJulianEphemerisDayToUniversalTime(double julianEphemerisDay) {
            return julianEphemerisDay - Calcs.SECOND_TO_DAY * resolveDeltaTSeconds(julianEphemerisDay, TimeScale.DYNAMICAL);
        }
    }

    private static Resolver resolver = new BasisMinus700ToPlus2000Resolver();

    /**
     * Sets a custom resolver of ΔT = {@linkplain TimeScale TT - UT}.
     * Results of time scale conversions will obviously depend on the resolver's accuracy.
     *
     * @param deltaTResolver    new resolver of ΔT = {@linkplain TimeScale TT - UT}
     */
    public static void setResolver(Resolver deltaTResolver) {
        TimeScaleDelta.resolver = deltaTResolver;
    }

    /**
     * Gets the current resolver of ΔT = {@linkplain TimeScale TT - UT}.
     *
     * @return resolver of ΔT = {@linkplain TimeScale TT - UT}
     */
    public static Resolver getResolver() {
        return resolver;
    }

    /**
     * Gets the value of ΔT = {@linkplain TimeScale TT - UT}
     * for the requested Julian Day.
     *
     * @param julianDay         Julian Day for which ΔT is being looked for
     * @param argumentTimeScale time scale of the given Julian Day
     * @return                  ΔT = {@linkplain TimeScale TT - UT}, in seconds
     */
    public static double getDeltaTSeconds(double julianDay, TimeScale argumentTimeScale) {
        return resolver.resolveDeltaTSeconds(julianDay, argumentTimeScale);
    }

    /**
     * Gets the value of ΔT = {@linkplain TimeScale TT - UT}
     * for the requested Julian Day (given in {@linkplain TimeScale#UNIVERSAL Universal Time}).
     *
     * @param julianDay         Julian Day for which ΔT is being looked for
     *                          (in {@linkplain TimeScale#UNIVERSAL Universal Time})
     * @return                  ΔT = {@linkplain TimeScale TT - UT}, in seconds
     */
    public static double getDeltaTSeconds(double julianDay) {
        return resolver.resolveDeltaTSeconds(julianDay);
    }

    /**
     * Gets the value of ΔT = {@linkplain TimeScale TT - UT}
     * for the requested timeline point.
     *
     * @param timelinePoint     timeline point for which ΔT is being looked for
     * @return                  ΔT = {@linkplain TimeScale TT - UT}, in seconds
     */
    public static double getDeltaTSeconds(TimelinePoint timelinePoint) {
        return resolver.resolveDeltaTSeconds(timelinePoint);
    }

    /**
     * Converts Julian Day to Julian Ephemeris Day
     * (to {@linkplain TimeScale#DYNAMICAL Dynamical Time}).
     *
     * @param julianDay     Julian Day to convert
     *                      (in {@linkplain TimeScale#UNIVERSAL Universal Time})
     * @return              corresponding Julian Ephemeris Day
     *                      (in {@linkplain TimeScale#DYNAMICAL Dynamical Time})
     */
    public static double convertJulianDayToDynamicalTime(double julianDay) {
        return resolver.convertJulianDayToDynamicalTime(julianDay);
    }

    /**
     * Converts Julian Ephemeris Day to Julian Day
     * (to {@linkplain TimeScale#UNIVERSAL Universal Time}).
     *
     * @param julianEphemerisDay    Julian Ephemeris Day to convert
     *                              (in {@linkplain TimeScale#DYNAMICAL Dynamical Time})
     * @return                      corresponding Julian Day
     *                              (in {@linkplain TimeScale#UNIVERSAL Universal Time})
     */
    public static double convertJulianEphemerisDayToUniversalTime(double julianEphemerisDay) {
        return resolver.convertJulianEphemerisDayToUniversalTime(julianEphemerisDay);
    }
}
