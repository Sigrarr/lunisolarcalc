package com.github.sigrarr.lunisolarcalc.time;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianDayOutOfPeriodException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelinePointTest
{

    private final Comparator<TimelinePoint> equivOkCmp = TimelinePoint.equivalenceConsistentComparator();

    @AfterEach
    public void resetTimelineEquivUnit() {
        Timeline.setEquivUnit(Timeline.DEFAULT_EQUIV_UNIT_DAYS);
    }

    @Test
    public void shouldConstructOfLegalValues() {
        assertDoesNotThrow(() -> new TimelinePoint(0.0));
        assertDoesNotThrow(() -> new TimelinePoint(Timeline.JULIAN_PERIOD_END_JD));
        assertDoesNotThrow(() -> new TimelinePoint(100.0));
    }

    @Test
    public void shouldNotConstructOfIllegalValues() {
        assertThrows(JulianDayOutOfPeriodException.class, () -> new TimelinePoint(- Calcs.EPSILON));
        assertThrows(JulianDayOutOfPeriodException.class, () -> new TimelinePoint(Timeline.JULIAN_PERIOD_END_JD + Calcs.EPSILON));
    }

    @Test
    public void shouldEquate() {
        TimelinePoint x = TimelinePoint.ofCalendarPoint(new CalendarPoint(-45, 12, 25.51));
        assertConsistentEquivalence(x, TimelinePoint.ofCalendarPoint(new CalendarPoint(-45, 12, 25, 12, 14, 24)));
        assertConsistentNonEquivalence(x, TimelinePoint.ofCalendarPoint(new CalendarPoint(-45, 12, 25.51)).add(Timeline.getEquivUnitDays() + Calcs.EPSILON));
        assertConsistentNonEquivalence(x, TimelinePoint.ofCalendarPoint(new CalendarPoint(-45, 12, 25.51)).add(- Timeline.getEquivUnitDays() - Calcs.EPSILON));

        assertNonEquivalence(TimelinePoint.ofCenturialT(0.0), TimelinePoint.ofCenturialT(0.0).toDynamicalTime());

        Timeline.setEquivUnit(1.0);
        x = TimelinePoint.ofCalendarPoint(new CalendarPoint(1582, 10, 15.5));
        assertConsistentEquivalence(x, x.add(0.5 - Calcs.EPSILON));
        assertConsistentEquivalence(x, x.add(-0.5 + Calcs.EPSILON));
        assertConsistentNonEquivalence(x, x.add(1.0));
        assertConsistentNonEquivalence(x, x.add(-1.0));
    }

    @Test
    public void shouldOrder() {
        double[] centerJds = new double[] {
            Timeline.calendarToJulianDay(new CalendarPoint(1581, 12, 32.0 - Calcs.EPSILON)),
            Timeline.calendarToJulianDay(new CalendarPoint(1582, 1, 1.0)),
            Timeline.GREGORIAN_CALENDAR_START_JD,
            Timeline.calendarToJulianDay(new CalendarPoint(1820, 1, 1.0)),
            Timeline.EPOCH_2000_JD - 0.5,
            Timeline.EPOCH_2000_JD,
            Timeline.calendarToJulianDay(new CalendarPoint(2000, 12, 32.0 - Calcs.EPSILON)),
            Timeline.calendarToJulianDay(new CalendarPoint(2001, 1, 1.0)),
            Timeline.centurialTToJulianDay(0.211111)
        };
        int secondsRadius = 3;
        ArrayList<TimelinePoint> points = new ArrayList<>(centerJds.length * (secondsRadius * 2) * 10 * 4);
        for (double centerJd : centerJds)
            for (int offsetDeciSeconds = -secondsRadius * 10; offsetDeciSeconds < secondsRadius * 10; offsetDeciSeconds++) {
                double jd = centerJd + (Calcs.SECOND_TO_DAY * offsetDeciSeconds * 0.1);
                points.add(new TimelinePoint(jd));
                points.add(new TimelinePoint(jd).toDynamicalTime());
                points.add(TimelinePoint.ofJulianEphemerisDay(jd));
                points.add(TimelinePoint.ofJulianEphemerisDay(jd).toUniversalTime());
            }

        Collections.sort(points);

        for (int i = 0; i < points.size() - 1; i++)
            for (TimeScale timeScale : TimeScale.values()) {
                TimelinePoint prev = points.get(i);
                TimelinePoint next = points.get(i + 1);
                double prevJd = prev.toTimeScale(timeScale).julianDay;
                double nextJd = next.toTimeScale(timeScale).julianDay;
                assertFalse(Calcs.compare(prevJd, nextJd, Timeline.getEquivUnitDays()) > 0);
                // TODO investigate
                if (Timeline.compare(prevJd, nextJd) > 0)
                    System.err.println(String.format("[W] [time scale conversion]/[ordering]-inconsistency: @%s  %s vs %s", timeScale, prev, next));
            }

        Collections.shuffle(points);
        Collections.sort(points, equivOkCmp);

        for (int i = 0; i < points.size() - 1; i++) {
            TimelinePoint prev = points.get(i);
            TimelinePoint next = points.get(i + 1);
            int chronoCmp = prev.compareTo(next);
            assertFalse(chronoCmp > 0);
            if (chronoCmp == 0)
                assertFalse(prev.timeScale.compareTo(next.timeScale) > 0);
        }

        for (TimelinePoint a : points)
            for (TimelinePoint b : points)
                if (a.equals(b))
                    assertConsistentEquivalence(a, b, equivOkCmp);
                else
                    assertConsistentNonEquivalence(a, b, equivOkCmp);
    }
}
