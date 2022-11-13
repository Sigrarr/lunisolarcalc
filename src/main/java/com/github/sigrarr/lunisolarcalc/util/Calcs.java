package com.github.sigrarr.lunisolarcalc.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Calcs {

    public static final double ROUND = 2.0 * Math.PI;
    public static final double EPSILON_9 = 0.000000001;
    public static final double EPSILON_12 = 0.000000000001;
    public static final double EPSILON_16 = 0.0000000000000001;
    public static final double EPSILON = EPSILON_9;
    public static final double EPSILON_MIN = EPSILON_16;
    public static final int EPSILON_DECIMALS_MAX = 16;
    public static final int DAY_SECONDS = 24 * 60 * 60;
    public static final double SECOND_TO_DAY = 1.0 / DAY_SECONDS;
    public static final double DEGREE_TO_ARC_MINUTE = 60.0;
    public static final double ARC_MINUTE_TO_ARC_SECOND = 60.0;
    public static final double DEGREE_TO_ARC_SECOND = DEGREE_TO_ARC_MINUTE * ARC_MINUTE_TO_ARC_SECOND;
    public static final double ARC_MINUTE_TO_DEGREE = 1.0 / DEGREE_TO_ARC_MINUTE;
    public static final double ARC_SECOND_TO_DEGREE = 1.0 / DEGREE_TO_ARC_SECOND;
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
        return equal(a, b, delta) ? 0 : (a < b ? -1 : 1);
    }

    public static double normalizeLongitudinally(double radians) {
        return radians - (ROUND * Math.floor(radians / ROUND));
    }

    public static double normalizeLatitudinally(double radians) {
        return radians - ROUND * Math.floor((radians + Math.PI) / ROUND);
    }

    public static double toLongitudinallyNormalRadians(double degrees) {
        return normalizeLongitudinally(Math.toRadians(degrees));
    }

    public static double toLatidudinallyNormalRadians(double degrees) {
        return normalizeLatitudinally(Math.toRadians(degrees));
    }

    public static double toArcminutes(double degrees) {
        return degrees * DEGREE_TO_ARC_MINUTE;
    }

    public static double toArcseconds(double degrees) {
        return degrees * DEGREE_TO_ARC_SECOND;
    }

    public static double arcminutesToDegrees(double arcminutes) {
        return arcminutes * ARC_MINUTE_TO_DEGREE;
    }

    public static double arcsecondsToDegrees(double arcseconds) {
        return arcseconds * ARC_SECOND_TO_DEGREE;
    }

    public static double toSingleDegreesValue(int signedDegrees, int absArcminutes, double absArcseconds) {
        return signedDegrees + ((arcminutesToDegrees(absArcminutes) + arcsecondsToDegrees(absArcseconds)) * Math.signum(signedDegrees));
    }

    public static double toSingleArcsecondsValue(int signedDegrees, int absArcminutes, double absArcseconds) {
        return toArcseconds(signedDegrees) + (((ARC_MINUTE_TO_ARC_SECOND * absArcminutes) + absArcseconds) * Math.signum(signedDegrees));
    }

    public static double roundToDelta(double value, double delta) {
        return delta == 0 ? value : Math.round(value / delta) * delta;
    }

    public static double decimalAutoDelta(double expectedFractionalValue) {
        if (Math.abs(expectedFractionalValue) < EPSILON_MIN) {
            return EPSILON;
        }
        String fractionPart = FRACTION_PART.format(expectedFractionalValue);
        int decimalPlaces = fractionPart.equals(".0") ? 0 : fractionPart.length() - 1;
        return Math.max(Math.pow(10, -decimalPlaces) / 2.0, EPSILON_MIN);
    }
}
