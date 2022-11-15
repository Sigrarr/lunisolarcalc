package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.GregorianCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.MeanMotionApproximate;

import org.junit.jupiter.api.Test;

public class SunSeasonPointFinderTest {
    /**
     * Meeus 1998, Table 27.E, p. 182
     */
    private static final Map<GregorianCalendarPoint, SunSeasonPoint> TRUE_VSOP87_SUN_SEASON_POINTS = new HashMap<GregorianCalendarPoint, SunSeasonPoint>() {{
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

    private SunSeasonPointFinder finder = new SunSeasonPointFinder() {{
        setCoreCalculationsLimit(3);
    }};

    @Test
    public void shouldFindEquinoxOrSolsticeToTwentySeconds() {
        Map<GregorianCalendarPoint, GregorianCalendarPoint> minuteNumberMismatches = new HashMap<>();

        for (Entry<GregorianCalendarPoint, SunSeasonPoint> entry : TRUE_VSOP87_SUN_SEASON_POINTS.entrySet()) {
            GregorianCalendarPoint vsop87Gcp = entry.getKey();
            double vsop87Jde = Timeline.calendarToJulianDay(vsop87Gcp);
            double actualJde = finder.findJulianEphemerisDay(vsop87Gcp.y, entry.getValue());
            GregorianCalendarPoint actualGcp = Timeline.julianDayToGregorianCalendar(actualJde);

            assertTrue(
                Math.abs(actualJde - vsop87Jde) <= Time.timeToDays(0, 0, 20),
                tooMuchDiffMsg(vsop87Gcp, actualGcp)
            );

            if (!vsop87Gcp.formatYMDHMin().equals(actualGcp.formatYMDHMin())) {
                minuteNumberMismatches.put(vsop87Gcp, actualGcp);
            }
        }

        assertTrue(
            minuteNumberMismatches.size() <= TRUE_VSOP87_SUN_SEASON_POINTS.size() / 6,
            tooManyMinuteNumberMismatchesMsg(minuteNumberMismatches)
        );
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
            finder.findMany(partYearScopes[i][0], partYearScopes[i][1], 15)
                .reduce((previous, next) -> {
                    double diff = next.ephemerisTimelinePoint.julianDay - previous.ephemerisTimelinePoint.julianDay;
                    assertTrue(
                        Math.signum(diff) > 0.0,
                        "Wrong order: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    assertTrue(
                        Math.abs(diff - (MeanMotionApproximate.TROPICAL_YEAR.lengthDays / 4)) < 7.0,
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
        List<String> allYMDs = TRUE_VSOP87_SUN_SEASON_POINTS.keySet().stream()
            .sorted()
            .map(gcp -> gcp.formatYMD())
            .collect(Collectors.toList());
        List<String> solsticeYMDs = allYMDs.stream()
            .filter(ymd -> ymd.matches(".*((/06/)|(/12/)).*"))
            .collect(Collectors.toList());

        Iterator<String> allIt1 = allYMDs.listIterator();
        finder.findMany(1996, 2005)
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(allIt1.next(), ymd));

        Iterator<String> solsticeIt1 = solsticeYMDs.listIterator();
        finder.findMany(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt1.next(), ymd));

        Iterator<String> allIt2 = allYMDs.listIterator();
        finder.findMany(1996, 2005, 90)
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(allIt2.next(), ymd));

        Iterator<String> solsticeIt2 = solsticeYMDs.listIterator();
        finder.findMany(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE), 90)
            .map(r -> r.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt2.next(), ymd));

        Iterator<String> allIt3 = allYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005)
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(allIt3.next(), ymd));

        Iterator<String> solsticeIt3 = solsticeYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt3.next(), ymd));

        Iterator<String> allIt4 = allYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, 90)
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(allIt4.next(), ymd));

        Iterator<String> solsticeIt4 = solsticeYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE), 90)
            .mapToObj(jde -> Timeline.julianDayToGregorianCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt4.next(), ymd));
    }

    private String tooMuchDiffMsg(GregorianCalendarPoint vsop87, GregorianCalendarPoint actual) {
        return "True VSOP 87 value: <" + vsop87 + " TD>, actual value: <" + actual + " TD>.";
    }

    private String tooManyMinuteNumberMismatchesMsg(Map<GregorianCalendarPoint, GregorianCalendarPoint> mismatches) {
        return "More than 1/6 minute number mismatches! [VSOP 87 value\tactual value]\n" + mismatches.entrySet().stream()
            .map(e -> e.getKey().formatYMDHMin() + "\t" + e.getValue().formatYMDHMin()).collect(Collectors.joining("\n"));
    }

    private String dateFormatTD(ResultCyclicPhenomenon<SunSeasonPoint> result) {
        return result.ephemerisTimelinePoint.toGregorianCalendarPoint().formatYMD() + " TD";
    }
}
