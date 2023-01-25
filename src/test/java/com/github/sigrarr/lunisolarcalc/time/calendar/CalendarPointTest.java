package com.github.sigrarr.lunisolarcalc.time.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianGregorianCalendarSkippedDateException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class CalendarPointTest {

    private CalendarPoint point;

    @Test
    public void shouldThrowForOct1582SkippedDateConstruction() {
        for (int d = 1; d <= 4; d++)
            new CalendarPoint(1582, 10, d);

        for (int d = 5; d <= 14; d++) {
            int nonexistentDay = d;
            JulianGregorianCalendarSkippedDateException skippedDateException = assertThrows(JulianGregorianCalendarSkippedDateException.class,
                () -> new CalendarPoint(1582, 10, nonexistentDay));
            assertEquals(nonexistentDay, skippedDateException.getDay());
        }

        for (int d = 15; d <= 31; d++)
            new CalendarPoint(1582, 10, d);
    }

    @Test
    void shouldConstructOfLegacyGregorianCalendar() {
        GregorianCalendar legacyGregorianCalendar = new GregorianCalendar(CalendarPoint.TIME_ZONE);

        legacyGregorianCalendar.set(1600, GregorianCalendar.JUNE, 1, 12, 14, 13);
        point = CalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new CalendarPoint(1600, 6, 1.0 + Calcs.Time.timeToDays(12, 14, 13)), point);

        legacyGregorianCalendar.set(1, GregorianCalendar.JANUARY, 3, 0, 0, 0);
        legacyGregorianCalendar.setTimeZone(TimeZone.getTimeZone("US/Pacific"));
        point = CalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new CalendarPoint(1, 1, 3), point);
        assertEquals(TimeZone.getTimeZone("US/Pacific").getRawOffset(), legacyGregorianCalendar.getTimeZone().getRawOffset());

        legacyGregorianCalendar.set(GregorianCalendar.ERA, GregorianCalendar.BC);
        point = CalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new CalendarPoint(0, 1, 3), point);

        legacyGregorianCalendar.set(45, GregorianCalendar.JANUARY, 1, 0, 0, 0);
        point = CalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new CalendarPoint(-44, 1, 1), point);
    }

    @Test
    public void shouldConvertToLegacyGregorianCalendar() {
        GregorianCalendar legacyGregorianCalendar;

        legacyGregorianCalendar = new CalendarPoint(1600, 6, 1.0 + Calcs.Time.timeToDays(12, 14, 13)).toLegacyGregorianCalendar();
        assertLegacyGregorianCalendarConversionOutput(legacyGregorianCalendar, new int[] { GregorianCalendar.AD, 1600, GregorianCalendar.JUNE, 1, 12, 14, 13 });

        legacyGregorianCalendar = new CalendarPoint(1, 1, 3).toLegacyGregorianCalendar();
        assertLegacyGregorianCalendarConversionOutput(legacyGregorianCalendar, new int[] { GregorianCalendar.AD, 1, GregorianCalendar.JANUARY, 3, 0, 0, 0 });

        legacyGregorianCalendar = new CalendarPoint(0, 1, 3).toLegacyGregorianCalendar();
        assertLegacyGregorianCalendarConversionOutput(legacyGregorianCalendar, new int[] { GregorianCalendar.BC, 1, GregorianCalendar.JANUARY, 3, 0, 0, 0 });

        legacyGregorianCalendar = new CalendarPoint(-44, 12, 1).toLegacyGregorianCalendar();
        assertLegacyGregorianCalendarConversionOutput(legacyGregorianCalendar, new int[] { GregorianCalendar.BC, 45, GregorianCalendar.DECEMBER, 1, 0, 0, 0 });
    }

    private void assertLegacyGregorianCalendarConversionOutput(GregorianCalendar output, int[] expectedValues) {
        assertEquals(output.getTimeZone().getRawOffset(), CalendarPoint.TIME_ZONE.getRawOffset());
        assertArrayEquals(expectedValues, new int[] {
            output.get(GregorianCalendar.ERA),
            output.get(GregorianCalendar.YEAR),
            output.get(GregorianCalendar.MONTH),
            output.get(GregorianCalendar.DAY_OF_MONTH),
            output.get(GregorianCalendar.HOUR_OF_DAY),
            output.get(GregorianCalendar.MINUTE),
            output.get(GregorianCalendar.SECOND)
        });
    }
}
