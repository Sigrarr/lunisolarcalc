package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.Example.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;

public class SunDiurnalPhaseFinderTest {

    private static final Example[] EXAMPLES = {
        // https://www.timeanddate.com/sun/poland/wroclaw?month=1&year=2023
        new Example("Wrocław (easy)",
            WROCLAW, +1.0,
            new CalendarPoint(2023, 01, 7.5),
            new CalendarPoint[] {
                new CalendarPoint(2023, 01, 07, 07, 54, 0),
                new CalendarPoint(2023, 01, 07, 11, 57, 0),
                new CalendarPoint(2023, 01, 07, 16, 02, 0),
            }
        ),
    };

    private SunDiurnalPhaseFinder finder = new SunDiurnalPhaseFinder();

    @Test
    public void shouldFind() {
        for (Example example : EXAMPLES) {
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
                        CalendarPoint actualLocalDateTime = example.toLocalDateTime(optOcc.get().getTimelinePoint());
                        assertEquals(
                            expectedLocalDateTime.formatDateTimeToMinutes(),
                            actualLocalDateTime.formatDateTimeToMinutes(),
                            String.format("@%s : %s : %s vs %s",
                                example.getTitle(),
                                optOcc.get().getType().diurnalPhase.getTitle(),
                                expectedLocalDateTime.formatDateTimeToMinutes(),
                                actualLocalDateTime.formatDateTimeToSeconds()
                            )
                        );
                    }
                });
            assertFalse(expectedLocalDateTimeIt.hasNext());
        }
    }
}
