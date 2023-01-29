package com.github.sigrarr.lunisolarcalc.time.timescaledelta;

import static com.github.sigrarr.lunisolarcalc.time.timescaledelta.Util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class BasisMinus700ToPlus2000ResolverTest {
    /**
     * Morrison & Stephenson 2004, Table 1, p. 332
     */
    private static final Map<Integer, Integer> REFERENCE_CALENDAR_YEAR_TO_DELTA_T = new HashMap<Integer, Integer>() {{
        put(-700, 21000);
        put(-600, 19040);
        put(-500, 17190);
        put(-400, 15530);
        put(-300, 14080);
        put(-200, 12790);
        put(-100, 11640);
        put(0, 10580);
        put(100, 9600);
        put(200, 8640);
        put(300, 7680);
        put(400, 6700);
        put(500, 5710);
        put(600, 4740);
        put(700, 3810);
        put(800, 2960);
        put(900, 2200);
        put(1000, 1570);
        put(1100, 1090);
        put(1200, 740);
        put(1300, 490);
        put(1400, 320);
        put(1500, 200);
        put(1600, 120);
        put(1700,   9);
        put(1710,  10);
        put(1720,  11);
        put(1730,  11);
        put(1740,  12);
        put(1750,  13);
        put(1760,  15);
        put(1770,  16);
        put(1780,  17);
        put(1790,  17);
        put(1800,  14);
        put(1810,  13);
        put(1820,  12);
        put(1830,   8);
        put(1840,   6);
        put(1850,   7);
        put(1860,   8);
        put(1870,   2);
        put(1880,  -5);
        put(1890,  -6);
        put(1900,  -3);
        put(1910,  10);
        put(1920,  21);
        put(1930,  24);
        put(1940,  24);
        put(1950,  29);
        put(1960,  33);
        put(1970,  40);
        put(1980,  51);
        put(1990,  57);
        put(2000,  65);
    }};
    private final static double START_MINUS_700_JD = Timeline.calendarToJulianDay(new CalendarPoint(-700, 1, 1.0));
    private final static double START_2000_JD = Timeline.EPOCH_2000_JD - 0.5;

    private BasisMinus700ToPlus2000Resolver resolver = new BasisMinus700ToPlus2000Resolver();

    @Test
    public void shouldFindValueFromTable() {
        REFERENCE_CALENDAR_YEAR_TO_DELTA_T.forEach((y, delta) -> assertEquals(
            delta.intValue(),
            resolver.resolveDeltaTSeconds(yearStartToJulianDay(y))
        ));
    }

    @Test
    public void shouldInterpolateLinearlyBetweenTablePoints() {
        assertEquals((21000 + 19040) / 2, resolver.resolveDeltaTSeconds(yearStartToJulianDay(-650)), 0.1);
        assertEquals(2960 + ((2200 - 2960) / 5), resolver.resolveDeltaTSeconds(yearStartToJulianDay(820)), 0.1);
    }

    @Test
    public void shouldExtrapolateParabolicallyOutsideTable() {
        // Morrison & Stephenson 2004: Table 1 (p. 332), entries with asterisks - rounded to 100s, -20s omitted (cf. table description vs pp. 332, 335)
        assertEquals(25400 - 20, resolver.resolveDeltaTSeconds(yearStartToJulianDay(-1000)), 50);
        assertEquals(23700 - 20, resolver.resolveDeltaTSeconds(yearStartToJulianDay( -900)), 50);
        assertEquals(22000 - 20, resolver.resolveDeltaTSeconds(yearStartToJulianDay( -800)), 50);
    }

    @Test
    public void shouldDeltaAndConversionsBeConsistentAndReversible() {
        Random random = new Random();
        double[] jds = new double[100];
        for (int i = 0; i < 25; i++)
            jds[i] = random.nextDouble() * START_MINUS_700_JD;
        for (int i = 25; i < 75; i++)
            jds[i] = START_MINUS_700_JD + random.nextDouble() * (START_2000_JD - START_MINUS_700_JD);
        for (int i = 75; i < 100; i++)
            jds[i] = START_2000_JD + random.nextDouble() * (Timeline.JULIAN_PERIOD_END_JD - START_2000_JD);

        for (int i = 0; i < 100; i++) {
            double jd = jds[i];

            double deltaT = resolver.resolveDeltaTSeconds(jd);
            double jde = resolver.convertJulianDayToDynamicalTime(jd);
            assertEquals(jd + deltaT*Calcs.SECOND_TO_DAY, jde, Calcs.EPSILON);

            double backDeltaT = resolver.resolveDeltaTSeconds(jde, TimeScale.DYNAMICAL);
            assertEquals(deltaT, backDeltaT, Calcs.EPSILON);

            double backJd = resolver.convertJulianEphemerisDayToUniversalTime(jde);
            assertEquals(jd, backJd, Calcs.EPSILON);
        }
    }
}
