package com.github.sigrarr.lunisolarcalc.time.julianform;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.JulianformCalendarPoint.Rules;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class GregorianCalendarPointTest {

    private GregorianCalendarPoint point;

    @Test
    void shouldUseRulesAccordingToDate() {
        assertEquals(Rules.GREGORIAN, GregorianCalendarPoint.FIRST_GREGORIAN_RULES_POINT.getRules());

        TimelinePoint firstGregorian = TimelinePoint.ofCalendarPoint(GregorianCalendarPoint.FIRST_GREGORIAN_RULES_POINT);
        double backwardDayDiffLimit = firstGregorian.julianDay - Timeline.JULIAN_PERIOD_START_JD - Calcs.EPSILON;
        double forwardDayDiffLimit = Timeline.JULIAN_PERIOD_END_JD - firstGregorian.julianDay - Calcs.EPSILON;

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            point = new TimelinePoint(firstGregorian.julianDay + (forwardDayDiffLimit * random.nextDouble())).toGregorianCalendarPoint();
            assertEquals(Rules.GREGORIAN, point.getRules());
            point = new TimelinePoint(firstGregorian.julianDay - (backwardDayDiffLimit * random.nextDouble())).toGregorianCalendarPoint();
            assertEquals(Rules.JULIAN, point.getRules());
        }
    }

    @Test
    void shouldConstructOfLegacyGregorianCalendar() {
        GregorianCalendar legacyGregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

        legacyGregorianCalendar.set(1600, 5, 1, 12, 14, 13);
        point = GregorianCalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new GregorianCalendarPoint(1600, 6, 1.0 + Time.timeToDays(12, 14, 13)), point);

        legacyGregorianCalendar.set(1, 0, 3, 0, 0, 0);
        point = GregorianCalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new GregorianCalendarPoint(1, 1, 3), point);

        legacyGregorianCalendar.set(GregorianCalendar.ERA, GregorianCalendar.BC);
        point = GregorianCalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new GregorianCalendarPoint(0, 1, 3), point);

        legacyGregorianCalendar.set(45, 0, 1, 0, 0, 0);
        point = GregorianCalendarPoint.ofLegacyGregorianCalendar(legacyGregorianCalendar);
        assertEquals(new GregorianCalendarPoint(-44, 1, 1), point);
    }
}
