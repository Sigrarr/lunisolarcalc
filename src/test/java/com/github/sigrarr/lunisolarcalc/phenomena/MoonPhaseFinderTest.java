package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.Assert.*;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.*;

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

    private MeasuredMoonPhaseFinder finder;

    @Before
    public void init() {
        finder = new MeasuredMoonPhaseFinder(5);
    }

    @Test
    public void shouldFindMoonPhase() {
        for (Entry<RomanCalendarPoint[], Double> entry : LUNATION_START_END_TO_DURATION.entrySet()) {
            RomanCalendarPoint lunationStartDate = entry.getKey()[0];
            RomanCalendarPoint lunationEndDate = entry.getKey()[1];
            double actualLunationStartJDE = finder.findJulianEphemerisDayAround(lunationStartDate, MoonPhase.NEW_MOON, 15);
            double actualLunationEndJDE = finder.findJulianEphemerisDayAround(lunationEndDate, MoonPhase.NEW_MOON, 15);
            double expectedLunationLength = entry.getValue();

            assertEquals(expectedLunationLength, actualLunationEndJDE - actualLunationStartJDE, Time.timeToDays(0, 0, 15 + 15));
        }

        // Meeus 1998, Example 49.a, p. 353
        RomanCalendarPoint rcp = new RomanCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, rcp.toYearWithFraction(), Calcs.EPSILON);

        double actualJDE = finder.findJulianEphemerisDayAround(rcp, MoonPhase.NEW_MOON, 15);
        double trueELP2K82JDE = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(1977, 2, 18, 3, 37, 40));
        assertEquals(trueELP2K82JDE, actualJDE, Time.timeToDays(0, 0, 15));

        // Meeus 1998, Example 49.b, p. 353
        rcp = new RomanCalendarPoint(2044, 1, 1.0);
        actualJDE = finder.findJulianEphemerisDayAround(rcp, MoonPhase.THIRD_QUARTER, 15);
        double exampleJDE = 2467636.49186;
        assertEquals(exampleJDE, actualJDE, Time.timeToDays(0, 0, 15));        
    }

    @Test
    public void shouldFindManyOrderedMoonPhasesToQuarterMinuteInMaxFiveIterationsEachAndLessThanThreeIterationsOnAverage() {
        RomanCalendarPoint[] startAroundPoints = new RomanCalendarPoint[] {
            new RomanCalendarPoint(-700,  1,  1.0),
            new RomanCalendarPoint(   0,  4,  7.0),
            new RomanCalendarPoint(1600,  7, 19.0)
        };
        int partLimitBase = (int) Math.round(200 * 4 * MeanValueApproximations.TROPICAL_YEAR_MEAN_DAYS / MeanValueApproximations.LUNATION_MEAN_DAYS);
 
        System.out.println("\tCalculations in progress...");
        for (int i = 0; i < startAroundPoints.length; i++)
            finder.findMany(startAroundPoints[i], partLimitBase * (i + 1), 15)
                .reduce((previous, next) -> {
                    double diff = next.julianEphemerisDay - previous.julianEphemerisDay;
                    assertTrue(
                        "Wrong order: " + dateFormatTD(previous) + " -> " + dateFormatTD(next),
                        Math.signum(diff) > 0.0
                    );
                    assertTrue(
                        "Wrong interval between subsequent phases: " + dateFormatTD(previous) + " -> " + dateFormatTD(next),
                        Math.abs(diff - (MeanValueApproximations.LUNATION_MEAN_DAYS / 4)) < 1.5
                    );
                    return next;
                });
 
        double avgIterations = ((double) finder.calcsTotal) / finder.findingsTotal;
        assertTrue("Expected average calculation iterations < 3, was: " + avgIterations, avgIterations < 3.0);
        System.out.println("\t\t" + finder.findingsTotal + " moon phases found, no anomalies detected.");
    }

    private String dateFormatTD(FoundPhenomenon<MoonPhase> result) {
        return dateFormatTD(result.julianEphemerisDay);
    }

    private String dateFormatTD(double jde) {
        return Timeline.julianDayToRomanCalendar(jde).formatYMD() + " TD";
    }

    private static class MeasuredMoonPhaseFinder extends MoonPhaseFinder {
        public int findingsTotal = 0;
        public int calcsTotal = 0;
        public int calcsInFindingMax = 0;
        private int calcsInFinding = 0;

        MeasuredMoonPhaseFinder(int iterationsLimit) {
            super();
            setCoreCalculationsLimit(iterationsLimit);
        }
       
        @Override
        protected double findJulianEphemerisDay(double approximateJde, MoonPhase phase, double meanPrecisionRadians) {
            double value = super.findJulianEphemerisDay(approximateJde, phase, meanPrecisionRadians);
            findingsTotal++;
            calcsTotal += calcsInFinding;
            if (calcsInFinding > calcsInFindingMax)
                calcsInFindingMax = calcsInFinding;
            calcsInFinding = 0;
            return value;
        }

        @Override
        protected double calculateMoonOverSunLambdaExcess() {
            calcsInFinding++;
            return super.calculateMoonOverSunLambdaExcess();
        }
    }
}
