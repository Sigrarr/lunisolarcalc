package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.Assert.*;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import java.util.Arrays;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint.Calendar;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class RomanCalendarPointTest {

    private static final int[] COMMON_YEARS = {750, 1429, 1700, 1800, 1900, 2100};
    private static final int[] LEAP_YEARS = {900, 1236, 1600, 2000, 2400};

    private RomanCalendarPoint pointA1 = new RomanCalendarPoint(8, 1, 1.51);
    private RomanCalendarPoint pointA2 = new RomanCalendarPoint(8, 1, 1.51);
    private RomanCalendarPoint pointB = new RomanCalendarPoint(8, 1, 1.51 + RomanCalendarPoint.MINUTE);
    private RomanCalendarPoint pointC = new RomanCalendarPoint(8, 2, 1.51);

    @Test
    public void shouldRecognizeJulianVsGregorian() {
        assertEquals(Calendar.JULIAN, new RomanCalendarPoint(8, 1, 1).getCalendar());
        assertEquals(Calendar.JULIAN, new RomanCalendarPoint(1582, 10, 4).getCalendar());
        assertEquals(Calendar.GREGORIAN, new RomanCalendarPoint(1582, 10, 15).getCalendar());
        assertEquals(Calendar.GREGORIAN, new RomanCalendarPoint(2000, 1, 1).getCalendar());
    }

    @Test
    public void shouldRecognizeLeapYear() {
        Arrays.stream(COMMON_YEARS).forEach(y -> assertFalse(y + " - false leap year", new RomanCalendarPoint(y, 1, 1).isYearLeap()));
        Arrays.stream(LEAP_YEARS).forEach(y -> assertTrue(y + " - false common year", new RomanCalendarPoint(y, 1, 1).isYearLeap()));
    }

    @Test
    public void shouldCalculateDayOfYear() {
        // Meeus 1998, Example 7.f, p. 65
        assertEquals(318, new RomanCalendarPoint(1978, 11, 14).getDayOfYear());
        // Meeus 1998, Example 7.g, p. 65
        assertEquals(113, new RomanCalendarPoint(1988, 4, 22).getDayOfYear());
    }

    @Test
    public void shouldConvertToYearWithFraction() {
        assertEquals(750.0, new RomanCalendarPoint(750, 1, 1.0).toYearWithFraction(), Calcs.EPSILON);
        assertEquals(1978.86849315, new RomanCalendarPoint(1978, 11, 14).toYearWithFraction(), autoDelta(0.86849315));
        assertEquals(1988.9999990513, new RomanCalendarPoint(1988, 12, 31, 23, 59, 30).toYearWithFraction(), autoDelta(0.9999990513));
    }

    @Test
    public void shouldCompare() {
        assertEquals(0, pointA1.compareTo(pointA2));
        assertTrue("Expected: " + pointB + " > " + pointA1,  pointB.compareTo(pointA1) > 0);
        assertTrue("Expected: " + pointB + " < " + pointC,   pointB.compareTo(pointC)  < 0);
    }

    @Test
    public void shouldReduceDtToPartIntegers() {
        assertEquals( 1, pointA1.getDay());
        assertEquals(12, pointA1.getHours());
        assertEquals(14, pointA1.getMinutes());
    }

    @Test
    public void shouldsEqualIffValuesAreTheSame() {
        assertEquals(pointA1, pointA2);
        assertEquals(pointA2, pointA1);
        assertNotEquals(pointA1, pointB);
        assertNotEquals(pointA2, pointC);
    }

    @Test
    public void shouldHashCodesEqualIffValuesAreTheSame() {
        assertEquals(pointA1.hashCode(), pointA2.hashCode());
        assertNotEquals(pointA1.hashCode(), pointB.hashCode());
        assertNotEquals(pointA2.hashCode(), pointC.hashCode());
    }

    @Test
    public void shouldHandleBasicFormatting() {
        assertEquals("8/01/01", pointA1.formatYMD());
        assertEquals("8/01/01 12:14", pointA2.formatYMDHI());
    }
}
