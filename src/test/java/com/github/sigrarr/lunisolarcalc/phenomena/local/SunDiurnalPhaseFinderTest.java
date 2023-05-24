package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.Example.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunDiurnalPhaseFinderTest {
    /**
     * The reference data-set's precision is 1 minute.
     * :30 s is choosen here as a minute's midpoint.
     */
    private static final Example[] EXAMPLES_TYPICAL = {
        // https://www.timeanddate.com/sun/poland/wroclaw?month=1&year=2023
        new Example("Wrocław (moderate, contemporary)",
            WROCLAW, +1.0, new CalendarPoint[] {
                new CalendarPoint(2023,  1, 07, 07, 54, 30),
                new CalendarPoint(2023,  1, 07, 11, 57, 30),
                new CalendarPoint(2023,  1, 07, 16, 02, 30),
            }),
        // https://www.timeanddate.com/sun/@2617832?month=6&year=1800
        new Example("Lejre (moderate, long ago)",
            LEJRE, +( 0.0 + 50.0/60.0 + 20.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800,  6,  1,  3, 26, 30),
                new CalendarPoint(1800,  6,  1, 11, 59, 30),
                new CalendarPoint(1800,  6,  1, 20, 33, 30),
            }),
        // https://www.timeanddate.com/sun/usa/honolulu?month=12&year=1800
        new Example("Honolulu (long ago)",
            HONOLULU, -( 10.0 + 31.0/60.0 + 26.0/3600.0 ), new CalendarPoint[] {
                new CalendarPoint(1800, 12, 21, 06, 33, 30),
                new CalendarPoint(1800, 12, 21, 11, 58, 30),
                new CalendarPoint(1800, 12, 21, 17, 23, 30),
            }),
    };
    private static final Example[] EXAMPLES_POLAR = {
        // https://www.timeanddate.com/sun/norway/longyearbyen?month=10&year=2000
        new Example("Longyearbyen, Svalbard (far-North, a polar night's beginning)",
            LONGYEARBYEN, +2.0, new CalendarPoint[] {
                new CalendarPoint(2000, 10, 25, 11, 44, 30),
                new CalendarPoint(2000, 10, 25, 12, 41, 30),
                new CalendarPoint(2000, 10, 25, 13, 35, 30),
                null,
                new CalendarPoint(2000, 10, 26, 12, 41, 30),
                null, null,
            }),
        // https://www.timeanddate.com/sun/antarctica/belgrano-ii-base?month=4&year=2000
        new Example("Belgrano II Base (far-South, a polar night's ending)",
            BELGRANO2, -3.0, new CalendarPoint[] {
                null,
                new CalendarPoint(2000,  8, 17, 11, 22, 30),
                null,
                new CalendarPoint(2000,  8, 18, 10, 56, 30),
                new CalendarPoint(2000,  8, 18, 11, 22, 30),
                new CalendarPoint(2000,  8, 18, 11, 49, 30),
            }),
        // https://www.timeanddate.com/sun/norway/bodo?month=5&year=2000
        new Example("Bodø (near the Arctic Circle, a polar day's beginning)",
            BODOE, +2.0, new CalendarPoint[] {
                new CalendarPoint(2000,  5, 29,  1, 36, 30),
                new CalendarPoint(2000,  5, 29, 12, 59, 30),
                new CalendarPoint(2000,  5, 30,  0, 35, 30),
                new CalendarPoint(2000,  5, 30,  1, 23, 30),
                new CalendarPoint(2000,  5, 30, 12, 59, 30),
                null, null,
            }),
        // https://www.timeanddate.com/sun/@6620709?month=1&year=2000
        new Example("Adelaide Island (near the Antarctic Circle, a polar day's ending)",
            ADELAIDE, -3.0, new CalendarPoint[] {
                null,
                new CalendarPoint(2000,  1,  9, 13, 40, 30),
                null, null,
                new CalendarPoint(2000,  1, 10, 13, 40, 30),
                new CalendarPoint(2000,  1, 11,  1, 28, 30),
                new CalendarPoint(2000,  1, 11,  1, 54, 30),
                new CalendarPoint(2000,  1, 11, 13, 41, 30),
                new CalendarPoint(2000,  1, 12,  1, 10, 30),
            }),
    };

    SunDiurnalPhaseFinder finder = new SunDiurnalPhaseFinder();
    private int seriesCounter;

    @Test
    public void shouldFindInTypicalScenarioInAgreementWithReferenceData() {
        double delta = 30.0 * Calcs.SECOND_TO_DAY;
        for (Example example : EXAMPLES_TYPICAL) {
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
    public void shouldFindInPolarScenarioReasonably() {
        double delta = 2.5 * Calcs.MINUTE_TO_DAY;
        for (Example example : EXAMPLES_POLAR) {
            seriesCounter = 0;
            Iterator<CalendarPoint> expectedLocalDateTimeIt = Arrays.stream(example.expectedLocalDateTimes).iterator();
            finder
                .findMany(example.baseLocalNoon, example.geoCoords)
                .limit(example.expectedLocalDateTimes.length)
                .forEach(optOcc -> {
                    CalendarPoint expectedLocalDateTime = expectedLocalDateTimeIt.next();
                    if (expectedLocalDateTime == null) {
                        assertFalse(optOcc.isPresent(),
                            String.format("Present %s #%d @%s", getExpectedPhase().getTitle(), seriesCounter, example.getTitle()));
                    } else {
                        assertTrue(optOcc.isPresent(),
                            String.format("Absent %s #%d @%s", getExpectedPhase().getTitle(), seriesCounter, example.getTitle()));
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
