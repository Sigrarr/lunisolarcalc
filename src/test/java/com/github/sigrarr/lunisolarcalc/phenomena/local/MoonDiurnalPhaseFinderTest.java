package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Time.timeToDays;

import java.util.*;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class MoonDiurnalPhaseFinderTest {

    // The source precision is 1 minute. Seconds value :30 set as a midpoint.
    final static DiurnalPhasesExample[] EXAMPLES = {
        // https://www.timeanddate.com/moon/iceland/reykjavik?month=6&year=1900 UTC -1:28
        new DiurnalPhasesExample(
            GeoCoords.ofDegreesWithDirections(
                64.128288, LatitudeDirection.N,
                21.827774, LongitudeDirection.W
            ),
            - timeToDays(1, 28, 0),
            new TreeMap<DiurnalPhase, NormalCalendarPoint>() {{
                put(DiurnalPhase.RISE, new CalendarPoint(1900, 6, 12, 21, 42, 30));
                put(DiurnalPhase.TRANSIT, new CalendarPoint(1900, 6, 12, 23, 54, 30));
                put(DiurnalPhase.SET, new CalendarPoint(1900, 6, 13, 2, 7, 30));
            }}
        ),
        // https://www.timeanddate.com/moon/china/shanghai?month=5&year=1800 UTC +8:05:43
        new DiurnalPhasesExample(
            GeoCoords.ofDegreesWithDirections(
                31.267401, LatitudeDirection.N,
                121.522179, LongitudeDirection.E
            ),
            timeToDays(8, 5, 43),
            new TreeMap<DiurnalPhase, NormalCalendarPoint>() {{
                put(DiurnalPhase.RISE, new CalendarPoint(1800, 5, 26, 6, 26, 30));
                put(DiurnalPhase.TRANSIT, new CalendarPoint(1800, 5, 26, 13, 56, 30));
                put(DiurnalPhase.SET, new CalendarPoint(1800, 5, 26, 21, 27, 30));
            }}
        ),
        // https://www.timeanddate.com/moon/australia/sydney?month=4&year=1800 UTC +10:04:52
        new DiurnalPhasesExample(
            GeoCoords.ofDegreesWithDirections(
                33.865143, LatitudeDirection.S,
                151.209900, LongitudeDirection.E
            ),
            timeToDays(10, 4, 52),
            new TreeMap<DiurnalPhase, NormalCalendarPoint>() {{
                put(DiurnalPhase.RISE, new CalendarPoint(1800, 4, 1, 12, 25, 30));
                put(DiurnalPhase.TRANSIT, new CalendarPoint(1800, 4, 1, 17, 11, 30));
                put(DiurnalPhase.SET, new CalendarPoint(1800, 4, 1, 21, 56, 30));
            }}
        ),
    };

    private MoonDiurnalPhaseFinder finder = new MoonDiurnalPhaseFinder();

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
                    assertEquals(expectedTime.julianDay, actualResult.getTimelinePoint().julianDay, 40 * Calcs.SECOND_TO_DAY);
                });
    }
}
