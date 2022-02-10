package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint.Calendar;

public class RomanCalendarPointTest {

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
