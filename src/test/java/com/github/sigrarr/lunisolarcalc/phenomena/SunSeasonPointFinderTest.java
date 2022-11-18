package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.PrimitiveIterator.OfDouble;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.GregorianCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class SunSeasonPointFinderTest {
    /**
     * Meeus 1998, Table 27.E, p. 182
     */
    private static final Map<GregorianCalendarPoint, SunSeasonPoint> VSOP87_SUN_SEASON_POINTS = new HashMap<GregorianCalendarPoint, SunSeasonPoint>() {{
        put(new GregorianCalendarPoint(1996,  3, 20,   8,  4,  7), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(1997,  3, 20,  13, 55, 42), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(1998,  3, 20,  19, 55, 35), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(1999,  3, 21,   1, 46, 53), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(2000,  3, 20,   7, 36, 10), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(2001,  3, 20,  13, 31, 47), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(2002,  3, 20,  19, 17, 13), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(2003,  3, 21,   1,  0, 50), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(2004,  3, 20,   6, 49, 42), SunSeasonPoint.MARCH_EQUINOX);
        put(new GregorianCalendarPoint(2005,  3, 20,  12, 34, 29), SunSeasonPoint.MARCH_EQUINOX);

        put(new GregorianCalendarPoint(1996,  6, 21,   2, 24, 46), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(1997,  6, 21,   8, 20, 59), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(1998,  6, 21,  14, 03, 38), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(1999,  6, 21,  19, 50, 11), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(2000,  6, 21,   1, 48, 46), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(2001,  6, 21,   7, 38, 48), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(2002,  6, 21,  13, 25, 29), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(2003,  6, 21,  19, 11, 32), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(2004,  6, 21,   0, 57, 57), SunSeasonPoint.JUNE_SOLSTICE);
        put(new GregorianCalendarPoint(2005,  6, 21,   6, 47, 12), SunSeasonPoint.JUNE_SOLSTICE);

        put(new GregorianCalendarPoint(1996,  9, 22,  18,  1,  8), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(1997,  9, 22,  23, 56, 49), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(1998,  9, 23,   5, 38, 15), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(1999,  9, 23,  11, 32, 34), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(2000,  9, 22,  17, 28, 40), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(2001,  9, 22,  23, 05, 32), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(2002,  9, 23,   4, 56, 28), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(2003,  9, 23,  10, 47, 53), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(2004,  9, 22,  16, 30, 54), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new GregorianCalendarPoint(2005,  9, 22,  22, 24, 14), SunSeasonPoint.SEPTEMBER_EQUINOX);

        put(new GregorianCalendarPoint(1996, 12, 21,  14,  6, 56), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(1997, 12, 21,  20,  8,  5), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(1998, 12, 22,   1, 57, 31), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(1999, 12, 22,   7, 44, 52), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(2000, 12, 21,  13, 38, 30), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(2001, 12, 21,  19, 22, 34), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(2002, 12, 22,   1, 15, 26), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(2003, 12, 22,   7,  4, 53), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(2004, 12, 21,  12, 42, 40), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new GregorianCalendarPoint(2005, 12, 21,  18, 36,  1), SunSeasonPoint.DECEMBER_SOLSTICE);
    }};

    /**
     * Meeus 1998, Table 27.F, p. 182
     */
    private static final Map<Integer, double[]> YEAR_TO_SEASON_DURATIONS_DAYS = new TreeMap<Integer, double[]>() {{
        put(-4000, new double[]{93.55, 89.18, 89.07, 93.44});
        put(-3500, new double[]{93.83, 89.53, 88.82, 93.07});
        put(-3000, new double[]{94.04, 89.92, 88.61, 92.67});
        put(-2500, new double[]{94.20, 90.33, 88.47, 92.25});
        put(-2000, new double[]{94.28, 90.76, 88.39, 91.81});
        put(-1500, new double[]{94.30, 91.20, 88.38, 91.37});
        put(-1000, new double[]{94.25, 91.63, 88.42, 90.94});
        put(-500, new double[]{94.14, 92.05, 88.53, 90.52});
        put(0, new double[]{93.96, 92.45, 88.69, 90.13});
        put(500, new double[]{93.73, 92.82, 88.91, 89.78});
        put(1000, new double[]{93.44, 93.15, 89.18, 89.47});
        put(1500, new double[]{93.12, 93.42, 89.50, 89.20});
        put(2000, new double[]{92.76, 93.65, 89.84, 88.99});
        put(2500, new double[]{92.37, 93.81, 90.22, 88.84});
        put(3000, new double[]{91.97, 93.92, 90.61, 88.74});
        // out of Julian Period:
        // put(3500, new double[]{91.57, 93.96, 91.01, 88.71});
        // put(4000, new double[]{91.17, 93.93, 91.40, 88.73});
        // put(4500, new double[]{90.79, 93.84, 91.79, 88.82});
        // put(5000, new double[]{90.44, 93.70, 92.15, 88.96});
        // put(5500, new double[]{90.11, 93.50, 92.49, 89.15});
        // put(6000, new double[]{89.82, 93.25, 92.79, 89.38});
        // put(6500, new double[]{89.58, 92.96, 93.04, 89.66});
    }};

    /**
     * https://aa.usno.navy.mil/calculated/seasons?year=1700&tz=0.00&tz_sign=1&tz_label=false&dst=false
     */
    private static final GregorianCalendarPoint[] SEASON_POINTS_1700 = new GregorianCalendarPoint[] {
        new GregorianCalendarPoint(1700, 3, 20, 14, 26, 0),
        new GregorianCalendarPoint(1700, 6, 21, 13, 52, 0),
        new GregorianCalendarPoint(1700, 9, 23, 2, 28, 0),
        new GregorianCalendarPoint(1700, 12, 21, 17, 37, 0)
    };

    private SunSeasonPointFinder finder = new SunSeasonPointFinder() {{
        setCoreCalculationsLimit(4);
    }};

    @Test
    public void shouldFindSeasonPointsInModernScope() {
        Map<GregorianCalendarPoint, GregorianCalendarPoint> minuteNumberMismatches = new HashMap<>();

        for (Entry<GregorianCalendarPoint, SunSeasonPoint> entry : VSOP87_SUN_SEASON_POINTS.entrySet()) {
            GregorianCalendarPoint vsop87Gcp = entry.getKey();
            double vsop87Jde = Timeline.calendarToJulianDay(vsop87Gcp);
            double actualJde = finder.findJulianEphemerisDay(vsop87Gcp.y, entry.getValue());
            GregorianCalendarPoint actualGcp = Timeline.julianDayToGregorianCalendar(actualJde);

            assertEquals(vsop87Jde, actualJde, Time.timeToDays(0, 0, 16), tooMuchDiffMsg(vsop87Gcp, actualGcp));

            if (!vsop87Gcp.formatYMDHMin().equals(actualGcp.formatYMDHMin())) {
                minuteNumberMismatches.put(vsop87Gcp, actualGcp);
            }
        }

        assertTrue(
            minuteNumberMismatches.size() <= VSOP87_SUN_SEASON_POINTS.size() / 6,
            tooManyMinuteNumberMismatchesMsg(minuteNumberMismatches)
        );
    }

    @Test
    public void shouldFindSeasonPointsInMoreDistantPast() {
        OfDouble actualJds = finder
            .findManyJulianEphemerisDays(1700, 1700)
            .map((jde) -> Time.shiftDaysToTimeType(jde, TimeType.UNIVERSAL, 1700))
            .iterator();

        for (int i = 0; i < 4; i++) {
            double expectedJd = Timeline.calendarToJulianDay(SEASON_POINTS_1700[i]);
            assertEquals(expectedJd, actualJds.next(), Time.timeToDays(0, 1, 16));
        }
    }

    @Test
    public void shouldResultsBeConsistentWithSeasonDurationsTable() {
        double delta = Time.timeToDays(0, 30, 0);
        proveThatGivenDeltaIsOkButDecimalAutoDeltaWouldBeTooTightForSeasonDurationsFromSourceTable(delta);

        YEAR_TO_SEASON_DURATIONS_DAYS.entrySet().forEach((entry) -> {
            int year = entry.getKey().intValue();
            double[] expectedDurations = entry.getValue();
            double[] points = finder.findManyJulianEphemerisDays(year).limit(5).toArray();

            for (int i = 0; i < 4; i++) {
                double actualDuration = points[i + 1] - points[i];
                assertEquals(expectedDurations[i], actualDuration, delta);
            }
        });
    }

    @Test
    public void shouldProduceManyUnsuspiciousResultsInOrderWithAcceptablePerformance() {
        int[][] partYearScopes = new int[][] {
            {-700, -400},
            {-100, 200},
            {1500, 1800},
            {2000, 2300},
        };

        int total = 0;
        System.out.println("\tCalculations in progress...");
        for (int i = 0; i < partYearScopes.length; i++) {
            finder.findMany(partYearScopes[i][0], partYearScopes[i][1])
                .reduce((previous, next) -> {
                    double diff = next.ephemerisTimelinePoint.julianDay - previous.ephemerisTimelinePoint.julianDay;
                    assertTrue(
                        Math.signum(diff) > 0.0,
                        "Wrong order: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    assertEquals(
                        diff,
                        MeanMotionApproximate.TROPICAL_YEAR.lengthDays / 4,
                        4.0,
                        "Wrong interval between subsequent points: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    return next;
                });
            total += (partYearScopes[i][1] - partYearScopes[i][0] + 1) * 4;
        }

        System.out.println("\t\t" + total + " sun season points found, no anomalies detected.");
    }

    @Test
    public void shouldFindManyResultsWithVariousParameterLists() {
        List<String> allYMDs = VSOP87_SUN_SEASON_POINTS.keySet().stream()
            .sorted()
            .map(gcp -> gcp.formatYMD())
            .collect(Collectors.toList());
        List<String> solsticeYMDs = allYMDs.stream()
            .filter(ymd -> ymd.matches(".*((/06/)|(/12/)).*"))
            .collect(Collectors.toList());

        Iterator<String> allIt1 = allYMDs.listIterator();
        finder.findMany(1996)
            .limit(allYMDs.size())
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(allIt1.next(), ymd));

        Iterator<String> solsticeIt1 = solsticeYMDs.listIterator();
        finder.findMany(1996, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .limit(solsticeYMDs.size())
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt1.next(), ymd));

        Iterator<String> allIt2 = allYMDs.listIterator();
        finder.findMany(1996, 2005)
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(allIt2.next(), ymd));

        Iterator<String> solsticeIt2 = solsticeYMDs.listIterator();
        finder.findMany(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt2.next(), ymd));

        Iterator<String> allIt3 = allYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996)
            .limit(allYMDs.size())
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(allIt3.next(), ymd));

        Iterator<String> solsticeIt3 = solsticeYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .limit(solsticeYMDs.size())
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt3.next(), ymd));

        Iterator<String> allIt4 = allYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005)
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(allIt4.next(), ymd));

        Iterator<String> solsticeIt4 = solsticeYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt4.next(), ymd));
    }

    private void proveThatGivenDeltaIsOkButDecimalAutoDeltaWouldBeTooTightForSeasonDurationsFromSourceTable(double delta) {
        double[] accurateWinter2000to2001BoundariesJDEs = VSOP87_SUN_SEASON_POINTS.entrySet().stream()
            .filter(
                (e) -> (e.getKey().y == 2000 && e.getValue() == SunSeasonPoint.DECEMBER_SOLSTICE)
                    || (e.getKey().y == 2001 && e.getValue() == SunSeasonPoint.MARCH_EQUINOX)
            )
            .mapToDouble((e) -> TimelinePoint.ofCalendarPoint(e.getKey(), TimeType.DYNAMICAL).julianDay)
            .sorted()
            .toArray();
        double accurateWinter2000to2001Duration = accurateWinter2000to2001BoundariesJDEs[1] - accurateWinter2000to2001BoundariesJDEs[0];
        double tablewiseWinter2000to2001Duration = YEAR_TO_SEASON_DURATIONS_DAYS.get(2000)[3];

        assertEquals(accurateWinter2000to2001Duration, tablewiseWinter2000to2001Duration, delta);
        assertThrows(
            AssertionFailedError.class,
            () -> assertEquals(tablewiseWinter2000to2001Duration, accurateWinter2000to2001Duration, Calcs.decimalAutoDelta(tablewiseWinter2000to2001Duration))
        );
    }

    private String tooMuchDiffMsg(GregorianCalendarPoint vsop87, GregorianCalendarPoint actual) {
        String msg = "Too much diff";
        if (vsop87.y == actual.y && vsop87.m == actual.m) {
            msg += " (" + (0.01 * Math.round(Math.abs(vsop87.dt - actual.dt) * Calcs.DAY_SECONDS * 100.0)) + " s)";
        }
        msg += "; true VSOP 87 value: <" + vsop87 + " TD>, actual value: <" + actual + " TD>.";
        return msg;
    }

    private String tooManyMinuteNumberMismatchesMsg(Map<GregorianCalendarPoint, GregorianCalendarPoint> mismatches) {
        return "More than 1/6 minute number mismatches! [VSOP 87 value\tactual value]\n" + mismatches.entrySet().stream()
            .map(e -> e.getKey().formatYMDHMin() + "\t" + e.getValue().formatYMDHMin()).collect(Collectors.joining("\n"));
    }

    private String dateFormatTD(ResultCyclicPhenomenon<SunSeasonPoint> result) {
        return result.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD() + " TD";
    }
}
