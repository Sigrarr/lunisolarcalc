package com.github.sigrarr.lunisolarcalc.phenomena.global;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class MeanCycleTest {

    private static final DynamicalTimelinePoint[] EXAMPLE_POINTS = new DynamicalTimelinePoint[] {
        Timeline.JULIAN_PERIOD_START_TT,
        DynamicalTimelinePoint.ofCalendaricParameters(-700, 12, 1),
        DynamicalTimelinePoint.ofCalendaricParameters(1, 1, 30),
        DynamicalTimelinePoint.ofCalendarPoint(CalendarPoint.GREGORIAN_RULES_START),
        Timeline.EPOCH_2000_TT,
        new DynamicalTimelinePoint(Timeline.EPOCH_2000_JD + 123.15),
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
            for (DynamicalTimelinePoint tx1 : EXAMPLE_POINTS)
                for (DynamicalTimelinePoint tx2 : EXAMPLE_POINTS) {
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
