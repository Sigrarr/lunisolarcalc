package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Time.timeToDays;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.phenomena.*;
import com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunDiurnalPhaseFinderTest {

    // The source precision is 1 minute. Seconds value :30 set as a midpoint.
    final static DiurnalPhasesExample[] EXAMPLES = {
        // https://www.timeanddate.com/sun/poland/wroclaw?month=1&year=2023 CET/UTC +1
        new DiurnalPhasesExample(
            GeoCoords.ofDegreesWithDirections(
                51.107883, LatitudeDirection.N,
                17.038538, LongitudeDirection.E
            ),
            timeToDays(1, 0, 0),
            new TreeMap<DiurnalPhase, NormalCalendarPoint>() {{
                put(DiurnalPhase.RISE, new CalendarPoint(2023, 1, 7, 7, 54, 30));
                put(DiurnalPhase.TRANSIT,  new CalendarPoint(2023, 1, 7, 11, 57, 30));
                put(DiurnalPhase.SET, new CalendarPoint(2023, 1, 7, 16, 2, 30));
            }}
        ),
        // https://www.timeanddate.com/sun/usa/honolulu?month=12&year=1800 UTC -10:31:26
        new DiurnalPhasesExample(
            GeoCoords.ofDegreesWithDirections(
                21.315603, LatitudeDirection.N,
                157.858093, LongitudeDirection.W
            ),
            - timeToDays(10, 31, 26),
            new TreeMap<DiurnalPhase, NormalCalendarPoint>() {{
                put(DiurnalPhase.RISE, new CalendarPoint(1800, 12, 21, 6, 33, 30));
                put(DiurnalPhase.TRANSIT, new CalendarPoint(1800, 12, 21,11, 58, 30));
                put(DiurnalPhase.SET, new CalendarPoint(1800, 12, 21, 17, 23, 30));
            }}
        ),
        // https://www.timeanddate.com/sun/australia/sydney?month=9&year=1800 UTC +10:04:52
        new DiurnalPhasesExample(
            GeoCoords.ofDegreesWithDirections(
                33.865143, LatitudeDirection.S,
                151.209900, LongitudeDirection.E
            ),
            timeToDays(10, 4, 52),
            new TreeMap<DiurnalPhase, NormalCalendarPoint>() {{
                put(DiurnalPhase.RISE, new CalendarPoint(1800, 9, 1, 6, 19, 30));
                put(DiurnalPhase.TRANSIT, new CalendarPoint(1800, 9, 1, 11, 59, 30));
                put(DiurnalPhase.SET, new CalendarPoint(1800, 9, 1, 17, 41, 30));
            }}
        ),
    };

    private SunDiurnalPhaseFinder finder = new SunDiurnalPhaseFinder();

    @Test
    public void shouldFind() {
        for(DiurnalPhasesExample example : EXAMPLES)
            finder.findMany(example.getCenterDateUT(), example.geoCoords)
                .limit(3)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach((actualResult) -> {
                    Entry<DiurnalPhase, NormalCalendarPoint> exampleEntry = example.it.next();
                    DiurnalPhase expectedPhase = exampleEntry.getKey();
                    TimelinePoint expectedTime = UniversalTimelinePoint.ofLocalTimeCalendarPoint(exampleEntry.getValue(), example.timeOffset);
                    assertEquals(expectedPhase, actualResult.getType().diurnalPhase);
                    assertEquals(expectedTime.julianDay, actualResult.getTimelinePoint().julianDay, 30 * Calcs.SECOND_TO_DAY);
                });
    }

    // @Test
    public void shouldProduceManyUnsuspiciousResultsInOrder() {
        CalendarPoint[] startAroundPoints = new CalendarPoint[] {
            new CalendarPoint(-700,  1,  1.0),
            new CalendarPoint(   0,  4,  7.0),
            new CalendarPoint(1600,  7, 19.0),
            new CalendarPoint(2000, 11,  3.0),
        };
        double[][] baseGeoCDeg = {{0, 0}, {45, 90}, {45, -90}, {-45, 120}, {-45, -120}, {70, 15}, {70, -45}, {-70, 120}, {-70, -135}};
        Random random = new Random();

        for (CalendarPoint startAround : startAroundPoints)
            Stream.concat(
                IntStream.range(0, 6)
                    .mapToObj(i -> new double[] {
                        Calcs.Angle.toNormalLatitude(random.nextDouble() * 360, 360),
                        Calcs.Angle.toNormalSignedLongtiude(random.nextDouble() * 360, 360)
                    }),
                Arrays.stream(baseGeoCDeg)
            )
            .map(dd -> GeoCoords.ofConventional(Math.toRadians(dd[0]), Math.toRadians(dd[1])))
            .forEach(geoCoords -> {
                finder
                    .findMany(startAround, geoCoords)
                    .limit(30 * 3)
                    .reduce((prev, next) -> {
                        if (prev.isPresent() && next.isPresent()) {
                            double progress = next.get().getTimelinePoint().julianDay - prev.get().getTimelinePoint().julianDay;
                            assertTrue(
                                Double.compare(progress, 0.0) > 0,
                                String.format("@%s %s %s then %s %s", geoCoords.toString(),
                                    prev.get().getType().diurnalPhase.toString(),
                                    prev.get().getTimelinePoint().formatCalendrically(),
                                    next.get().getType().diurnalPhase.toString(),
                                    next.get().getTimelinePoint().formatCalendrically()
                                )
                            );
                            assertFalse(
                                Double.compare(progress, 1.0) > 0,
                                String.format("@%s %s %s then %s %s", geoCoords.toString(),
                                    prev.get().getType().diurnalPhase.toString(),
                                    prev.get().getTimelinePoint().formatCalendrically(),
                                    next.get().getType().diurnalPhase.toString(),
                                    next.get().getTimelinePoint().formatCalendrically()
                                )
                            );
                        }
                        return next;
                    });
            });
    }

    @Test
    public void shouldHandlePolarNightAndDay() {
        assertForPolarNight();
        assertForPolarDay();
    }

    void assertForPolarNight() {
        /**
         * https://www.timeanddate.com/sun/norway/longyearbyen?month=10&year=2000
         * https://www.timeanddate.com/sun/norway/longyearbyen?month=2&year=2001
         */
        GeoCoords longyearbyen = GeoCoords.ofConventional(Math.toRadians(78.2166658), Math.toRadians(15.5499978));
        CalendarPoint startDate = new CalendarPoint(2000, 10, 26);
        CalendarPoint endDate = new CalendarPoint(2001, 2, 14);
        int lengthDays = 1 + (int) (Timeline.normalCalendarToJulianDay(endDate) - Timeline.normalCalendarToJulianDay(startDate));

        Optional<UniversalOccurrence<BodyDiurnalPhase>> before = finder
            .find(TimelinePoint.ofCalendar(startDate).add(-1).toCalendarPoint(), longyearbyen, DiurnalPhase.TRANSIT);
        assertTrue(before.isPresent());

        finder
            .findMany(startDate, longyearbyen)
            .limit(lengthDays * 3)
            .forEach(optionalOccurrence -> assertFalse(optionalOccurrence.isPresent()));

        Optional<UniversalOccurrence<BodyDiurnalPhase>> after = finder
            .find(TimelinePoint.ofCalendar(endDate).add(1).toCalendarPoint(), longyearbyen, DiurnalPhase.TRANSIT);
        assertTrue(after.isPresent());
    }

    void assertForPolarDay() {
        /**
         * https://www.timeanddate.com/sun/@6620709?month=12&year=2000
         * https://www.timeanddate.com/sun/@6620709?month=1&year=2001
         */
        GeoCoords adelaide = GeoCoords.ofConventional(Math.toRadians(-67.25), Math.toRadians(-68.5));
        CalendarPoint startLocalDate = new CalendarPoint(2000, 12, 2, 13, 23, 0);
        CalendarPoint startUniversalDate = UniversalTimelinePoint.ofLocalTimeCalendarPoint(startLocalDate, -3.0/24).toCalendarPoint();
        CalendarPoint endLocalDate = new CalendarPoint(2001, 1, 10, 13, 41, 0);
        CalendarPoint endUniversalDate = UniversalTimelinePoint.ofLocalTimeCalendarPoint(endLocalDate, -3.0/24).toCalendarPoint();
        int lengthDays = 1 + (int) (Timeline.normalCalendarToJulianDay(endLocalDate) - Timeline.normalCalendarToJulianDay(startLocalDate));

        /**
         * We're omitting the referenced polar day's boundary days: the first, the last,
         * the first before and the first after. A user should be informed about the need
         * of caution around the polar day's or night's boundaries, especially at latitudes
         * close to a polar circle.
         */

        Optional<UniversalOccurrence<BodyDiurnalPhase>> twoBefore = finder
            .find(TimelinePoint.ofCalendar(startUniversalDate).add(-2).toCalendarPoint(), adelaide, DiurnalPhase.TRANSIT);
        assertTrue(twoBefore.isPresent());

        CalendarPoint dayAfterStart = TimelinePoint.ofCalendar(startUniversalDate).add(1).toCalendarPoint();
        finder
            .findMany(dayAfterStart, adelaide)
            .limit((lengthDays - 2) * 3)
            .forEach(optionalOccurrence -> assertFalse(optionalOccurrence.isPresent()));

        Optional<UniversalOccurrence<BodyDiurnalPhase>> twoAfter = finder
            .find(TimelinePoint.ofCalendar(endUniversalDate).add(2).toCalendarPoint(), adelaide, DiurnalPhase.TRANSIT);
        assertTrue(twoAfter.isPresent());
    }
}
