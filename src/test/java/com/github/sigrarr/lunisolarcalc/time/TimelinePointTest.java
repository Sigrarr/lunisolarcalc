package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.timeline.JulianDayOutOfPeriodException;


public class TimelinePointTest
{

    @Test
    public void shouldConstructOfLegalValues() {
        assertDoesNotThrow(() -> new TimelinePoint(TimelinePoint.MINIMAL_JULIAN_DAY));
        assertDoesNotThrow(() -> new TimelinePoint(TimelinePoint.MAXIMAL_JULIAN_DAY));
        assertDoesNotThrow(() -> new TimelinePoint(100.0));
    }

    @Test
    public void shouldNotConstructOfIllegalValues() {
        assertThrows(JulianDayOutOfPeriodException.class, () -> new TimelinePoint(TimelinePoint.MINIMAL_JULIAN_DAY - 1.0));
        assertThrows(JulianDayOutOfPeriodException.class, () -> new TimelinePoint(TimelinePoint.MAXIMAL_JULIAN_DAY + 1.0));
    }

}
