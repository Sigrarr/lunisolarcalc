package com.github.sigrarr.lunisolarcalc.time;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import java.util.*;
import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelineTest
{
    /**
     * Meeus 1998, Example 7.a-b, 47.a, p. 61, 342; Ch. 7, pp. 62, 64
     */ 
    private static final Map<RomanCalendarPoint, Double> ROMAN_CALENDAR_TO_JD = new HashMap<RomanCalendarPoint, Double>() {{
        put(new RomanCalendarPoint( 1957, 10,  4.81), 2436116.31);
        put(new RomanCalendarPoint(  333,  1, 27.5 ), 1842713.0 );
        put(new RomanCalendarPoint( 2000,  1,  1.5 ), 2451545.0 );
        put(new RomanCalendarPoint( 1999,  1,  1.0 ), 2451179.5 );
        put(new RomanCalendarPoint( 1987,  1, 27.0 ), 2446822.5 );
        put(new RomanCalendarPoint( 1987,  6, 19.5 ), 2446966.0 );
        put(new RomanCalendarPoint( 1988,  1, 27.0 ), 2447187.5 );
        put(new RomanCalendarPoint( 1988,  6, 19.5 ), 2447332.0 );
        put(new RomanCalendarPoint( 1900,  1,  1.0 ), 2415020.5 );
        put(new RomanCalendarPoint( 1600,  1,  1.0 ), 2305447.5 );
        put(new RomanCalendarPoint( 1600, 12, 31.0 ), 2305812.5 );
        put(new RomanCalendarPoint(  837,  4, 10.3 ), 2026871.8 );
        put(new RomanCalendarPoint( -123, 12, 31.0 ), 1676496.5 );
        put(new RomanCalendarPoint( -122,  1,  1.0 ), 1676497.5 );
        put(new RomanCalendarPoint(-1000,  7, 12.5 ), 1356001.0 );
        put(new RomanCalendarPoint(-1000,  2, 29.0 ), 1355866.5 );
        put(new RomanCalendarPoint(-1001,  8, 17.9 ), 1355671.4 );
        put(new RomanCalendarPoint(-4712,  1,  1.5 ),       0.0 );
        put(new RomanCalendarPoint( -584,  5, 28.63), 1507900.13);
        put(new RomanCalendarPoint( 1992,  4, 12.0 ), 2448724.5 );
    }};

    @Test
    public void shouldConvertRomanCalendarToJD() {
        ROMAN_CALENDAR_TO_JD.forEach((rc, jd) -> assertEquals(jd.doubleValue(), Timeline.romanCalendarToJulianDay(rc), Calcs.EPSILON));
    }

    @Test
    public void shouldConvertJDtoRomanCalendar() {
        ROMAN_CALENDAR_TO_JD.forEach((rc, jd) -> assertEquals(rc, Timeline.julianDayToRomanCalendar(jd)));
    }

    @Test
    public void shouldConvertJDtoTau() {
        // Meeus 1998, Example 32.a, p. 219
        assertEquals(-0.007032169747, Timeline.julianDayToMillenialTau(2448976.5), autoDelta(-0.007032169747));
    }

    @Test
    public void shouldCovertJDtoT() {
        // Meeus 1998, Example 22.a, p. 148
        assertEquals(-0.127296372348, Timeline.julianDayToCenturialT(2446895.5), autoDelta(-0.127296372348));
        // Meeus 1998, Example 47.a, p. 342
        assertEquals(-0.077221081451, Timeline.julianDayToCenturialT(2448724.5), autoDelta(-0.077221081451));
    }

    @Test
    public void shouldConvertBetweenTauAndT() {
        Random random = new Random();
        double limitJD = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(2200, 12, 31));
        for (int i = 0; i < 10; i++) {
            double jd = random.nextDouble() * limitJD;
            double cT = Timeline.julianDayToCenturialT(jd);
            double tau = Timeline.julianDayToMillenialTau(jd);
            assertEquals(cT, Timeline.millenialTauToCenturialT(tau), Calcs.EPSILON_12);
            assertEquals(tau, Timeline.centurialTToMillenialTau(cT), Calcs.EPSILON_12);            
        }
    }

    @Test
    public void shouldConvertBetweenUTAndTD() {
        Random random = new Random();
        double limitJD = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(2200, 12, 31));
        for (int i = 0; i < 10; i++) {
            double jd = random.nextDouble() * limitJD;
            RomanCalendarPoint rcp = Timeline.julianDayToRomanCalendar(jd);
            double deltaTDays = Time.timeToDays(0, 0, Time.getDeltaTSeconds(rcp.y));
            assertEquals(deltaTDays, Timeline.julianDayToEphemeris(jd, rcp.y) - jd, Calcs.EPSILON);
            assertEquals(deltaTDays, Timeline.romanCalendarToJulianEphemerisDay(rcp) - jd, Calcs.EPSILON);

            double jde = random.nextDouble() * limitJD;
            rcp = Timeline.julianEphemerisDayToRomanCalendar(jde);
            RomanCalendarPoint rcpTD = Timeline.julianDayToRomanCalendar(jde);
            deltaTDays = Time.timeToDays(0, 0, Time.getDeltaTSeconds(rcp.y));
            assertEquals(deltaTDays, Timeline.romanCalendarToJulianDay(rcpTD) - Timeline.romanCalendarToJulianDay(rcp), Calcs.EPSILON);
        }
    }
}
