package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.Example.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class MoonDiurnalPhaseFinderTest {
    /**
     * The reference data-set's precision is 1 minute.
     * :30 s is choosen here as a minute's midpoint.
     */
    private static final Example[] EXAMPLES_EASY = {
        // https://www.timeanddate.com/moon/@2617832?month=4&year=2000
        new Example("Lejre (moderate, contemporary, around New Moon)",
            LEJRE, +2.0, new CalendarPoint[] {
                new CalendarPoint(2000,  4,  4,  7,  4, 30),
                new CalendarPoint(2000,  4,  4, 13,  8, 30),
                new CalendarPoint(2000,  4,  4, 19, 28, 30),
            }),
        // https://www.timeanddate.com/moon/australia/sydney?month=7&year=1800
        new Example("Sydney (long ago, around First Quarter)",
            SYDNEY, +( 10.0 + 4.0/60.0 + 52.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800,  7, 29, 10, 58, 30),
                new CalendarPoint(1800,  7, 29, 17, 47, 30),
                new CalendarPoint(1800,  7, 30,  0, 47, 30),
            }),
        // https://www.timeanddate.com/moon/usa/honolulu?month=10&year=1800
        new Example("Honolulu (long ago, around Third Quarter)",
            HONOLULU, -( 10.0 + 31.0/60.0 + 26.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800, 10,  9, 22, 42, 30),
                new CalendarPoint(1800, 10, 10,  5, 45, 30),
                new CalendarPoint(1800, 10, 10, 12, 46, 30),
            }),
    };
    private static Example[] EXAMPLES_DEMANDING = {
        // https://www.timeanddate.com/moon/@7670547?month=1&year=1800
        new Example("Greenwich (long ago, around Full Moon)",
            GREENWICH_PARK, -( 0.0 + 1.0/60.0 + 15.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800,  1,  9, 14,  6, 30),
                new CalendarPoint(1800,  1,  9, 23, 10, 30),
                new CalendarPoint(1800,  1, 10,  8, 13, 30),
                null, null, null,
                new CalendarPoint(1800,  1, 10, 15,  4, 30),
                new CalendarPoint(1800,  1, 11,  0,  5, 30),
                new CalendarPoint(1800,  1, 11,  8, 56, 30),
            }),
        // https://www.timeanddate.com/moon/canada/toronto?month=2&year=1800
        new Example("Vancouver (long ago, around Full Moon)",
            VANCOUVER, -( 8.0 + 12.0/60.0 + 28.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800,  2,  7, 14, 35, 30),
                new CalendarPoint(1800,  2,  7, 23,  8, 30),
                new CalendarPoint(1800,  2,  8,  7, 27, 30),
                null, null, null,
                new CalendarPoint(1800,  2,  8, 15, 52, 30),
                new CalendarPoint(1800,  2,  9,  0,  1, 30),
                new CalendarPoint(1800,  2,  9,  7, 53, 30),
            }),
        // https://www.timeanddate.com/moon/japan/tokyo?month=3&year=1800
        new Example("Tokyo (long ago, around Full Moon)",
            TOKYO, +( 9.0 + 18.0/60.0 + 59.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800,  3, 10, 16, 55, 30),
                new CalendarPoint(1800,  3, 10, 23, 44, 30),
                new CalendarPoint(1800,  3, 11,  6, 23, 30),
                null, null, null,
                new CalendarPoint(1800,  3, 11, 18,  4, 30),
                new CalendarPoint(1800,  3, 12,  0, 33, 30),
                new CalendarPoint(1800,  3, 12,  6, 51, 30),
            }),
        // https://www.timeanddate.com/moon/antarctica/belgrano-ii-base?month=4&year=2000
        new Example("Belgrano II Base (far-South, a polar Moon-night's beginning, between NM and 1Q)",
            BELGRANO2, -3.0, new CalendarPoint[] {
                new CalendarPoint(2000,  4,  6, 10, 58, 30),
                new CalendarPoint(2000,  4,  6, 13,  2, 30),
                new CalendarPoint(2000,  4,  6, 14, 35, 30),
                null,
                new CalendarPoint(2000,  4,  7, 13, 56, 30),
                null,
            }),
        // https://www.timeanddate.com/moon/norway/longyearbyen?month=10&year=2000
        new Example("Longyearbyen, Svalbard (far-North, a polar Moon-day's ending, between 3Q and NM)",
            LONGYEARBYEN, +2.0, new CalendarPoint[] {
                null,
                new CalendarPoint(2000, 10, 23,  9, 38, 30),
                new CalendarPoint(2000, 10, 23, 19, 35, 30),
            }),
    };

    MoonDiurnalPhaseFinder finder = new MoonDiurnalPhaseFinder();
    private int seriesCounter;

    @Test
    public void shouldFindInEasyScenarioInAgreementWithReferenceData() {
        double delta = 30.0 * Calcs.SECOND_TO_DAY;
        for (Example example : EXAMPLES_EASY) {
            seriesCounter = 0;
            Iterator<CalendarPoint> expectedLocalDateTimeIt = Arrays.stream(example.expectedLocalDateTimes).iterator();
            finder
                .findMany(example.baseLocalNoon, example.geoCoords)
                .limit(example.expectedLocalDateTimes.length)
                .map(Optional::get)
                .forEach(occ -> {
                    CalendarPoint expectedLocalDateTime = expectedLocalDateTimeIt.next();
                    assertEquals(getExpectedPhase(), occ.getType().diurnalPhase);
                    UniversalTimelinePoint expectedPoint = example.toUniversalPoint(expectedLocalDateTime);
                    UniversalTimelinePoint actualPoint = occ.getTimelinePoint();
                    assertEquals(expectedPoint.julianDay, actualPoint.julianDay, delta,
                        String.format("@%s : %s : %s vs %s",
                            example.getTitle(),
                            occ.getType().diurnalPhase.getTitle(),
                            expectedLocalDateTime.formatDateTimeToMinutes(),
                            example.toLocalDateTime(actualPoint).formatDateTimeToSeconds()
                        )
                    );
                    seriesCounter++;
                });
            assertFalse(expectedLocalDateTimeIt.hasNext());
        }
    }

    @Test
    public void shouldFindInDemandingScenarioInAgreementWithReferenceData() {
        double delta = 30.0 * Calcs.SECOND_TO_DAY;
        for (Example example : EXAMPLES_DEMANDING) {
            seriesCounter = 0;
            Iterator<CalendarPoint> expectedLocalDateTimeIt = Arrays.stream(example.expectedLocalDateTimes).iterator();
            finder
                .findMany(example.baseLocalNoon, example.geoCoords)
                .limit(example.expectedLocalDateTimes.length)
                .forEach(optOcc -> {
                    CalendarPoint expectedLocalDateTime = expectedLocalDateTimeIt.next();
                    if (expectedLocalDateTime == null) {
                        assertFalse(optOcc.isPresent());
                    } else {
                        assertTrue(optOcc.isPresent());
                        assertEquals(getExpectedPhase(), optOcc.get().getType().diurnalPhase);
                        UniversalTimelinePoint expectedPoint = example.toUniversalPoint(expectedLocalDateTime);
                        UniversalTimelinePoint actualPoint = optOcc.get().getTimelinePoint();
                        assertEquals(expectedPoint.julianDay, actualPoint.julianDay, delta,
                            String.format("@%s : %s : %s vs %s",
                                example.getTitle(),
                                optOcc.get().getType().diurnalPhase.getTitle(),
                                expectedLocalDateTime.formatDateTimeToMinutes(),
                                example.toLocalDateTime(actualPoint).formatDateTimeToSeconds()
                            )
                        );
                    }
                    seriesCounter++;
                });
            assertFalse(expectedLocalDateTimeIt.hasNext());
        }
    }

    private DiurnalPhase getExpectedPhase() {
        return DiurnalPhase.values()[seriesCounter % 3];
    }
}
