package com.github.sigrarr.lunisolarcalc.time.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.time.exceptions.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class NormalCalendaricExpressionTest {

    @AfterEach
    public void resetTimelineEquivUnit() {
        Timeline.setEquivUnit(Timeline.DEFAULT_EQUIV_UNIT_DAYS);
    }

    @Test
    public void shouldThrowForFormallyInvalidDateTimeParameters() {
        for (int m : new int[] {-2, -1, 0, 13, 14, 15}) {
            InvalidCalendarMonthException monthException = assertThrows(InvalidCalendarMonthException.class,
                () -> new NormalCalendaricExpression(2000, m, 1));
            assertEquals(m, monthException.getMonth());
        }

        for(double md[] : new double[][] {
            {1.0, 0.0}, {3.0, -1.0}, {5.0, 32.0}, {7.0, 0.999}, {8.0, -0.01}, {10.0, 32.01}, {12.0, 32.0},
            {2.0, 30.0}, {4.0, 31.0}, {6.0, 31.25}, {9.0, 31.01}, {11.0, 31.0}
        }) {
            InvalidCalendarDayException dayException = assertThrows(InvalidCalendarDayException.class,
                () -> new NormalCalendaricExpression(1600, (int) md[0], md[1]));
            assertEquals(md[1], dayException.getDt());
        }

        for (int[] hms : new int[][] {{-1, 0, 0}, {24, 0, 0}, {0, -1, 0}, {0, 60, 0}, {0, 0, -1}, {0, 0, 60}}) {
            InvalidCalendarTimeException timeException = assertThrows(InvalidCalendarTimeException.class,
                () -> new NormalCalendaricExpression(-46, 12, 24, hms[0], hms[1], hms[2]));
            assertEquals(hms[0], timeException.getHours());
            assertEquals(hms[1], timeException.getMinutes());
            assertEquals(hms[2], timeException.getSeconds());
        }

        // time should be validated before date:
        assertThrows(InvalidCalendarTimeException.class, () -> new NormalCalendaricExpression(200, 1, 1, -12, 0, 0));
    }

    @Test
    public void shouldReduceDtToPartialIntegers() {
        NormalCalendaricExpression point = new NormalCalendaricExpression(8, 1, 1.51);
        assertEquals( 1, point.getDay());
        assertEquals(12, point.getHours());
        assertEquals(14, point.getMinutes());
        assertEquals(24, point.getSeconds());
    }

    @Test
    public void shouldFormat() {
        NormalCalendaricExpression point = new NormalCalendaricExpression(8, 1, 1.51);
        assertEquals("+0008-01-01", point.formatDate());
        assertEquals("+0008-01-01 12:14", point.formatDateTimeToMinutes());
        assertEquals("+0008-01-01 12:14:24", point.formatDateTimeToSeconds());
        point = new NormalCalendaricExpression(-45, 12, 24.25);
        assertEquals("−0045-12-24 06:00", point.formatDateTimeToMinutes());
        assertEquals("−0045-12-24 06:00:00", point.formatDateTimeToSeconds());
    }

    @Test
    public void shouldCompareNominally() {
        Comparator<NormalCalendaricExpression> comparator = NormalCalendaricExpression.nominalComparator();

        assertEquals(0, comparator.compare(
            new NormalCalendaricExpression(2000, 1, 1.0),
            new NormalCalendaricExpression(2000, 1, 1)
        ));

        assertEquals(0, comparator.compare(
            new NormalCalendaricExpression(2000, 1, 1, 12, 14, 24),
            new NormalCalendaricExpression(2000, 1, 1.51)
        ));

        assertTrue(comparator.compare(
            new NormalCalendaricExpression(-46, 12, 25.0),
            new NormalCalendaricExpression(-46, 12, 25.0 + Timeline.getEquivUnitDays())
        ) < 0);

        assertTrue(comparator.compare(
            new NormalCalendaricExpression(0, 2, 28.0),
            new NormalCalendaricExpression(0, 2, 28.0 - Timeline.getEquivUnitDays())
        ) > 0);

        Timeline.setEquivUnit(Timeline.MIN_EQUIV_UNIT_DAYS);

        assertEquals(0, comparator.compare(
            new NormalCalendaricExpression(2000, 1, 1.0 + Calcs.EPSILON),
            new NormalCalendaricExpression(2000, 1, 1.0 + Calcs.EPSILON)
        ));

        assertTrue(comparator.compare(
            new NormalCalendaricExpression(-46, 12, 25.123456),
            new NormalCalendaricExpression(-46, 12, 25.123456 + Timeline.getEquivUnitDays())
        ) < 0);

        assertTrue(comparator.compare(
            new NormalCalendaricExpression(0, 2, 28.111111 + Timeline.getEquivUnitDays()),
            new NormalCalendaricExpression(0, 2, 28.111111)
        ) > 0);

        Timeline.setEquivUnit(1.0);

        assertEquals(0, comparator.compare(
            new NormalCalendaricExpression(2000, 1, 1.5),
            new NormalCalendaricExpression(2000, 1, 2.0)
        ));

        assertEquals(0, comparator.compare(
            new NormalCalendaricExpression(-46, 12, 25.5 - Calcs.EPSILON),
            new NormalCalendaricExpression(-46, 12, 24.5)
        ));

        assertTrue(comparator.compare(
            new NormalCalendaricExpression(2000, 1, 1.5),
            new NormalCalendaricExpression(2000, 1, 2.5)
        ) < 0);

        assertTrue(comparator.compare(
            new NormalCalendaricExpression(-46, 12, 25.5),
            new NormalCalendaricExpression(-46, 12, 25.0)
        ) > 0);
    }
}
