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
    private static final Example[] EXAMPLES_TYPICAL = {
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

    MoonDiurnalPhaseFinder finder = new MoonDiurnalPhaseFinder();
    private int seriesCounter;

    @Test
    public void shouldFindInTypicalSituationInAgreementWithReferenceData() {
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

    private DiurnalPhase getExpectedPhase() {
        return DiurnalPhase.values()[seriesCounter % 3];
    }
}
