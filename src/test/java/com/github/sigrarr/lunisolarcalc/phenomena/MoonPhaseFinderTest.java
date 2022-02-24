package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.Test;

public class MoonPhaseFinderTest {
    /**
     * Meeus 1998, Table 49.A, p. 354
     */
    private static final Map<RomanCalendarPoint[], Double> LUNATION_START_END_TO_DURATION = new HashMap<RomanCalendarPoint[], Double>() {{
        put(new RomanCalendarPoint[] { new RomanCalendarPoint(1903,  6, 25), new RomanCalendarPoint(1903, 7, 24) }, 29 + Time.timeToDays( 6, 35, 0));
        put(new RomanCalendarPoint[] { new RomanCalendarPoint(2035,  6,  6), new RomanCalendarPoint(2035, 7,  5) }, 29 + Time.timeToDays( 6, 39, 0));
        put(new RomanCalendarPoint[] { new RomanCalendarPoint(2053,  6, 16), new RomanCalendarPoint(2053, 7, 15) }, 29 + Time.timeToDays( 6, 35, 0));
        put(new RomanCalendarPoint[] { new RomanCalendarPoint(2071,  6, 27), new RomanCalendarPoint(2071, 7, 27) }, 29 + Time.timeToDays( 6, 36, 0));
        put(new RomanCalendarPoint[] { new RomanCalendarPoint(1955, 12, 14), new RomanCalendarPoint(1956, 1, 13) }, 29 + Time.timeToDays(19, 54, 0));
        put(new RomanCalendarPoint[] { new RomanCalendarPoint(1973, 12, 24), new RomanCalendarPoint(1974, 1, 23) }, 29 + Time.timeToDays(19, 55, 0));
    }};
    private static final double FIFTEEN_SECONDS_EQUIVALENT_RADIANS = Math.toRadians(MeanValueApproximations.MoonPhase.degreesPerTimeMiliseconds(15000));

    private MoonPhaseFinder finder = new MoonPhaseFinder();

    @Test
    public void shouldFindMoonPhaseToQuarterMinuteInThreeIterations() {
        for (Entry<RomanCalendarPoint[], Double> entry : LUNATION_START_END_TO_DURATION.entrySet()) {
            RomanCalendarPoint lunationStartDate = entry.getKey()[0];
            RomanCalendarPoint lunationEndDate = entry.getKey()[1];
            double actualLunationStartJDE = finder.findJulianEphemerisDay(lunationStartDate, MoonPhase.NEW_MOON, FIFTEEN_SECONDS_EQUIVALENT_RADIANS);
            assertIterations();
            double actualLunationEndJDE = finder.findJulianEphemerisDay(lunationEndDate, MoonPhase.NEW_MOON, FIFTEEN_SECONDS_EQUIVALENT_RADIANS);
            assertIterations();

            assertEquals(entry.getValue(), actualLunationEndJDE - actualLunationStartJDE, Time.timeToDays(0, 0, 15 + 15));
        }

        // Meeus 1998, Example 49.a, p. 353
        RomanCalendarPoint rcp = new RomanCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, rcp.toYearWithFraction(), Calcs.EPSILON);

        double actualJDE = finder.findJulianEphemerisDay(rcp, MoonPhase.NEW_MOON, FIFTEEN_SECONDS_EQUIVALENT_RADIANS);
        double trueELP2K82JDE = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(1977, 2, 18, 3, 37, 40));
        assertEquals(trueELP2K82JDE, actualJDE, Time.timeToDays(0, 0, 15));
        assertIterations();

        // Meeus 1998, Example 49.b, p. 353
        rcp = new RomanCalendarPoint(2044, 1, 1.0);
        actualJDE = finder.findJulianEphemerisDay(rcp, MoonPhase.THIRD_QUARTER, FIFTEEN_SECONDS_EQUIVALENT_RADIANS);
        double exampleJDE = 2467636.49186;
        assertEquals(exampleJDE, actualJDE, Time.timeToDays(0, 0, 15));        
        assertIterations();
    }

    private void assertIterations() {
        int iters = finder.getLastExcessCalculationsCount();
        assertTrue("Expected 3 or less excess calculations, " + iters + " performed." , iters <= 3);
    }
}
