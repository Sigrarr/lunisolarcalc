package com.github.sigrarr.lunisolarcalc.util;

/**
 * Calculation utilities (static).
 */
public abstract class Calcs {
    /**
     * 2π radians (round angle).
     */
    public static final double TURN = 2.0 * Math.PI;
    public static final double DEGREE_ARCSECONDS = 60 * 60;
    public static final double ARCMINUTE_TO_DEGREE = 1.0 / 60.0;
    public static final double ARCSECOND_TO_DEGREE = 1.0 / DEGREE_ARCSECONDS;

    public static final int DAY_MINUTES = 24 * 60;
    public static final double MINUTE_TO_DAY = 1.0 / DAY_MINUTES;
    public static final int DAY_SECONDS = DAY_MINUTES * 60;
    public static final double SECOND_TO_DAY = 1.0 / DAY_SECONDS;

    public static final double EPSILON_9 = 0.000000001;
    public static final double EPSILON_12 = 0.000000000001;
    public static final double EPSILON_16 = 0.0000000000000001;
    public static final double EPSILON = EPSILON_9;
    public static final double EPSILON_MIN = EPSILON_16;

    /**
     * Checks whether two numbers are {@linkplain #equal(double, double, double) equal},
     * applying the default delta ({@value #EPSILON}).
     *
     * @param a     first numer
     * @param b     second number
     * @return      {@code true} - if given numbers are {@linkplain #equal(double, double, double) equal},
     *              applying the default delta ({@value #EPSILON}); {@code false} - otherwise
     */
    public static boolean equal(double a, double b) {
        return equal(a, b, EPSILON);
    }

    /**
     * Checks whether the absolute difference between two numbers is lesser
     * than given delta.
     *
     * @param a         first numer
     * @param b         second number
     * @param delta     comparison delta
     * @return          {@code true} - if the difference between given numbers
     *                  is lesser than given delta; {@code false} - otherwise
     */
    public static boolean equal(double a, double b, double delta) {
        return Math.abs(a - b) < delta;
    }

    /**
     * {@linkplain #compare(double, double, double) Compares} two numbers,
     * applying the default delta ({@value #EPSILON}).
     *
     * @param a     first number
     * @param b     second number
     * @return      the result of {@linkplain #compare(double, double, double) comparison}
     *              of given numbers, applying the default delta ({@value #EPSILON})
     */
    public static int compare(double a, double b) {
        return compare(a, b, EPSILON);
    }

    /**
     * Compares two numbers, considering them equal iff the absolute difference
     * between them is lesser than given delta.
     *
     * @param a         first number
     * @param b         second number
     * @param delta     comparison delta
     * @return          result of comparison of given numbers,
     *                  in the {@linkplain java.util.Comparator#compare(Object, Object) Comparator's format},
     *                  0 iff the numbers are {@linkplain #equal(double, double, double) equal with the given delta}
     */
    public static int compare(double a, double b, double delta) {
        return equal(a, b, delta) ? 0 : (a < b ? -1 : 1);
    }

    /**
     * Rounds a number to the closest multiple of given delta
     * (or doesn't change the number iff the delta is equal to 0).
     *
     * @param number    number to round
     * @param delta     delta
     * @return          given number rounded to the closest multiple
     *                  of given delta (or the unchanged number iff the delta is equal to 0)
     */
    public static double roundToDelta(double number, double delta) {
        return delta == 0 ? number : Math.round(number / delta) * delta;
    }

    public static abstract class Angle {
        /**
         * Normalizes an angle to a proper longitude: [0, 1 turn).
         * Reduces full turns. Converts a negative sub-round angle α to 1 turn subtract α.
         *
         * @param angle         input angle
         * @param scaleTurn     1 turn (round angle) in the same scale as the input angle
         * @return              normalized angle, in the same scale: [0, 1 turn)
         */
        public static double normalizeLongitudinally(double angle, double scaleTurn) {
            return angle - (scaleTurn * Math.floor(angle / scaleTurn));
        }

        /**
         * Normalizes an angle to a proper latitude: [-1/4 turn, 1/4 turn].
         *
         * @param angle         input angle
         * @param scaleTurn     1 turn (round angle) in the same scale as the input angle
         * @return              normalized angle, in the same scale: [-1/4 turn, 1/4 turn]
         */
        public static double normalizeLatitudinally(double angle, double scaleTurn) {
            return angle - (scaleTurn * Math.floor((angle + 0.5*scaleTurn) / scaleTurn));
        }

        /**
         * Normalizes an angle to a proper longitude: [0, 2π).
         * Reduces full turns. Converts a negative sub-round angle α to 2π-α.
         *
         * @param radians   input angle, in radians
         * @return          normalized angle, in radians: [0, 2π)
         */
        public static double normalizeLongitudinally(double radians) {
            return radians - (TURN * Math.floor(radians / TURN));
        }

        /**
         * Normalizes an angle to a proper latitude: [-π/2, π/2].
         *
         * @param radians   input angle, in radians
         * @return          normalized angle, in radians: [-π/2, π/2]
         */
        public static double normalizeLatitudinally(double radians) {
            return radians - (TURN * Math.floor((radians + Math.PI) / TURN));
        }

        /**
         * Converts degrees to arcminutes.
         *
         * @param degrees   angle, in degrees
         * @return          angle, in arcminutes
         */
        public static double toArcminutes(double degrees) {
            return degrees * 60.0;
        }

        /**
         * Converts degrees to arcseconds.
         *
         * @param degrees   angle, in degrees
         * @return          angle, in arcseconds
         */
        public static double toArcseconds(double degrees) {
            return degrees * DEGREE_ARCSECONDS;
        }

        /**
         * Converts arcminutes to degrees.
         *
         * @param arcminutes    angle, in arcminutes
         * @return              angle, in degrees
         */
        public static double arcminutesToDegrees(double arcminutes) {
            return arcminutes * ARCMINUTE_TO_DEGREE;
        }

        /**
         * Converts arcseconds to degrees.
         *
         * @param arcseconds    angle, in arcseconds
         * @return              angle, in degrees
         */
        public static double arcsecondsToDegrees(double arcseconds) {
            return arcseconds * ARCSECOND_TO_DEGREE;
        }

        /**
         * Converts a combined degrees-arcminutes-arcseconds expression to a single number of degrees.
         * In the case of a negative angle, only degrees should be passed as a negative number
         * (negative values of the smaller units may yield unexpected results).
         *
         * Eg.: -90°45′30″ should be passed as -90, 45, 36 and would be converted to -90.51 (°).
         *
         * @param signedDegrees     degrees (number bearing the sign of the input angle)
         * @param absArcminutes     arcminutes (non-negative number)
         * @param absArcseconds     arcseconds (non-negative number)
         * @return                  number of degrees (with fraction)
         */
        public static double toSingleDegreesValue(int signedDegrees, int absArcminutes, double absArcseconds) {
            return signedDegrees + ((arcminutesToDegrees(absArcminutes) + arcsecondsToDegrees(absArcseconds)) * Math.signum(signedDegrees));
        }

        /**
         * Converts a combined degrees-arcminutes-arcseconds expression to a single number of arcseconds.
         * In the case of a negative angle, only degrees should be passed as a negative number
         * (negative values of the smaller units may yield unexpected results).
         *
         * Eg.: -30°40′51″ should be passed as -30, 40, 51 and would be converted to -110451 (″).
         *
         * @param signedDegrees     degrees (number bearing the sign of the input angle)
         * @param absArcminutes     arcminutes (non-negative number)
         * @param absArcseconds     arcseconds (non-negative number)
         * @return                  number of arcseconds
         */
        public static double toSingleArcsecondsValue(int signedDegrees, int absArcminutes, double absArcseconds) {
            return toArcseconds(signedDegrees) + (((60.0 * absArcminutes) + absArcseconds) * Math.signum(signedDegrees));
        }
    }

    public static abstract class Time {
        /**
         * Converts a combined hours-minutes-seconds expression to a single number of days.
         *
         * Eg: 12 hours, 14 minutes and 24 seconds would be converted to 0.51 (of a day).
         *
         * @param h     number of hours (positive)
         * @param min   number of minutes (positive)
         * @param s     number of seconds (positive)
         * @return      number of days (with fraction)
         */
        public static double timeToDays(int h, int min, double s) {
            return SECOND_TO_DAY * (s + (60.0 * (min + (60.0 * h))));
        }

        /**
         * Converts a fraction of day (or number of days) to an array of three integral numbers:
         * whole hours of a day (0-23), whole minutes of an hour (0-59), whole seconds of a minute (0-59).
         *
         * The input number should be as small as possible, to avoid loss of precision.
         *
         * @param dayFraction   fraction of a day (or number of days; positive)
         * @return              array of numbers: whole hours of a day (0-23),
         *                      whole minutes of an hour (0-59), whole seconds of a minute (0-59)
         */
        public static int[] dayFractionToWholeHMinS(double dayFraction) {
            double time = (dayFraction - Math.floor(dayFraction)) * 24;
            int h = (int) Math.floor(time);
            time = (time - h) * 60;
            int m = (int) Math.floor(time);
            time = (time - m) * 60;
            int s = (int) Math.floor(time);

            return new int[] {h, m, s};
        }

        /**
         * Converts a fraction of day (or number of days) to a number of whole hours of a day (0-23).
         *
         * The input number should be as small as possible, to avoid loss of precision.
         *
         * @param dayFraction   fraction of a day (or number of days; positive)
         * @return              number of whole hours of a day (0-23)
         */
        public static int dayFractionToWholeHours(double dayFraction) {
            return (int) (dayFraction * 24) % 24;
        }

        /**
         * Converts a fraction of day (or number of days) to a number of whole minutes of an hour (0-59).
         *
         * The input number should be as small as possible, to avoid loss of precision.
         *
         * @param dayFraction   fraction of a day (or number of days; positive)
         * @return              number of whole minutes of an hour (0-59)
         */
        public static int dayFractionToWholeMinutes(double dayFraction) {
            return (int) (dayFraction * DAY_MINUTES) % 60;
        }

        /**
         * Converts a fraction of day (or number of days) to a number of whole seconds of a minute (0-59).
         *
         * The input number should be as small as possible, to avoid loss of precision.
         *
         * @param dayFraction   fraction of a day (or number of days; positive)
         * @return              number of whole seconds of a minute (0-59)
         */
        public static int dayFractionToWholeSeconds(double dayFraction) {
            return (int) (dayFraction * DAY_SECONDS) % 60;
        }
    }

    public static abstract class CircularMotion {
        /**
         * Calculates angle corresponding to given amount of time
         * in a cycle of given length; i.e. the angle value
         * by which such cycle progresses during given time.
         *
         * @param cycleLengthSeconds    cycle's length, in seconds
         * @param seconds               amount of time, in seconds
         * @return                      progress angle, in radians
         */
        public static double radiansPerTimeSeconds(double cycleLengthSeconds, int seconds) {
            return seconds / cycleLengthSeconds * TURN;
        }

        /**
         * Calculates angle corresponding to given amount of time
         * in a cycle of given length; i.e. the angle value
         * by which such cycle progresses during given time.
         *
         * @param cycleLengthSeconds    cycle's length, in seconds
         * @param seconds               amount of time, in seconds
         * @return                      progress angle, in degrees
         */
        public static double degreesPerTimeSeconds(double cycleLengthSeconds, int seconds) {
            return seconds / cycleLengthSeconds * 360.0;
        }

        /**
         * Calculates amount of time corresponding to given angle
         * in a cycle of given length; i.e. how much time
         * does it take for a cycle to progress by given angle.
         *
         * @param cycleLengthSeconds    cycle's length, in seconds
         * @param radians               angle, in radians
         * @return                      time of progress, in seconds
         */
        public static double secondsPerRadians(double cycleLengthSeconds, double radians) {
            return cycleLengthSeconds * radians / TURN;
        }

        /**
         * Calculates amount of time corresponding to given angle
         * in a cycle of given length; i.e. how much time
         * does it take for a cycle to progress by given angle.
         *
         * @param cycleLengthSeconds    cycle's length, in seconds
         * @param degrees               angle, in degrees
         * @return                      time of progress, in seconds
         */
        public static double secondsPerDegrees(double cycleLengthSeconds, double degrees) {
            return cycleLengthSeconds * degrees / 360.0;
        }

        /**
         * Calculates angle corresponding to given amount of time
         * in a cycle of given length; i.e. the angle value
         * by which such cycle progresses during given time.
         *
         * @param cycleLengthSeconds    cycle's length, in seconds
         * @param miliseconds           amount of time, in miliseconds
         * @return                      progress angle, in degrees
         */
        public static double degreesPerTimeMiliseconds(double cycleLengthSeconds, int miliseconds) {
            return miliseconds / cycleLengthSeconds * 0.36;
        }

        /**
         * Calculates angle corresponding to given amount of time
         * assuming given length of a cycle; i.e. the angle value
         * by which such cycle progresses during given time.
         *
         * @param cycleLengthSeconds    cycle's length, in seconds
         * @param miliseconds           amount of time, in miliseconds
         * @return                      progress angle, in arcseconds
         */
        public static double arcsecondsPerTimeMiliseconds(double cycleLengthSeconds, int miliseconds) {
            return DEGREE_ARCSECONDS * miliseconds / cycleLengthSeconds * 0.36;
        }
    }
}
