package com.github.sigrarr.lunisolarcalc.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Calcs {

    public static final double EPSILON_9 = 0.000000001;
    public static final double EPSILON_12 = 0.000000000001;
    public static final double EPSILON_16 = 0.0000000000000001;
    public static final double EPSILON = EPSILON_9;
    public static final double EPSILON_MIN = EPSILON_16;
    public static final int EPSILON_DECIMALS_MAX = 16;
    private static final double DEGREE_TO_ARC_MINUTE = 60.0;
    private static final double DEGREE_TO_ARC_SECOND = DEGREE_TO_ARC_MINUTE * 60.0;
    private static final double ARC_MINUTE_TO_DEGREE = 1.0 / DEGREE_TO_ARC_MINUTE;
    private static final double ARC_SECOND_TO_DEGREE = 1.0 / DEGREE_TO_ARC_SECOND;
    private static final double TWO_PI = 2.0 * Math.PI;
    private final static DecimalFormat FRACTION_PART = new DecimalFormat(".#", DecimalFormatSymbols.getInstance(Locale.ENGLISH)) {{
        setMaximumIntegerDigits(0);
        setMaximumFractionDigits(EPSILON_DECIMALS_MAX);
        setNegativePrefix("");
    }};

    public static boolean equal(double a, double b) {
        return equal(a, b, EPSILON);
    }

    public static boolean equal(double a, double b, double delta) {
        return Math.abs(a - b) < delta;
    }

    public static int compare(double a, double b) {
        return compare(a, b, EPSILON);
    }

    public static int compare(double a, double b, double delta) {
        return equal(a, b) ? 0 : (a < b ? -1 : 1);
    }

    public static double normalizeAngle(double radians) {
        return radians - (TWO_PI * Math.floor(radians / TWO_PI));
    }

    public static double toNormalizedRadians(double degrees) {
        return normalizeAngle(Math.toRadians(degrees));
    }

    public static double toArcMinutes(double degrees) {
        return degrees * DEGREE_TO_ARC_MINUTE;
    }

    public static double toArcSeconds(double degrees) {
        return degrees * DEGREE_TO_ARC_SECOND;
    }

    public static double arcMinutesToDegrees(double arcMinutes) {
        return arcMinutes * ARC_MINUTE_TO_DEGREE;
    }

    public static double arcSecondsToDegrees(double arcSeconds) {
        return arcSeconds * ARC_SECOND_TO_DEGREE;
    }

    public static double autoDelta(double expectedValue) {
        if (Math.abs(expectedValue) < EPSILON_MIN) {
            return EPSILON;
        }
        int decimalPlaces = FRACTION_PART.format(expectedValue).length() - 1;
        return Math.max(Math.pow(10, -decimalPlaces) / 2.0, EPSILON_MIN);
    }
}
