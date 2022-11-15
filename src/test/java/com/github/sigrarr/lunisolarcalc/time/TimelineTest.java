package com.github.sigrarr.lunisolarcalc.time;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.julianform.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelineTest
{
    /**
     * Meeus 1998, Example 7.a-b, 47.a, p. 61, 342; Ch. 7, pp. 62, 64
     */
    private static final Map<GregorianCalendarPoint, Double> GREGORIAN_CALENDAR_TO_JD = new HashMap<GregorianCalendarPoint, Double>() {{
        put(new GregorianCalendarPoint( 1957, 10,  4.81), 2436116.31);
        put(new GregorianCalendarPoint(  333,  1, 27.5 ), 1842713.0 );
        put(new GregorianCalendarPoint( 2000,  1,  1.5 ), 2451545.0 );
        put(new GregorianCalendarPoint( 1999,  1,  1.0 ), 2451179.5 );
        put(new GregorianCalendarPoint( 1987,  1, 27.0 ), 2446822.5 );
        put(new GregorianCalendarPoint( 1987,  6, 19.5 ), 2446966.0 );
        put(new GregorianCalendarPoint( 1988,  1, 27.0 ), 2447187.5 );
        put(new GregorianCalendarPoint( 1988,  6, 19.5 ), 2447332.0 );
        put(new GregorianCalendarPoint( 1900,  1,  1.0 ), 2415020.5 );
        put(new GregorianCalendarPoint( 1600,  1,  1.0 ), 2305447.5 );
        put(new GregorianCalendarPoint( 1600, 12, 31.0 ), 2305812.5 );
        put(new GregorianCalendarPoint(  837,  4, 10.3 ), 2026871.8 );
        put(new GregorianCalendarPoint( -123, 12, 31.0 ), 1676496.5 );
        put(new GregorianCalendarPoint( -122,  1,  1.0 ), 1676497.5 );
        put(new GregorianCalendarPoint(-1000,  7, 12.5 ), 1356001.0 );
        put(new GregorianCalendarPoint(-1000,  2, 29.0 ), 1355866.5 );
        put(new GregorianCalendarPoint(-1001,  8, 17.9 ), 1355671.4 );
        put(new GregorianCalendarPoint(-4712,  1,  1.5 ),       0.0 );
        put(new GregorianCalendarPoint( -584,  5, 28.63), 1507900.13);
        put(new GregorianCalendarPoint( 1992,  4, 12.0 ), 2448724.5 );
    }};

    @Test
    public void shouldProperlyConvertGregorianCalendarToJD() {
        GREGORIAN_CALENDAR_TO_JD.forEach((gc, jd) -> assertEquals(jd.doubleValue(), Timeline.calendarToJulianDay(gc), Calcs.EPSILON));
    }

    @Test
    public void shouldProperlyConvertJDtoGregorianCalendar() {
        GREGORIAN_CALENDAR_TO_JD.forEach((gc, jd) -> assertEquals(gc, Timeline.julianDayToGregorianCalendar(jd)));
    }

    @Test
    public void shouldConversionBetweenJDAndCalendarsBeReversible() {
        Random random = new Random();
        for (double jd = Timeline.JULIAN_PERIOD_START_JD; jd <= Timeline.JULIAN_PERIOD_END_JD; jd += (0.5 + (random.nextDouble() * 9.5))) {
            GregorianCalendarPoint g = Timeline.julianDayToGregorianCalendar(jd);
            assertEquals(jd, Timeline.calendarToJulianDay(g), Calcs.EPSILON);

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
        double t = -0.127296372348;
        double jd = 2446895.5;
        assertEquals(t, Timeline.julianDayToCenturialT(jd), decimalAutoDelta(t));
        assertEquals(jd, Timeline.centurialTToJulianDay(t), decimalAutoDelta(jd));
        // Meeus 1998, Example 47.a, p. 342
        t = -0.077221081451;
        jd = 2448724.5;
        assertEquals(t, Timeline.julianDayToCenturialT(jd), decimalAutoDelta(t));
        assertEquals(jd, Timeline.centurialTToJulianDay(t), decimalAutoDelta(jd));
    }

    @Test
    public void shouldConvertBetweenTauAndT() {
        Random random = new Random();
        double limitJD = Timeline.calendarToJulianDay(new GregorianCalendarPoint(2200, 12, 31));
        for (int i = 0; i < 10; i++) {
            double jd = random.nextDouble() * limitJD;
            double cT = Timeline.julianDayToCenturialT(jd);
            double tau = Timeline.julianDayToMillenialTau(jd);
            assertEquals(cT, Timeline.millenialTauToCenturialT(tau), Calcs.EPSILON_12);
            assertEquals(tau, Timeline.centurialTToMillenialTau(cT), Calcs.EPSILON_12);
        }
    }

    @Test
    public void shouldJulianPeriodExtremaBeConsistentWithProlepticJulianCalendar() {
        assertEquals(
            Timeline.JULIAN_PERIOD_START_JD,
            Timeline.calendarToJulianDay(
                new ProlepticJulianCalendarPoint(Timeline.JULIAN_PERIOD_START_Y, 1, 1, 12, 0, 0)
            )
        );
        assertEquals(
            new ProlepticJulianCalendarPoint(Timeline.JULIAN_PERIOD_START_Y + Timeline.JULIAN_PERIOD_YEARS, 1, 1, 12, 0, 0),
            Timeline.julianDayToProlepticJulianCalendar(Timeline.JULIAN_PERIOD_END_JD)
        );
    }

    @Test
    public void shouldMaintainProperDiffBetweenGregorianAndProlepticJulianDatesForwards() {
        int startY = GregorianCalendarPoint.EPOCH_GREGORIAN_RULES.y + 1;
        int limitY = Timeline.JULIAN_PERIOD_START_Y + Timeline.JULIAN_PERIOD_YEARS + 1;
        int expectedDiff = 10;
        int checksCount = 0;
        for (int y = startY; y < limitY; y++, checksCount++) {
            GregorianCalendarPoint gMarch1 = new GregorianCalendarPoint(y, 3, 1);
            ProlepticJulianCalendarPoint pjMarch1 = new ProlepticJulianCalendarPoint(y, 3, 1);

            if (gMarch1.isYearLeap() != pjMarch1.isYearLeap()) {
                expectedDiff++;
            }

            double jdOfPJ = Timeline.calendarToJulianDay(pjMarch1);
            double jdOfG = Timeline.calendarToJulianDay(gMarch1);
            assertEquals(expectedDiff, jdOfPJ - jdOfG, "WRONG AT " + y);
            assertEquals(pjMarch1, Timeline.julianDayToProlepticJulianCalendar(jdOfPJ));
        }

        assertEquals(limitY - startY, checksCount);
    }

    @Test
    public void shouldHandleGregorian1582OctOffsetAndItsLackInProlepticGregorian() {
        double jdBeforeGregorianEpoch = Timeline.EPOCH_GREGORIAN_JD - 1;
        assertEquals(new GregorianCalendarPoint(1582, 10, 4), Timeline.julianDayToGregorianCalendar(jdBeforeGregorianEpoch));
        assertEquals(new ProlepticGregorianCalendarPoint(1582, 10, 14), Timeline.julianDayToProlepticGregorianCalendar(jdBeforeGregorianEpoch));
        assertEquals(jdBeforeGregorianEpoch, Timeline.calendarToJulianDay(new GregorianCalendarPoint(1582, 10, 4)));
        assertEquals(jdBeforeGregorianEpoch, Timeline.calendarToJulianDay(new ProlepticGregorianCalendarPoint(1582, 10, 14)));
    }

    @Test
    public void shouldMaintainProperDiffBetweenProlepticGregorianAndJulianDatesBackwards() {
        int startY = GregorianCalendarPoint.EPOCH_GREGORIAN_RULES.y;
        int limitY = Timeline.JULIAN_PERIOD_START_Y - 1;
        int expectedDiff = 10;
        int checksCount = 0;
        for (int y = startY; y > limitY; y--, checksCount++) {
            ProlepticJulianCalendarPoint pjJan1 = new ProlepticJulianCalendarPoint(y, 1, 1);
            ProlepticGregorianCalendarPoint pgJan1 = new ProlepticGregorianCalendarPoint(y, 1, 1);

            if (pjJan1.isYearLeap() != pgJan1.isYearLeap()) {
                expectedDiff--;
            }

            double jdOfPJ = Timeline.calendarToJulianDay(pjJan1);
            double jdOfPG = Timeline.calendarToJulianDay(pgJan1);
            assertEquals(expectedDiff, jdOfPJ - jdOfPG, "WRONG AT " + y);
            assertEquals(pgJan1, Timeline.julianDayToProlepticGregorianCalendar(jdOfPG));
        }

        assertEquals(startY - limitY, checksCount);
    }

}
