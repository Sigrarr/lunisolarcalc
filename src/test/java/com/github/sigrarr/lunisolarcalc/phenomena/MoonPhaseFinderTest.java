package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.GregorianCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;

public class MoonPhaseFinderTest {
    /**
     * Meeus 1998, Table 49.A, p. 354
     */
    private static final Map<GregorianCalendarPoint[], Double> LUNATION_START_END_TO_DURATION = new HashMap<GregorianCalendarPoint[], Double>() {{
        put(new GregorianCalendarPoint[] { new GregorianCalendarPoint(1903,  6, 25), new GregorianCalendarPoint(1903, 7, 24) }, 29 + Time.timeToDays( 6, 35, 0));
        put(new GregorianCalendarPoint[] { new GregorianCalendarPoint(2035,  6,  6), new GregorianCalendarPoint(2035, 7,  5) }, 29 + Time.timeToDays( 6, 39, 0));
        put(new GregorianCalendarPoint[] { new GregorianCalendarPoint(2053,  6, 16), new GregorianCalendarPoint(2053, 7, 15) }, 29 + Time.timeToDays( 6, 35, 0));
        put(new GregorianCalendarPoint[] { new GregorianCalendarPoint(2071,  6, 27), new GregorianCalendarPoint(2071, 7, 27) }, 29 + Time.timeToDays( 6, 36, 0));
        put(new GregorianCalendarPoint[] { new GregorianCalendarPoint(1955, 12, 14), new GregorianCalendarPoint(1956, 1, 13) }, 29 + Time.timeToDays(19, 54, 0));
        put(new GregorianCalendarPoint[] { new GregorianCalendarPoint(1973, 12, 24), new GregorianCalendarPoint(1974, 1, 23) }, 29 + Time.timeToDays(19, 55, 0));
    }};

    private MeasuredMoonPhaseFinder finder = new MeasuredMoonPhaseFinder(5);

    @Test
    public void shouldFindMoonPhase() {
        for (Entry<GregorianCalendarPoint[], Double> entry : LUNATION_START_END_TO_DURATION.entrySet()) {
            TimelinePoint lunationStartDate = TimelinePoint.ofCalendarPoint(entry.getKey()[0]);
            TimelinePoint lunationEndDate = TimelinePoint.ofCalendarPoint(entry.getKey()[1]);
            double actualLunationStartJde = finder.findJulianEphemerisDayAround(lunationStartDate, MoonPhase.NEW_MOON, 15);
            double actualLunationEndJde = finder.findJulianEphemerisDayAround(lunationEndDate, MoonPhase.NEW_MOON, 15);
            double expectedLunationLength = entry.getValue();

            assertEquals(expectedLunationLength, actualLunationEndJde - actualLunationStartJde, Time.timeToDays(0, 0, 15 + 15));
        }

        // Meeus 1998, Example 49.a, p. 353
        GregorianCalendarPoint gcp = new GregorianCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, gcp.toYearWithFraction(), Calcs.EPSILON);
        TimelinePoint tx = TimelinePoint.ofCalendarPoint(gcp);

        double actualJde = finder.findJulianEphemerisDayAround(tx, MoonPhase.NEW_MOON, 15);
        double trueELP2K82Jde = Timeline.julianformCalendarToJulianDay(new GregorianCalendarPoint(1977, 2, 18, 3, 37, 40));
        assertEquals(trueELP2K82Jde, actualJde, Time.timeToDays(0, 0, 15));

        // Meeus 1998, Example 49.b, p. 353
        tx = TimelinePoint.ofCalendarPoint(new GregorianCalendarPoint(2044, 1, 1.0));
        actualJde = finder.findJulianEphemerisDayAround(tx, MoonPhase.THIRD_QUARTER, 15);
        double exampleJde = 2467636.49186;
        assertEquals(exampleJde, actualJde, Time.timeToDays(0, 0, 15));
    }

    @Test
    public void shouldProduceManyUnsuspiciousResultsInOrderWithAcceptablePerformance() {
        GregorianCalendarPoint[] startAroundPoints = new GregorianCalendarPoint[] {
            new GregorianCalendarPoint(-700,  1,  1.0),
            new GregorianCalendarPoint(   0,  4,  7.0),
            new GregorianCalendarPoint(1600,  7, 19.0),
            new GregorianCalendarPoint(2000, 11,  3.0),
        };
        int partLimit = (int) Math.round(200 * 4 * MeanMotionApproximate.TROPICAL_YEAR.lengthDays / MeanMotionApproximate.SYNODIC_MONTH.lengthDays);

        System.out.println("\tCalculations in progress...");
        for (int i = 0; i < startAroundPoints.length; i++)
            finder.findMany(TimelinePoint.ofCalendarPoint(startAroundPoints[i]), partLimit, 15)
                .reduce((previous, next) -> {
                    double diff = next.ephemerisTimelinePoint.julianDay - previous.ephemerisTimelinePoint.julianDay;
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
        for (GregorianCalendarPoint[] subsequentNewMoonsPairApproximates : LUNATION_START_END_TO_DURATION.keySet()) {
            TimelinePoint ta = TimelinePoint.ofCalendarPoint(subsequentNewMoonsPairApproximates[0]);
            TimelinePoint tb = TimelinePoint.ofCalendarPoint(subsequentNewMoonsPairApproximates[1]);
            double aJde = finder.findJulianEphemerisDayAround(ta, MoonPhase.NEW_MOON, 15);
            double bJde = finder.findJulianEphemerisDayAround(tb, MoonPhase.NEW_MOON, 15);
            TimelinePoint start = new TimelinePoint(aJde - 60.0);

            assertEquals(2, finder.findMany(start, 13)
                .filter(r -> matchesOne(r.ephemerisTimelinePoint.julianDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(start, 4, EnumSet.of(MoonPhase.NEW_MOON))
                .filter(r -> matchesOne(r.ephemerisTimelinePoint.julianDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(start, 13, 15)
                .filter(r -> matchesOne(r.ephemerisTimelinePoint.julianDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(start, 4, EnumSet.of(MoonPhase.NEW_MOON), 15)
                .filter(r -> matchesOne(r.ephemerisTimelinePoint.julianDay, aJde, bJde, delta)).count());

            assertEquals(2, finder.findManyJulianEphemerisDays(start, 13)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(start, 4, EnumSet.of(MoonPhase.NEW_MOON))
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(start, 13, 15)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(start, 4, EnumSet.of(MoonPhase.NEW_MOON), 15)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
        }
    }

    private String dateFormatTD(ResultCyclicPhenomenon<MoonPhase> result) {
        return result.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD() + " TD";
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
