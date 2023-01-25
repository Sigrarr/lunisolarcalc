package com.github.sigrarr.lunisolarcalc.time;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelineTest
{
    private static final Map<CalendarPoint, Double> CALENDAR_TO_JD = new HashMap<CalendarPoint, Double>() {{
        // Meeus 1998, Example 7.a-b, 47.a, p. 61, 342; Ch. 7, pp. 62, 64
        put(new CalendarPoint( 1957, 10,  4.81), 2436116.31);
        put(new CalendarPoint(  333,  1, 27.5 ), 1842713.0 );
        put(new CalendarPoint( 2000,  1,  1.5 ), 2451545.0 );
        put(new CalendarPoint( 1999,  1,  1.0 ), 2451179.5 );
        put(new CalendarPoint( 1987,  1, 27.0 ), 2446822.5 );
        put(new CalendarPoint( 1987,  6, 19.5 ), 2446966.0 );
        put(new CalendarPoint( 1988,  1, 27.0 ), 2447187.5 );
        put(new CalendarPoint( 1988,  6, 19.5 ), 2447332.0 );
        put(new CalendarPoint( 1900,  1,  1.0 ), 2415020.5 );
        put(new CalendarPoint( 1600,  1,  1.0 ), 2305447.5 );
        put(new CalendarPoint( 1600, 12, 31.0 ), 2305812.5 );
        put(new CalendarPoint(  837,  4, 10.3 ), 2026871.8 );
        put(new CalendarPoint( -123, 12, 31.0 ), 1676496.5 );
        put(new CalendarPoint( -122,  1,  1.0 ), 1676497.5 );
        put(new CalendarPoint(-1000,  7, 12.5 ), 1356001.0 );
        put(new CalendarPoint(-1000,  2, 29.0 ), 1355866.5 );
        put(new CalendarPoint(-1001,  8, 17.9 ), 1355671.4 );
        put(new CalendarPoint(-4712,  1,  1.5 ),       0.0 );
        put(new CalendarPoint( -584,  5, 28.63), 1507900.13);
        put(new CalendarPoint( 1992,  4, 12.0 ), 2448724.5 );
        // L.E. Doggett 1992, 12.7 in ESAA
        put(new CalendarPoint(1990, 6, 25.5), 2448068.0 );
    }};

    @Test
    public void shouldProperlyConvertBetweenMainCalendarAndJD() {
        CALENDAR_TO_JD.forEach((cp, jd) -> assertEquals(jd.doubleValue(), Timeline.calendarToJulianDay(cp), Calcs.EPSILON));
        CALENDAR_TO_JD.forEach((cp, jd) -> assertEquals(cp, Timeline.julianDayToCalendar(jd)));
    }

    @Test
    public void shouldConversionBetweenJDAndCalendarsBeReversible() {
        for (double jd = 0.0; jd <= Timeline.JULIAN_PERIOD_END_JD; jd += 1.0) {
            CalendarPoint c = Timeline.julianDayToCalendar(jd);
            assertEquals(jd, Timeline.calendarToJulianDay(c), Calcs.EPSILON);

            ProlepticJulianCalendarPoint pj = Timeline.julianDayToProlepticJulianCalendar(jd);
            assertEquals(jd, Timeline.calendarToJulianDay(pj), Calcs.EPSILON);

            ProlepticGregorianCalendarPoint pg = Timeline.julianDayToProlepticGregorianCalendar(jd);
            assertEquals(jd, Timeline.calendarToJulianDay(pg), Calcs.EPSILON);
        }
    }

    @Test
    public void shouldConvertBetweenJDAndTau() {
        // Meeus 1998, Example 32.a, p. 219
        double tau = -0.007032169747;
        double jd = 2448976.5;
        assertEquals(tau, Timeline.julianDayToMillenialTau(jd), decimalAutoDelta(tau));
        assertEquals(jd, Timeline.millenialTauToJulianDay(tau), decimalAutoDelta(jd));
    }

    @Test
    public void shouldCovertBetweenJDAndT() {
        // Meeus 1998, Example 22.a, p. 148
        double cT = -0.127296372348;
        double jd = 2446895.5;
        assertEquals(cT, Timeline.julianDayToCenturialT(jd), decimalAutoDelta(cT));
        assertEquals(jd, Timeline.centurialTToJulianDay(cT), decimalAutoDelta(jd));
        // Meeus 1998, Example 47.a, p. 342
        cT = -0.077221081451;
        jd = 2448724.5;
        assertEquals(cT, Timeline.julianDayToCenturialT(jd), decimalAutoDelta(cT));
        assertEquals(jd, Timeline.centurialTToJulianDay(cT), decimalAutoDelta(jd));
    }

    @Test
    public void shouldConvertBetweenTauAndT() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            double jd = random.nextDouble() * Timeline.JULIAN_PERIOD_END_JD;
            double cT = Timeline.julianDayToCenturialT(jd);
            double tau = Timeline.julianDayToMillenialTau(jd);
            assertEquals(cT, Timeline.millenialTauToCenturialT(tau), Calcs.EPSILON_12);
            assertEquals(tau, Timeline.centurialTToMillenialTau(cT), Calcs.EPSILON_12);
        }
    }

    @Test
    public void shouldMainTimePointsBeProperlyDefined() {
        assertEquals(0.0, Timeline.JULIAN_PERIOD_START_JD);
        assertEquals(Timeline.calendarToJulianDay(CalendarPoint.GREGORIAN_RULES_START), Timeline.GREGORIAN_CALENDAR_START_JD);
        assertEquals(new CalendarPoint(2000, 1, 1, 12, 0, 0), Timeline.julianDayToCalendar(Timeline.EPOCH_2000_JD));

        assertEquals(Timeline.JULIAN_PERIOD_START_TT.julianDay, Timeline.JULIAN_PERIOD_START_UT.julianDay);
        assertEquals(Timeline.EPOCH_2000_TT.julianDay, Timeline.EPOCH_2000_UT.julianDay);
        assertEquals(Timeline.JULIAN_PERIOD_END_TT.julianDay, Timeline.JULIAN_PERIOD_END_UT.julianDay);
    }

    @Test
    public void shouldJulianPeriodExtremaBeConsistentWithProlepticJulianCalendar() {
        assertEquals(
            Timeline.JULIAN_PERIOD_START_JD,
            Timeline.calendarToJulianDay(
                new ProlepticJulianCalendarPoint(-4712, 1, 1, 12, 0, 0)
            )
        );
        assertEquals(
            new ProlepticJulianCalendarPoint(-4712 + Timeline.JULIAN_PERIOD_YEARS, 1, 1, 12, 0, 0),
            Timeline.julianDayToProlepticJulianCalendar(Timeline.JULIAN_PERIOD_END_JD)
        );
    }

    @Test
    public void shouldMaintainProperDiffBetweenGregorianAndProlepticJulianDatesForwards() {
        int startY = CalendarPoint.GREGORIAN_RULES_START.y + 1;
        int limitY = -4712 + Timeline.JULIAN_PERIOD_YEARS + 1;
        int expectedDiff = 10;
        int checksCount = 0;
        for (int y = startY; y < limitY; y++, checksCount++) {
            ProlepticJulianCalendarPoint pjFeb28 = new ProlepticJulianCalendarPoint(y, 2, 28);
            CalendarPoint gFeb28 = new CalendarPoint(y, 2, 28);
            ProlepticJulianCalendarPoint pjMar1 = new ProlepticJulianCalendarPoint(y, 3, 1);
            CalendarPoint gMar1 = new CalendarPoint(y, 3, 1);

            double jdOfPJ = Timeline.calendarToJulianDay(pjFeb28);
            double jdOfG = Timeline.calendarToJulianDay(gFeb28);
            assertEquals(expectedDiff, jdOfPJ - jdOfG, "WRONG AT " + y + " Feb 28");
            assertEquals(0, NormalCalendaricExpression.nominalComparator().compare(gFeb28, Timeline.julianDayToProlepticGregorianCalendar(jdOfG)));

            if (gFeb28.isYearLeap() != pjFeb28.isYearLeap()) {
                expectedDiff++;
            }

            jdOfPJ = Timeline.calendarToJulianDay(pjMar1);
            jdOfG = Timeline.calendarToJulianDay(gMar1);
            assertEquals(expectedDiff, jdOfPJ - jdOfG, "WRONG AT " + y + " Mar 1");
            assertEquals(0, NormalCalendaricExpression.nominalComparator().compare(gMar1, Timeline.julianDayToProlepticGregorianCalendar(jdOfG)));
        }

        assertEquals(limitY - startY, checksCount);
    }

    @Test
    public void shouldMaintainProperDiffBetweenProlepticGregorianAndJulianDatesBackwards() {
        int startY = CalendarPoint.GREGORIAN_RULES_START.y;
        int limitY = -4712 - 1;
        int expectedDiff = 10;
        int checksCount = 0;
        for (int y = startY; y > limitY; y--, checksCount++) {
            ProlepticJulianCalendarPoint pjFeb28 = new ProlepticJulianCalendarPoint(y, 2, 28);
            ProlepticGregorianCalendarPoint pgFeb28 = new ProlepticGregorianCalendarPoint(y, 2, 28);
            ProlepticJulianCalendarPoint pjMar1 = new ProlepticJulianCalendarPoint(y, 3, 1);
            ProlepticGregorianCalendarPoint pgMar1 = new ProlepticGregorianCalendarPoint(y, 3, 1);

            double jdOfPJ = Timeline.calendarToJulianDay(pjMar1);
            double jdOfPG = Timeline.calendarToJulianDay(pgMar1);
            assertEquals(expectedDiff, jdOfPJ - jdOfPG, "WRONG AT " + y + " Mar 1");
            assertEquals(0, NormalCalendaricExpression.nominalComparator().compare(pjMar1, Timeline.julianDayToCalendar(jdOfPJ)));

            if (pjFeb28.isYearLeap() != pgFeb28.isYearLeap()) {
                expectedDiff--;
            }

            jdOfPJ = Timeline.calendarToJulianDay(pjFeb28);
            jdOfPG = Timeline.calendarToJulianDay(pgFeb28);
            assertEquals(expectedDiff, jdOfPJ - jdOfPG, "WRONG AT " + y + " Feb 28");
            assertEquals(0, NormalCalendaricExpression.nominalComparator().compare(pjFeb28, Timeline.julianDayToCalendar(jdOfPJ)));
        }

        assertEquals(startY - limitY, checksCount);
    }

    @Test
    public void shouldHandleJulianGregorian1582OctOffsetAndItsLackInProlepticCalendars() {
        double jdBeforeGregorianEpoch = Timeline.GREGORIAN_CALENDAR_START_JD - 1.0;

        assertEquals(new CalendarPoint(1582, 10, 4), Timeline.julianDayToCalendar(jdBeforeGregorianEpoch));
        assertEquals(new ProlepticJulianCalendarPoint(1582, 10, 4), Timeline.julianDayToProlepticJulianCalendar(jdBeforeGregorianEpoch));
        assertEquals(new ProlepticGregorianCalendarPoint(1582, 10, 14), Timeline.julianDayToProlepticGregorianCalendar(jdBeforeGregorianEpoch));
        assertEquals(jdBeforeGregorianEpoch, Timeline.calendarToJulianDay(new CalendarPoint(1582, 10, 4)));
        assertEquals(jdBeforeGregorianEpoch, Timeline.calendarToJulianDay(new ProlepticJulianCalendarPoint(1582, 10, 4)));
        assertEquals(jdBeforeGregorianEpoch, Timeline.calendarToJulianDay(new ProlepticGregorianCalendarPoint(1582, 10, 14)));

        assertEquals(new ProlepticJulianCalendarPoint(1582, 10, 5), Timeline.julianDayToProlepticJulianCalendar(Timeline.GREGORIAN_CALENDAR_START_JD));
        assertEquals(Timeline.GREGORIAN_CALENDAR_START_JD, Timeline.calendarToJulianDay(new ProlepticJulianCalendarPoint(1582, 10, 5)));
    }
}
