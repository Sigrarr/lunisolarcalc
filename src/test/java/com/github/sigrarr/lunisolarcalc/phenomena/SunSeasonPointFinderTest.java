package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations;

import org.junit.jupiter.api.Test;

public class SunSeasonPointFinderTest {
    /**
     * Meeus 1998, Table 27.E, p. 182
     */
    private static final Map<RomanCalendarPoint, SunSeasonPoint> TRUE_VSOP87_SUN_SEASON_POINTS = new HashMap<RomanCalendarPoint, SunSeasonPoint>() {{
        put(new RomanCalendarPoint(1996,  3, 20,   8,  4,  7), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(1997,  3, 20,  13, 55, 42), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(1998,  3, 20,  19, 55, 35), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(1999,  3, 21,   1, 46, 53), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(2000,  3, 20,   7, 36, 10), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(2001,  3, 20,  13, 31, 47), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(2002,  3, 20,  19, 17, 13), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(2003,  3, 21,   1,  0, 50), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(2004,  3, 20,   6, 49, 42), SunSeasonPoint.MARCH_EQUINOX);
        put(new RomanCalendarPoint(2005,  3, 20,  12, 34, 29), SunSeasonPoint.MARCH_EQUINOX);

        put(new RomanCalendarPoint(1996,  6, 21,   2, 24, 46), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(1997,  6, 21,   8, 20, 59), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(1998,  6, 21,  14, 03, 38), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(1999,  6, 21,  19, 50, 11), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(2000,  6, 21,   1, 48, 46), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(2001,  6, 21,   7, 38, 48), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(2002,  6, 21,  13, 25, 29), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(2003,  6, 21,  19, 11, 32), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(2004,  6, 21,   0, 57, 57), SunSeasonPoint.JUNE_SOLSTICE);
        put(new RomanCalendarPoint(2005,  6, 21,   6, 47, 12), SunSeasonPoint.JUNE_SOLSTICE);

        put(new RomanCalendarPoint(1996,  9, 22,  18,  1,  8), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(1997,  9, 22,  23, 56, 49), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(1998,  9, 23,   5, 38, 15), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(1999,  9, 23,  11, 32, 34), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(2000,  9, 22,  17, 28, 40), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(2001,  9, 22,  23, 05, 32), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(2002,  9, 23,   4, 56, 28), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(2003,  9, 23,  10, 47, 53), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(2004,  9, 22,  16, 30, 54), SunSeasonPoint.SEPTEMBER_EQUINOX);
        put(new RomanCalendarPoint(2005,  9, 22,  22, 24, 14), SunSeasonPoint.SEPTEMBER_EQUINOX);

        put(new RomanCalendarPoint(1996, 12, 21,  14,  6, 56), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(1997, 12, 21,  20,  8,  5), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(1998, 12, 22,   1, 57, 31), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(1999, 12, 22,   7, 44, 52), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(2000, 12, 21,  13, 38, 30), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(2001, 12, 21,  19, 22, 34), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(2002, 12, 22,   1, 15, 26), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(2003, 12, 22,   7,  4, 53), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(2004, 12, 21,  12, 42, 40), SunSeasonPoint.DECEMBER_SOLSTICE);
        put(new RomanCalendarPoint(2005, 12, 21,  18, 36,  1), SunSeasonPoint.DECEMBER_SOLSTICE);
    }};

    private SunSeasonPointFinder finder = new SunSeasonPointFinder() {{
        setCoreCalculationsLimit(3);
    }};

    @Test
    public void shouldFindEquinoxOrSolsticeToHalfMinute() {
        Map<RomanCalendarPoint, RomanCalendarPoint> inaccuracies = new HashMap<>();

        for (Entry<RomanCalendarPoint, SunSeasonPoint> entry : TRUE_VSOP87_SUN_SEASON_POINTS.entrySet()) {
            RomanCalendarPoint vsop87Rcp = entry.getKey();
            double vsop87JDE = Timeline.romanCalendarToJulianDay(vsop87Rcp);
            double actualJDE = finder.findJulianEphemerisDay(vsop87Rcp.y, entry.getValue());
            RomanCalendarPoint actualRcp = Timeline.julianDayToRomanCalendar(actualJDE);

            double diffJDE = Math.abs(actualJDE - vsop87JDE);
            assertTrue(
                diffJDE <= Time.timeToDays(0, 0, 30),
                tooMuchDiffMsg(vsop87Rcp, actualRcp)
            );

            if (!vsop87Rcp.formatYMDHI().equals(actualRcp.formatYMDHI())) {
                inaccuracies.put(vsop87Rcp, actualRcp);
            }
        }

        assertTrue(
            inaccuracies.size() <= TRUE_VSOP87_SUN_SEASON_POINTS.size() / 4,
            tooManyInaccuraciesMsg(inaccuracies)
        );
    }

    @Test
    public void shouldFindManyOrderedPointsWithQuarterMinuteMeanPrecisionInMaxThreeIterationsEach() {
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
                    double diff = next.julianEphemerisDay - previous.julianEphemerisDay;
                    assertTrue(
                        Math.signum(diff) > 0.0,
                        "Wrong order: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    assertTrue(
                        Math.abs(diff - (MeanValueApproximations.TROPICAL_YEAR_MEAN_DAYS/ 4)) < 7.0,
                        "Wrong interval between subsequent points: " + dateFormatTD(previous) + " -> " + dateFormatTD(next)
                    );
                    return next;
                });
            total += (partYearScopes[i][1] - partYearScopes[i][0] + 1) * 4;
        }

        System.out.println("\t\t" + total + " sun season points found, no anomalies detected.");
    }

    @Test
    public void shouldStreamManyResults() {
        List<String> allYMDs = TRUE_VSOP87_SUN_SEASON_POINTS.keySet().stream()
            .sorted()
            .map(rcp -> rcp.formatYMD())
            .collect(Collectors.toList());
        List<String> solsticeYMDs = allYMDs.stream()
            .filter(ymd -> ymd.matches(".*((/06/)|(/12/)).*"))
            .collect(Collectors.toList());

        Iterator<String> allIt1 = allYMDs.listIterator();
        finder.findMany(1996, 2005)
            .map(r -> Timeline.julianDayToRomanCalendar(r.julianEphemerisDay).formatYMD())
            .forEach(ymd -> assertEquals(allIt1.next(), ymd));

        Iterator<String> solsticeIt1 = solsticeYMDs.listIterator();
        finder.findMany(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .map(r -> Timeline.julianDayToRomanCalendar(r.julianEphemerisDay).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt1.next(), ymd));

        Iterator<String> allIt2 = allYMDs.listIterator();
        finder.findMany(1996, 2005, 90)
            .map(r -> Timeline.julianDayToRomanCalendar(r.julianEphemerisDay).formatYMD())
            .forEach(ymd -> assertEquals(allIt2.next(), ymd));

        Iterator<String> solsticeIt2 = solsticeYMDs.listIterator();
        finder.findMany(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE), 90)
            .map(r -> Timeline.julianDayToRomanCalendar(r.julianEphemerisDay).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt2.next(), ymd));

        Iterator<String> allIt3 = allYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005)
            .mapToObj(jde -> Timeline.julianDayToRomanCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(allIt3.next(), ymd));

        Iterator<String> solsticeIt3 = solsticeYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE))
            .mapToObj(jde -> Timeline.julianDayToRomanCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt3.next(), ymd));

        Iterator<String> allIt4 = allYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, 90)
            .mapToObj(jde -> Timeline.julianDayToRomanCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(allIt4.next(), ymd));

        Iterator<String> solsticeIt4 = solsticeYMDs.listIterator();
        finder.findManyJulianEphemerisDays(1996, 2005, EnumSet.of(SunSeasonPoint.JUNE_SOLSTICE, SunSeasonPoint.DECEMBER_SOLSTICE), 90)
            .mapToObj(jde -> Timeline.julianDayToRomanCalendar(jde).formatYMD())
            .forEach(ymd -> assertEquals(solsticeIt4.next(), ymd));
    }

    private String tooMuchDiffMsg(RomanCalendarPoint vsop87, RomanCalendarPoint actual) {
        return "True VSOP 87 value: <" + vsop87.formatYMDHI() + " TD>, actual value: <" + actual.formatYMDHI() + " TD>.";
    }

    private String tooManyInaccuraciesMsg(Map<RomanCalendarPoint, RomanCalendarPoint> inaccuracies) {
        return "More than 25% inaccuracies! [VSOP 87 value\tactual value]\n" + inaccuracies.entrySet().stream()
            .map(e -> e.getKey().formatYMDHI() + "\t" + e.getValue().formatYMDHI()).collect(Collectors.joining("\n"));
    }

    private String dateFormatTD(FoundPhenomenon<SunSeasonPoint> result) {
        return dateFormatTD(result.julianEphemerisDay);
    }

    private String dateFormatTD(double jde) {
        return Timeline.julianDayToRomanCalendar(jde).formatYMD() + " TD";
    }
}
