package com.github.sigrarr.lunisolarcalc.time.calendar;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.*;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.time.exceptions.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class NormalCalendarPointTest {

    private static final int[][] GENERAL_COMMON_YEARS = {{-3, 1, 750, 1429, 1581}, {1583, 1700, 1900, 2100}};
    private static final int[][] JULIAN_GREGORIAN_LEAP_YEARS = {{-800, -400, -8, -4, 0, 4, 900, 1236}, {1584, 1600, 2000, 2400}};
    private static final int[][] PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS = {{-1300, -900, -500, -100, 100, 200, 900, 1500}, {1700, 1900, 2100, 2300}};
    private static final NormalCalendar[] CALENDARS = {
        CalendarPoint.CALENDAR,
        ProlepticGregorianCalendarPoint.CALENDAR,
        ProlepticJulianCalendarPoint.CALENDAR
    };

    @AfterEach
    public void resetTimelineEquivUnit() {
        Timeline.setEquivUnit(Timeline.DEFAULT_EQUIV_UNIT_DAYS);
    }

    @Test
    public void shouldThrowForInvalidLeapDates() {
        for (int[][] yearArray2d : new int[][][] { GENERAL_COMMON_YEARS, JULIAN_GREGORIAN_LEAP_YEARS, PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS })
            for (int[] yearArray : yearArray2d)
                for (int y : yearArray)
                    for (NormalCalendar calendar : CALENDARS) {
                        NormalCalendarPoint point = calendar.makeCalendarPoint(y, 2, 28.0);
                        if (point.isYearLeap())
                            assertDoesNotThrow(() -> calendar.makeCalendarPoint(y, 2, 29.0));
                        else {
                            InvalidCalendarLeapDayException leapDayException = assertThrows(InvalidCalendarLeapDayException.class,
                                () -> calendar.makeCalendarPoint(y, 2, 29.0));
                            assertEquals(y, leapDayException.getYear());
                            assertEquals(point.getLeapRules(), leapDayException.getLeapRules());
                            assertEquals(point.getCalendar(), leapDayException.getCalendarType());
                        }
                    }
    }

    @Test
    public void shouldRecognizeLeapYears() {
        int[] commonYearsJulianRulesPeriod = GENERAL_COMMON_YEARS[0];
        for (int y : commonYearsJulianRulesPeriod) {
            assertFalse(new CalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
        }
        int[] commonYearsGregorianRulesPeriod = GENERAL_COMMON_YEARS[1];
        for (int y : commonYearsGregorianRulesPeriod) {
            assertFalse(new CalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
        }

        int[] julianLeapYears = JULIAN_GREGORIAN_LEAP_YEARS[0];
        for (int y : julianLeapYears) {
            assertTrue(new CalendarPoint(y, 1, 1).isYearLeap());
            assertTrue(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
        }
        int[] gregorianLeapYears = JULIAN_GREGORIAN_LEAP_YEARS[1];
        for (int y : gregorianLeapYears) {
            assertTrue(new CalendarPoint(y, 1, 1).isYearLeap());
            assertTrue(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
        }

        int[] differenceYearsJulianRulesPeriod = PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS[0];
        for (int y : differenceYearsJulianRulesPeriod) {
            assertTrue(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertTrue(new CalendarPoint(y, 1, 1).isYearLeap());
        }
        int[] differenceYearsGregorianRulesPeriod = PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS[1];
        for (int y : differenceYearsGregorianRulesPeriod) {
            assertTrue(new ProlepticJulianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new ProlepticGregorianCalendarPoint(y, 1, 1).isYearLeap());
            assertFalse(new CalendarPoint(y, 1, 1).isYearLeap());
        }
    }

    @Test
    public void shouldNumberOfDaysInYearBe366IffLeap365Otherwise() {
        for (int[][] yearArray2d : new int[][][] { GENERAL_COMMON_YEARS, JULIAN_GREGORIAN_LEAP_YEARS, PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS })
            for (int[] yearArray : yearArray2d)
                for (int y : yearArray)
                    for (NormalCalendar calendar : CALENDARS) {
                        NormalCalendarPoint point = calendar.makeCalendarPoint(y, 1, 1);
                        assertEquals(point.isYearLeap() ? 366 : 365, point.getNumberOfDaysInYear());
                    }
    }

    @Test
    public void shouldGetMonthLength() {
        for (int[] monthAndExpectedDaysCommon : new int[][] {
            {1, 31}, {2, 28}, {3, 31}, {4, 30}, {5, 31}, {6, 30},
            {7, 31}, {8, 31}, {9, 30}, {10, 31}, {11, 30}, {12, 31}
        })
        for (int y : GENERAL_COMMON_YEARS[0])
            assertEquals(
                monthAndExpectedDaysCommon[1],
                new ProlepticGregorianCalendarPoint(y, monthAndExpectedDaysCommon[0], 1).getNumberOfDaysInMonth()
            );

        for (int y : JULIAN_GREGORIAN_LEAP_YEARS[0])
            assertEquals(29, new ProlepticJulianCalendarPoint(y, 2, 1).getNumberOfDaysInMonth());
    }

    @Test
    public void shouldCalculateDayOfYear() {
        // Meeus 1998, Example 7.f, p. 65
        assertEquals(318, new CalendarPoint(1978, 11, 14).getDayOfYear());
        // Meeus 1998, Example 7.g, p. 65
        assertEquals(113, new CalendarPoint(1988, 4, 22).getDayOfYear());

        for (int[] differenceYearArray : PJULIAN_LEAP_PGREGORIAN_COMMON_YEARS)
            for (int y : differenceYearArray) {
                int pJulianDayOfYear = new ProlepticJulianCalendarPoint(y, 3, 1).getDayOfYear();
                int pGregorianDayOfYear = new ProlepticGregorianCalendarPoint(y, 3, 1).getDayOfYear();
                assertEquals(1, pJulianDayOfYear - pGregorianDayOfYear);
            }
    }

    @Test
    public void shouldEquate() {
        NormalCalendarPoint x = new CalendarPoint(-45, 12, 25.51);
        assertEquivalence(x, new CalendarPoint(-45, 12, 25, 12, 14, 24));
        assertNonEquivalence(x, new CalendarPoint(x.y, x.m, x.dt + Timeline.getEquivUnitDays() + Calcs.EPSILON));
        assertNonEquivalence(x, new CalendarPoint(x.y, x.m, x.dt - Timeline.getEquivUnitDays() - Calcs.EPSILON));

        for (NormalCalendar calendar : CALENDARS) {
            assertEquivalence(calendar.makeCalendarPoint(-45, 12, 25.51), calendar.makeCalendarPoint(-45, 12, 25.51));
            for (NormalCalendar anotherCalendar : CALENDARS)
                if (anotherCalendar != calendar)
                    assertNonEquivalence(anotherCalendar.makeCalendarPoint(0, 1, 1), calendar.makeCalendarPoint(0, 1, 1));
        }

        Timeline.setEquivUnit(1.0);
        x = new ProlepticGregorianCalendarPoint(1582, 10, 15);
        assertEquivalence(x, new ProlepticGregorianCalendarPoint(x.y, x.m, x.dt + 0.5 - Calcs.EPSILON));
        assertNonEquivalence(x, new ProlepticGregorianCalendarPoint(x.y, x.m, x.dt + 1.0));
        x = new ProlepticJulianCalendarPoint(2000, 11, 11);
        assertEquivalence(x, new ProlepticJulianCalendarPoint(x.y, x.m, x.dt - 0.5 + Calcs.EPSILON));
        assertNonEquivalence(x, new ProlepticJulianCalendarPoint(x.y, x.m, x.dt - 1.0));
    }
}
