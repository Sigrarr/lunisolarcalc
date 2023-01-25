package com.github.sigrarr.lunisolarcalc.time.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class ProlepticGregorianCalendarPointTest {

    private ProlepticGregorianCalendarPoint point;

    @Test
    void shouldConstructOfJavaTimeClasses() {
        point = ProlepticGregorianCalendarPoint.ofLocalDate(LocalDate.of(1600, 6, 1));
        assertEquals(new ProlepticGregorianCalendarPoint(1600, 6, 1), point);

        point = ProlepticGregorianCalendarPoint.ofLocalDate(LocalDate.of(0, 12, 31));
        assertEquals(new ProlepticGregorianCalendarPoint(0, 12, 31), point);

        point = ProlepticGregorianCalendarPoint.ofLocalDate(LocalDate.of(-44, 1, 1));
        assertEquals(new ProlepticGregorianCalendarPoint(-44, 1, 1), point);

        point = ProlepticGregorianCalendarPoint.ofLocalDateTime(LocalDateTime.of(-44, 1, 1, 12, 14, 13));
        assertEquals(new ProlepticGregorianCalendarPoint(-44, 1, 1.0 + Calcs.Time.timeToDays(12, 14, 13)), point);
    }

    @Test
    void shouldConvertToJavaTimeClasses() {
        LocalDate localDate;

        localDate = new ProlepticGregorianCalendarPoint(1600, 6, 1).toLocalDate();
        assertLocalDateConversionOutput(localDate, new int[] { 1600, 6, 1 });

        localDate = new ProlepticGregorianCalendarPoint(0, 12, 31).toLocalDate();
        assertLocalDateConversionOutput(localDate, new int[] { 0, 12, 31 });

        localDate = new ProlepticGregorianCalendarPoint(-44, 1, 1).toLocalDate();
        assertLocalDateConversionOutput(localDate, new int[] { -44, 1, 1 });

        LocalDateTime localDateTime = new ProlepticGregorianCalendarPoint(-44, 1, 1.0 + Calcs.Time.timeToDays(12, 14, 13)).toLocalDateTime();
        assertLocalDateTimeConversionOutput(localDateTime, new int[] { -44, 1, 1, 12, 14, 13 });
        localDateTime = new ProlepticGregorianCalendarPoint(-44, 1, 1.0 + Calcs.Time.timeToDays(12, 14, 56)).toLocalDateTime();
        assertLocalDateTimeConversionOutput(localDateTime, new int[] { -44, 1, 1, 12, 14, 56 });
    }

    private void assertLocalDateConversionOutput(LocalDate localDate, int[] expectedValues) {
        assertArrayEquals(expectedValues, new int[] {
            localDate.getYear(),
            localDate.getMonth().getValue(),
            localDate.getDayOfMonth()
        });
    }

    private void assertLocalDateTimeConversionOutput(LocalDateTime localDateTime, int[] expectedValues) {
        assertArrayEquals(expectedValues, new int[] {
            localDateTime.getYear(),
            localDateTime.getMonth().getValue(),
            localDateTime.getDayOfMonth(),
            localDateTime.getHour(),
            localDateTime.getMinute(),
            localDateTime.getSecond()
        });
    }
}
