package com.github.sigrarr.lunisolarcalc.time.julianform;

import static org.junit.jupiter.api.Assertions.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.*;

public class JulianformCalendarPointTest {

    private static final int[][] GREGORIAN_COMMON_YEARS = {{1, 750, 1429, 1581}, {1583, 1700, 1900, 2100}};
    private static final int[][] GREGORIAN_LEAP_YEARS = {{0, 4, 900, 1236}, {1584, 1600, 2000, 2400}};
    private static final int[][] PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS = {{100, 200, 900, 1500}, {1700, 1900, 2100, 2300}};

    @AfterEach
    public void cleanUpEach() {
        JulianformCalendarPoint.setComparisonDeltaDays(JulianformCalendarPoint.DEFAULT_COMPARISON_DELTA_DAYS);
    }

    @Test
    public void shouldCompareNominally() {
        assertEquals(0, JulianformCalendarPoint.compareNominally(new GregorianCalendarPoint(1000, 1, 1), new ProlepticGregorianCalendarPoint(1000, 1, 1)));
        assertEquals(0, JulianformCalendarPoint.compareNominally(new ProlepticJulianCalendarPoint(1800, 12, 12), new GregorianCalendarPoint(1800, 12, 12)));
        int cmp = JulianformCalendarPoint.compareNominally(new GregorianCalendarPoint(1000, 1, 1.0), new ProlepticGregorianCalendarPoint(1000, 1, 1.099));
        assertTrue(cmp < 0);
        cmp = JulianformCalendarPoint.compareNominally(new GregorianCalendarPoint(8, 5, 17), new ProlepticJulianCalendarPoint(8, 5, 16));
        assertTrue(cmp > 0);

        JulianformCalendarPoint.setComparisonDeltaDays(0.1);
        assertEquals(0, JulianformCalendarPoint.compareNominally(new GregorianCalendarPoint(1000, 1, 1.0), new ProlepticGregorianCalendarPoint(1000, 1, 1.099)));
    }

    @Test
    public void shouldRecognizeLeapYears() {
        int[] gregorianCommonYearsJulianRulesPeriod = GREGORIAN_COMMON_YEARS[0];
        for (int y : gregorianCommonYearsJulianRulesPeriod) {
            assertFalse(new GregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
        }
        int[] gregorianCommonYearsGregorianRulesPeriod = GREGORIAN_COMMON_YEARS[1];
        for (int y : gregorianCommonYearsGregorianRulesPeriod) {
            assertFalse(new GregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
        }

        int[] gregorianLeapYearsJulianRulesPeriod = GREGORIAN_LEAP_YEARS[0];
        for (int y : gregorianLeapYearsJulianRulesPeriod) {
            assertTrue(new GregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertTrue(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
        }
        int[] gregorianLeapYearsGregorianRulesPeriod = GREGORIAN_LEAP_YEARS[1];
        for (int y : gregorianLeapYearsGregorianRulesPeriod) {
            assertTrue(new GregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertTrue(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
        }

        int[] differenceYearsJulianRulesPeriod = PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS[0];
        for (int y : differenceYearsJulianRulesPeriod) {
            assertTrue(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertTrue(new GregorianCalendarPoint(y, 1, 1).isYearLeap());
        }
        int[] differenceYearsGregorianRulesPeriod = PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS[1];
        for (int y : differenceYearsGregorianRulesPeriod) {
            assertTrue(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new GregorianCalendarPoint(y, 1, 1).isYearLeap());
        }        
    }

    @Test
    public void shouldNumberOfDaysInYearBe366IffLeap365Otherwise() {
        JulianformCalendarPoint[] points;
        int[][][] yearArray3d = {GREGORIAN_COMMON_YEARS, GREGORIAN_LEAP_YEARS, PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS};
        for (int[][] yearArray2d : yearArray3d)
            for (int[] yearArray : yearArray2d)
                for (int y : yearArray) {
                    points = new JulianformCalendarPoint[] {new GregorianCalendarPoint(y, 1, 1), new ProlepticGregorianCalendarPoint(y, 1, 1), new ProlepticJulianCalendarPoint(y, 1, 1)};
                    for (JulianformCalendarPoint point : points)
                        assertEquals(point.isYearLeap() ? 366 : 365, point.getNumberOfDaysInYear());
                }
    }

    @Test
    public void shouldCalculateDayOfYear() {
        // Meeus 1998, Example 7.f, p. 65
        assertEquals(318, new GregorianCalendarPoint(1978, 11, 14).getDayOfYear());
        // Meeus 1998, Example 7.g, p. 65
        assertEquals(113, new GregorianCalendarPoint(1988, 4, 22).getDayOfYear());

        for (int[] differenceYearArray : PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS)
            for (int y : differenceYearArray) {
                int pJulianDayOfYear = new ProlepticJulianCalendarPoint(y, 3, 1).getDayOfYear();
                int pGregorianDayOfYear = new ProlepticGregorianCalendarPoint(y, 3, 1).getDayOfYear();
                assertEquals(1, pJulianDayOfYear - pGregorianDayOfYear);
            }
    }

    @Test
    public void shouldConvertToYearWithFraction() {
        assertEquals(750.0, new GregorianCalendarPoint(750, 1, 1.0).toYearWithFraction(), Calcs.EPSILON);
        assertEquals(1978.86849315, new GregorianCalendarPoint(1978, 11, 14).toYearWithFraction(), decimalAutoDelta(0.86849315));
        assertEquals(1988.9999990513, new GregorianCalendarPoint(1988, 12, 31, 23, 59, 30).toYearWithFraction(), decimalAutoDelta(0.9999990513));
    }

    @Test
    public void shouldReduceDtToPartialIntegers() {
        JulianformCalendarPoint point = new GregorianCalendarPoint(8, 1, 1.51);
        assertEquals( 1, point.getDay());
        assertEquals(12, point.getHours());
        assertEquals(14, point.getMinutes());
    }

    @Test
    public void shouldHandleBasicFormatting() {
        JulianformCalendarPoint point = new GregorianCalendarPoint(8, 1, 1.51);
        assertEquals("8/01/01", point.formatYMD());
        assertEquals("8/01/01 12:14", point.formatYMDHMin());
    }

    @Test
    public void shouldIdentifyIffRulesAndNominalValuesAreEqualWithDelta() {
        JulianformCalendarPoint a, b;
        a = new GregorianCalendarPoint(8, 1, 1.0);
        b = new GregorianCalendarPoint(8, 1, 1.0);
        assertIdentity(a, b);
        b = new ProlepticJulianCalendarPoint(8, 1, 1.0);
        assertIdentity(a, b);
        b = new ProlepticGregorianCalendarPoint(8, 1, 1.0);
        assertDifferentIdentity(a, b);
        b = new GregorianCalendarPoint(8, 1, 1.0 + JulianformCalendarPoint.DEFAULT_COMPARISON_DELTA_DAYS - Calcs.EPSILON);
        assertIdentity(a, b);
        b = new GregorianCalendarPoint(8, 1, 1.0 + JulianformCalendarPoint.DEFAULT_COMPARISON_DELTA_DAYS + Calcs.EPSILON);
        assertDifferentIdentity(a, b);
        JulianformCalendarPoint.setComparisonDeltaDays(1.0);
        assertIdentity(a, b);
        a = new GregorianCalendarPoint(1600, 12, 12.5);
        b = new ProlepticGregorianCalendarPoint(1600, 12, 12.75);
        assertIdentity(a, b);
        b = new ProlepticJulianCalendarPoint(1600, 12, 12.5);
        assertDifferentIdentity(a, b);
    }

    private void assertIdentity(Object a, Object b) {
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());        
    }

    private void assertDifferentIdentity(Object a, Object b) {
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());        
    }
}
