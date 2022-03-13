package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;

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

    private MeasuredMoonPhaseFinder finder = new MeasuredMoonPhaseFinder(5);

    @Test
    public void shouldFindMoonPhase() {
        for (Entry<RomanCalendarPoint[], Double> entry : LUNATION_START_END_TO_DURATION.entrySet()) {
            RomanCalendarPoint lunationStartDate = entry.getKey()[0];
            RomanCalendarPoint lunationEndDate = entry.getKey()[1];
            double actualLunationStartJde = finder.findJulianEphemerisDayAround(lunationStartDate, MoonPhase.NEW_MOON, 15);
            double actualLunationEndJde = finder.findJulianEphemerisDayAround(lunationEndDate, MoonPhase.NEW_MOON, 15);
            double expectedLunationLength = entry.getValue();

            assertEquals(expectedLunationLength, actualLunationEndJde - actualLunationStartJde, Time.timeToDays(0, 0, 15 + 15));
        }

        // Meeus 1998, Example 49.a, p. 353
        RomanCalendarPoint rcp = new RomanCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, rcp.toYearWithFraction(), Calcs.EPSILON);

        double actualJde = finder.findJulianEphemerisDayAround(rcp, MoonPhase.NEW_MOON, 15);
        double trueELP2K82Jde = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(1977, 2, 18, 3, 37, 40));
        assertEquals(trueELP2K82Jde, actualJde, Time.timeToDays(0, 0, 15));

        // Meeus 1998, Example 49.b, p. 353
        rcp = new RomanCalendarPoint(2044, 1, 1.0);
        actualJde = finder.findJulianEphemerisDayAround(rcp, MoonPhase.THIRD_QUARTER, 15);
        double exampleJde = 2467636.49186;
        assertEquals(exampleJde, actualJde, Time.timeToDays(0, 0, 15));        
    }

    @Test
    public void shouldFindManyOrderedMoonPhasesToQuarterMinuteInMaxFiveIterationsEachAndLessThanThreeIterationsOnAverage() {
        RomanCalendarPoint[] startAroundPoints = new RomanCalendarPoint[] {
            new RomanCalendarPoint(-700,  1,  1.0),
            new RomanCalendarPoint(   0,  4,  7.0),
            new RomanCalendarPoint(1600,  7, 19.0),
            new RomanCalendarPoint(2000, 11,  3.0),
        };
        int partLimit = (int) Math.round(200 * 4 * MeanMotionApproximate.TROPICAL_YEAR.lengthDays / MeanMotionApproximate.SYNODIC_MONTH.lengthDays);
 
        System.out.println("\tCalculations in progress...");
        for (int i = 0; i < startAroundPoints.length; i++)
            finder.findMany(startAroundPoints[i], partLimit, 15)
                .reduce((previous, next) -> {
                    double diff = next.julianEphemerisDay - previous.julianEphemerisDay;
                    assertTrue(
                        Math.signum(diff) > 0.0,
                        "Wrong order: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    assertTrue(
                        Math.abs(diff - (MeanMotionApproximate.SYNODIC_MONTH.lengthDays / 4)) < 1.5,
                        "Wrong interval between subsequent phases: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    return next;
                });
 
        double avgIterations = ((double) finder.calcsTotal) / finder.findingsTotal;
        assertTrue(avgIterations < 3.0, "Expected average calculation iterations < 3, was: " + avgIterations);
        System.out.println("\t\t" + finder.findingsTotal + " moon phases found, no anomalies detected.");
    }

    @Test
    public void shouldFindManyResultsWithVariousParameterLists() {
        double delta = Time.timeToDays(0, 0, 15);
        for (RomanCalendarPoint[] subsequentNewMoonsPairApproximates : LUNATION_START_END_TO_DURATION.keySet()) {
            double aJde = finder.findJulianEphemerisDayAround(subsequentNewMoonsPairApproximates[0], MoonPhase.NEW_MOON, 15);
            double bJde = finder.findJulianEphemerisDayAround(subsequentNewMoonsPairApproximates[1], MoonPhase.NEW_MOON, 15);
            RomanCalendarPoint startRoman = Timeline.julianDayToRomanCalendar(aJde - 60.0);

            assertEquals(2, finder.findMany(startRoman, 13)
                .filter(r -> matchesOne(r.julianEphemerisDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(startRoman, 4, EnumSet.of(MoonPhase.NEW_MOON))
                .filter(r -> matchesOne(r.julianEphemerisDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(startRoman, 13, 15)
                .filter(r -> matchesOne(r.julianEphemerisDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(startRoman, 4, EnumSet.of(MoonPhase.NEW_MOON), 15)
                .filter(r -> matchesOne(r.julianEphemerisDay, aJde, bJde, delta)).count());

            assertEquals(2, finder.findManyJulianEphemerisDays(startRoman, 13)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(startRoman, 4, EnumSet.of(MoonPhase.NEW_MOON))
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(startRoman, 13, 15)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(startRoman, 4, EnumSet.of(MoonPhase.NEW_MOON), 15)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
        }
    }

    private String dateFormatTD(FoundCyclicPhenomenon<MoonPhase> result) {
        return dateFormatTD(result.julianEphemerisDay);
    }

    private String dateFormatTD(double jde) {
        return Timeline.julianDayToRomanCalendar(jde).formatYMD() + " TD";
    }

    private boolean matchesOne(double base, double optionA, double optionB, double delta) {
        return Calcs.compare(base, optionA, delta) == 0 || Calcs.compare(base, optionB, delta) == 0;
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
