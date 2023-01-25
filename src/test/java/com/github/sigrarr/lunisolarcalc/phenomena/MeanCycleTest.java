package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class MeanCycleTest {

    private static final TimelinePoint[] EXAMPLE_POINTS = new TimelinePoint[] {
        Timeline.JULIAN_PERIOD_START_TT,
        TimelinePoint.ofCalendarPoint(new CalendarPoint(-700, 12, 1), TimeType.DYNAMICAL),
        TimelinePoint.ofCalendarPoint(new CalendarPoint(1, 1, 30), TimeType.DYNAMICAL),
        TimelinePoint.ofCalendarPoint(CalendarPoint.GREGORIAN_RULES_START, TimeType.DYNAMICAL),
        Timeline.EPOCH_2000_TT,
        TimelinePoint.ofJulianEphemerisDay(Timeline.EPOCH_2000_JD + 123.15),
        Timeline.JULIAN_PERIOD_END_TT
    };
    private static final EnumMap<MeanCycle, Integer> MONOTONY = new EnumMap<MeanCycle, Integer>(MeanCycle.class) {{
        put(MeanCycle.TROPICAL_YEAR, -1);
        put(MeanCycle.LUNATION, 1);
    }};

    @Test
    public void shouldBeConsistentEpochwise() {
        for (MeanCycle meanCycle : MeanCycle.values()) {
            assertEquals(
                meanCycle.epochalLengthDays,
                meanCycle.calculateLengthDays(meanCycle.epoch),
                Calcs.EPSILON
            );

            for (TimelinePoint tx : EXAMPLE_POINTS)
                assertEquals(
                    meanCycle.calculateLengthDaysBetweenEpochAndTx(tx.toCenturialT()),
                    meanCycle.calculateLengthDaysBetween(meanCycle.epoch, tx),
                    Calcs.EPSILON
                );
        }
    }

    @Test
    public void shouldReflectMonotonyOfLengthFunctionInScope() {
        for (MeanCycle meanCycle : MeanCycle.values()) {
            int monotony = MONOTONY.get(meanCycle);
            for (TimelinePoint tx1 : EXAMPLE_POINTS)
                for (TimelinePoint tx2 : EXAMPLE_POINTS) {
                    double mean1 = meanCycle.calculateLengthDays(tx1);
                    double mean2 = meanCycle.calculateLengthDays(tx2);
                    double meanBetween = meanCycle.calculateLengthDaysBetween(tx1, tx2);

                    assertEquals(Math.signum(tx1.compareTo(tx2)), Math.signum(Double.compare(mean1, mean2) * monotony));
                    assertTrue(meanBetween >= Math.min(mean1, mean2));
                    assertTrue(meanBetween <= Math.max(mean1, mean2));
                }
        }
    }
}
