package com.github.sigrarr.lunisolarcalc.time.julianform;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.*;

import com.github.sigrarr.lunisolarcalc.time.*;

import org.junit.jupiter.api.Test;

public class ProlepticGregorianCalendarPointTest {

    private ProlepticGregorianCalendarPoint point;

    @Test
    void shouldConstructOfLocalDateTimeClasses() {
        point = ProlepticGregorianCalendarPoint.ofLocalDate(LocalDate.of(1600, 6, 1));
        assertEquals(new ProlepticGregorianCalendarPoint(1600, 6, 1), point);

        point = ProlepticGregorianCalendarPoint.ofLocalDate(LocalDate.of(0, 12, 31));
        assertEquals(new ProlepticGregorianCalendarPoint(0, 12, 31), point);

        point = ProlepticGregorianCalendarPoint.ofLocalDate(LocalDate.of(-44, 1, 1));
        assertEquals(new ProlepticGregorianCalendarPoint(-44, 1, 1), point);

        point = ProlepticGregorianCalendarPoint.ofLocalDateTime(LocalDateTime.of(-44, 1, 1, 12, 14, 13));
        assertEquals(new ProlepticGregorianCalendarPoint(-44, 1, 1.0 + Time.timeToDays(12, 14, 13)), point);
    }
}
