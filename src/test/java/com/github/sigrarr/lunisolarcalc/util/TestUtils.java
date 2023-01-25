package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;

import java.text.*;
import java.util.*;

public abstract class TestUtils {

    public static final int EPSILON_MIN_DECIMALS = 16;

    private final static DecimalFormat fractionPartFormat = new DecimalFormat(".#", DecimalFormatSymbols.getInstance(Locale.ENGLISH)) {{
        setMaximumIntegerDigits(0);
        setMaximumFractionDigits(EPSILON_MIN_DECIMALS);
        setNegativePrefix("");
    }};

    public static double decimalAutoDelta(double expectedFractionalValue) {
        if (Math.abs(expectedFractionalValue) < Calcs.EPSILON_MIN) {
            return Calcs.EPSILON;
        }
        String fractionPart = fractionPartFormat.format(expectedFractionalValue);
        int decimalPlaces = fractionPart.equals(".0") ? 0 : fractionPart.length() - 1;
        return Math.max(Math.pow(10, -decimalPlaces) / 2.0, Calcs.EPSILON_MIN);
    }

    public static void assertEquivalence(Object expected, Object actual) {
        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());
    }

    public static void assertNonEquivalence(Object unexpected, Object actual) {
        assertNotEquals(unexpected, actual);
        assertNotEquals(unexpected.hashCode(), actual.hashCode());
    }

    public static <T extends Comparable<T>> void assertConsistentEquivalence(T expected, T actual) {
        assertEquivalence(expected, actual);
        assertEquals(0, expected.compareTo(actual));
        assertEquals(0, actual.compareTo(expected));
    }

    public static <G, T extends G> void assertConsistentEquivalence(T a, T b, Comparator<G> comparator) {
        assertEquivalence(a, b);
        assertEquals(0, comparator.compare(a, b));
        assertEquals(0, comparator.compare(b, a));
    }

    public static <T extends Comparable<T>> void assertConsistentNonEquivalence(T unexpected, T actual) {
        assertNonEquivalence(unexpected, actual);
        assertNotEquals(0, unexpected.compareTo(actual));
        assertNotEquals(0, actual.compareTo(unexpected));
    }

    public static <G, T extends G> void assertConsistentNonEquivalence(T unexpected, T actual, Comparator<G> comparator) {
        assertNonEquivalence(unexpected, actual);
        assertNotEquals(0, comparator.compare(unexpected, actual));
        assertNotEquals(0, comparator.compare(actual, unexpected));
    }
}
