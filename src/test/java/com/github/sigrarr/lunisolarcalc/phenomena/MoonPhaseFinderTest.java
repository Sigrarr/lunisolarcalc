package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Time.*;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.*;

public class MoonPhaseFinderTest {
    /**
     * Meeus 1998, Table 49.A, p. 354
     */
    private static final Map<CalendarPoint[], Double> LUNATION_START_END_TO_DURATION = new HashMap<CalendarPoint[], Double>() {{
        put(new CalendarPoint[] { new CalendarPoint(1903,  6, 25), new CalendarPoint(1903, 7, 24) }, 29 + timeToDays( 6, 35, 0));
        put(new CalendarPoint[] { new CalendarPoint(2035,  6,  6), new CalendarPoint(2035, 7,  5) }, 29 + timeToDays( 6, 39, 0));
        put(new CalendarPoint[] { new CalendarPoint(2053,  6, 16), new CalendarPoint(2053, 7, 15) }, 29 + timeToDays( 6, 35, 0));
        put(new CalendarPoint[] { new CalendarPoint(2071,  6, 27), new CalendarPoint(2071, 7, 27) }, 29 + timeToDays( 6, 36, 0));
        put(new CalendarPoint[] { new CalendarPoint(1955, 12, 14), new CalendarPoint(1956, 1, 13) }, 29 + timeToDays(19, 54, 0));
        put(new CalendarPoint[] { new CalendarPoint(1973, 12, 24), new CalendarPoint(1974, 1, 23) }, 29 + timeToDays(19, 55, 0));
    }};

    /**
     * https://www.timeanddate.com/moon/phases/spain/madrid?year=1600 UTC -0:14:44
     */
    private static final CalendarPoint[] NEW_MOONS_MADRID_1600 = new CalendarPoint[] {
        new CalendarPoint(1600, 1, 16, 4, 51, 0),
        new CalendarPoint(1600, 2, 14, 17, 16, 0),
        new CalendarPoint(1600, 3, 15, 3, 27, 0),
        new CalendarPoint(1600, 4, 13, 11, 53, 0),
        new CalendarPoint(1600, 5, 12, 19, 27, 0),
        new CalendarPoint(1600, 6, 11, 3, 15, 0),
        new CalendarPoint(1600, 7, 10, 12, 16, 0),
        new CalendarPoint(1600, 8, 8, 23, 17, 0),
        new CalendarPoint(1600, 9, 7, 12, 42, 0),
        new CalendarPoint(1600, 10, 7, 4, 38, 0),
        new CalendarPoint(1600, 11, 5, 22, 38, 0),
        new CalendarPoint(1600, 12, 5, 17, 42, 0)
    };

    private MoonPhaseFinder finder;

    @BeforeEach
    public void reinitializeFinder() {
        finder = new MoonPhaseFinder();
        finder.setCoreCalculationsLimit(5);
    }

    @Test
    public void shouldFindMoonPhaseInContemporaryScope() {
        for (Entry<CalendarPoint[], Double> entry : LUNATION_START_END_TO_DURATION.entrySet()) {
            TimelinePoint lunationStartDate = UniversalTimelinePoint.ofCalendar(entry.getKey()[0]);
            TimelinePoint lunationEndDate = UniversalTimelinePoint.ofCalendar(entry.getKey()[1]);
            double actualLunationStartJde = finder.findJulianEphemerisDayAround(lunationStartDate, MoonPhase.NEW_MOON);
            double actualLunationEndJde = finder.findJulianEphemerisDayAround(lunationEndDate, MoonPhase.NEW_MOON);
            double expectedLunationLength = entry.getValue();

            assertEquals(expectedLunationLength, actualLunationEndJde - actualLunationStartJde, timeToDays(0, 0, 30));
        }

        // Meeus 1998, Example 49.a, p. 353
        CalendarPoint gcp = new CalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, 1977 + (gcp.getDayOfYear() - 1 + gcp.getTime()) / gcp.getNumberOfDaysInYear(), Calcs.EPSILON);
        TimelinePoint tx = UniversalTimelinePoint.ofCalendar(gcp);

        double actualJde = finder.findJulianEphemerisDayAround(tx, MoonPhase.NEW_MOON);
        double trueELP2K82Jde = Timeline.normalCalendarToJulianDay(new CalendarPoint(1977, 2, 18, 3, 37, 40));
        assertEquals(trueELP2K82Jde, actualJde, timeToDays(0, 0, 10));

        // Meeus 1998, Example 49.b, p. 353
        final TimelinePoint tx2 = UniversalTimelinePoint.ofCalendaricParameters(2044, 1, 1);
        double exampleJde = 2467636.49186;
        finder.findManyJulianEphemerisDays(tx2, MoonPhase.THIRD_QUARTER)
            .limit(2)
            .forEach((jde) -> {
                assertTrue(
                    Math.abs(jde - exampleJde) <= timeToDays(0, 0, 10) || (
                        Math.abs(jde - tx2.julianDay) < Math.abs(exampleJde - tx2.julianDay)
                        && Calcs.equal(jde, exampleJde, MeanCycle.LUNATION.epochalLengthDays + timeToDays(7, 0, 0))
                    ),
                    "Suspicious 3rd Q.: " + new DynamicalTimelinePoint(jde).toCalendarPoint().formatDateTimeToMinutes()
                );
            });
    }

    @Test
    public void shouldFindMoonPhaseInMoreDistantPast() {
        PrimitiveIterator.OfDouble actualNewMoonJds = finder
            .findManyJulianEphemerisDays(UniversalTimelinePoint.ofCalendaricParameters(1600, 1, 15), MoonPhase.NEW_MOON)
            .map((jde) -> TimeScaleDelta.convertJulianEphemerisDayToUniversalTime(jde))
            .iterator();

        double timeZoneDiff = timeToDays(0, 14, 44);
        for (int i = 0; i < 12; i++) {
            double expectedJd = Timeline.normalCalendarToJulianDay(NEW_MOONS_MADRID_1600[i]) + timeZoneDiff;
            assertEquals(expectedJd, actualNewMoonJds.next(), timeToDays(0, 1, 0));
        }
    }

    @Test
    public void shouldProduceManyUnsuspiciousResultsInOrderWithAcceptablePerformance() {
        CalendarPoint[] startAroundPoints = new CalendarPoint[] {
            new CalendarPoint(-700,  1,  1.0),
            new CalendarPoint(   0,  4,  7.0),
            new CalendarPoint(1600,  7, 19.0),
            new CalendarPoint(2000, 11,  3.0),
        };
        int partLimit = (int) Math.round(200 * 4 * MeanCycle.TROPICAL_YEAR.epochalLengthDays / MeanCycle.LUNATION.epochalLengthDays);

        System.out.println("\tCalculations in progress...");
        for (int i = 0; i < startAroundPoints.length; i++)
            finder.findMany(UniversalTimelinePoint.ofCalendar(startAroundPoints[i]))
                .limit(partLimit)
                .reduce((previous, next) -> {
                    double diff = next.timelinePoint.julianDay - previous.timelinePoint.julianDay;
                    assertTrue(
                        Math.signum(diff) > 0.0,
                        "Wrong order: " + previous + " -> " + next
                    );
                    assertTrue(
                        Math.abs(diff - (MeanCycle.LUNATION.epochalLengthDays / 4)) < 1.5,
                        "Big interval between subsequent phases: " + previous + " -> " + next
                    );
                    return next;
                });

        double avgIterations = ((double) finder.getTotalCoreCalculationsCount()) / finder.getTotalFindingsCount();
        assertTrue(avgIterations < 3.5, "Expected average calculation iterations < 3.5, was: " + avgIterations);
        System.out.println("\t\t" + finder.getTotalFindingsCount() + " moon phases found, no anomalies detected.");
    }

    @Test
    public void shouldFindManyResultsWithVariousParameterLists() {
        double delta = timeToDays(0, 0, 2 * (int) finder.getPrecisionTimeSeconds());
        for (CalendarPoint[] subsequentNewMoonsPairApproximates : LUNATION_START_END_TO_DURATION.keySet()) {
            TimelinePoint ta = UniversalTimelinePoint.ofCalendar(subsequentNewMoonsPairApproximates[0]);
            TimelinePoint tb = UniversalTimelinePoint.ofCalendar(subsequentNewMoonsPairApproximates[1]);
            double aJde = finder.findJulianEphemerisDayAround(ta, MoonPhase.NEW_MOON);
            double bJde = finder.findJulianEphemerisDayAround(tb, MoonPhase.NEW_MOON);
            TimelinePoint start = new DynamicalTimelinePoint(aJde - 60.0);

            assertEquals(2, finder.findMany(start)
                .limit(13)
                .filter(r -> matchesOne(r.timelinePoint.julianDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(start, MoonPhase.NEW_MOON)
                .limit(4)
                .filter(r -> matchesOne(r.timelinePoint.julianDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findMany(start, EnumSet.of(MoonPhase.NEW_MOON))
                .limit(4)
                .filter(r -> matchesOne(r.timelinePoint.julianDay, aJde, bJde, delta)).count());
            assertEquals(2, finder.findManyJulianEphemerisDays(start, MoonPhase.NEW_MOON)
                .limit(4)
                .filter(jde -> matchesOne(jde, aJde, bJde, delta)).count());
        }
    }

    @Test
    public void shouldOutputOfDefaultApproximatorBeCloseToResult() {
        double totalDiff = 0.0;
        double maxDiff = 0.0;

        double startJde = Timeline.normalCalendarToJulianDay(new CalendarPoint(-700, 1, 1));
        double endJde = Timeline.normalCalendarToJulianDay(new CalendarPoint(2022, 1, 1));
        double step = MeanCycle.LUNATION.epochalLengthDays * 0.85;
        MoonPhase[] phases = MoonPhase.values();
        System.out.println("\tCalculations in progress...");
        for (double jde = startJde; jde <= endJde; jde += step) {
            TimelinePoint argument = new DynamicalTimelinePoint(jde);
            MoonPhase phase = phases[(int) jde % 4];
            double approximateJde = finder.approximator.approximateJulianEphemerisDayAround(argument, phase);
            double resultJde = finder.findJulianEphemerisDay(approximateJde, phase);
            double diff = Math.abs(resultJde - approximateJde);
            totalDiff += diff;
            maxDiff = Math.max(diff, maxDiff);
        }

        assertTrue(maxDiff < MeanCycle.LUNATION.epochalLengthDays / 4);
        double avgDiff = totalDiff / finder.getTotalFindingsCount();
        assertTrue(avgDiff < MeanCycle.LUNATION.epochalLengthDays / 8);
        System.out.print("\t\t" + finder.getTotalFindingsCount() + " approximate-result pairs checked (moon phases)."
            + " Avg diff: " + avgDiff + "; max diff: " + maxDiff + " [days].");
        System.out.println(" " + (maxDiff <= 1.0 && avgDiff < 1.0 ? "Very good." : "OK."));
    }

    private boolean matchesOne(double base, double optionA, double optionB, double delta) {
        return Calcs.compare(base, optionA, delta) == 0 || Calcs.compare(base, optionB, delta) == 0;
    }
}
