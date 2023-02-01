package com.github.sigrarr.lunisolarcalc.time;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianDayOutOfPeriodException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelinePointTest
{

    private final static Comparator<TimelinePoint> AUTO_CMP = (a, b) -> {
        if (a.getTimeScale() != b.getTimeScale())
            throw new IllegalArgumentException();
        switch (a.getTimeScale()) {
            case UNIVERSAL:
                return a.toUniversalTime().compareTo(b.toUniversalTime());
            case DYNAMICAL:
            default:
                return a.toDynamicalTime().compareTo(b.toDynamicalTime());
        }
    };

    @AfterEach
    public void resetTimelineEquivUnit() {
        Timeline.setEquivUnit(Timeline.DEFAULT_EQUIV_UNIT_DAYS);
    }

    @Test
    public void shouldConstructOfLegalValues() {
        assertDoesNotThrow(() -> new DynamicalTimelinePoint(0.0));
        assertDoesNotThrow(() -> new UniversalTimelinePoint(Timeline.JULIAN_PERIOD_END_JD));
        assertDoesNotThrow(() -> new UniversalTimelinePoint(100.0));
    }

    @Test
    public void shouldNotConstructOfIllegalValues() {
        assertThrows(JulianDayOutOfPeriodException.class, () -> new UniversalTimelinePoint(- Calcs.EPSILON));
        assertThrows(JulianDayOutOfPeriodException.class, () -> new DynamicalTimelinePoint(Timeline.JULIAN_PERIOD_END_JD + Calcs.EPSILON));
    }

    @Test
    public void shouldEquate() {
        UniversalTimelinePoint x1 = UniversalTimelinePoint.ofCalendaricParameters(-45, 12, 25.51);
        assertConsistentEquivalence(x1, UniversalTimelinePoint.ofCalendaricParameters(-45, 12, 25, 12, 14, 24));
        assertConsistentNonEquivalence(x1, UniversalTimelinePoint.ofCalendaricParameters(-45, 12, 25.51).add(Timeline.getEquivUnitDays() + Calcs.EPSILON));
        assertConsistentNonEquivalence(x1, UniversalTimelinePoint.ofCalendaricParameters(-45, 12, 25.51).add(- Timeline.getEquivUnitDays() - Calcs.EPSILON));

        assertNonEquivalence(UniversalTimelinePoint.ofCenturialT(1.0), DynamicalTimelinePoint.ofCenturialT(1.0));
        assertNonEquivalence(UniversalTimelinePoint.ofCenturialT(0.0), UniversalTimelinePoint.ofCenturialT(0.0).toDynamicalTime());

        Timeline.setEquivUnit(1.0);
        DynamicalTimelinePoint x2 = DynamicalTimelinePoint.ofCalendaricParameters(1582, 10, 15.5);
        assertConsistentEquivalence(x2, x2.add(0.5 - Calcs.EPSILON));
        assertConsistentEquivalence(x2, x2.add(-0.5 + Calcs.EPSILON));
        assertConsistentNonEquivalence(x2, x2.add(1.0));
        assertConsistentNonEquivalence(x2, x2.add(-1.0));
    }

    @Test
    public void shouldOrder() {
        double[] centerJds = new double[] {
            Timeline.normalCalendarToJulianDay(new CalendarPoint(1581, 12, 32.0 - Calcs.EPSILON)),
            Timeline.normalCalendarToJulianDay(new CalendarPoint(1582, 1, 1.0)),
            Timeline.GREGORIAN_CALENDAR_START_JD,
            Timeline.normalCalendarToJulianDay(new CalendarPoint(1820, 1, 1.0)),
            Timeline.EPOCH_2000_JD - 0.5,
            Timeline.EPOCH_2000_JD,
            Timeline.normalCalendarToJulianDay(new CalendarPoint(2000, 12, 32.0 - Calcs.EPSILON)),
            Timeline.normalCalendarToJulianDay(new CalendarPoint(2001, 1, 1.0)),
            Timeline.centurialTToJulianDay(0.211111)
        };
        int secondsRadius = 3;
        ArrayList<TimelinePoint> points = new ArrayList<>(centerJds.length * (secondsRadius * 2) * 10 * 4);
        for (double centerJd : centerJds)
            for (int offsetDeciSeconds = -secondsRadius * 10; offsetDeciSeconds < secondsRadius * 10; offsetDeciSeconds++) {
                double jd = centerJd + (Calcs.SECOND_TO_DAY * offsetDeciSeconds * 0.1);
                points.add(new UniversalTimelinePoint(jd));
                points.add(new UniversalTimelinePoint(jd).toDynamicalTime());
                points.add(new DynamicalTimelinePoint(jd));
                points.add(new DynamicalTimelinePoint(jd).toUniversalTime());
            }

        Collections.shuffle(points);

        Map<TimeScale, List<TimelinePoint>> sortedInTimeScale = new EnumMap<>(TimeScale.class);
        sortedInTimeScale.put(TimeScale.DYNAMICAL, points.stream()
            .map(TimelinePoint::toDynamicalTime)
            .sorted()
            .collect(Collectors.toList()));
        sortedInTimeScale.put(TimeScale.UNIVERSAL, points.stream()
            .map(TimelinePoint::toUniversalTime)
            .sorted()
            .collect(Collectors.toList()));

        for (TimeScale timeScale : TimeScale.values()) {
            List<TimelinePoint> sortedPoints = sortedInTimeScale.get(timeScale);
            for (int i = 0; i < sortedPoints.size() - 1; i++) {
                TimelinePoint prev = sortedPoints.get(i);
                TimelinePoint next = sortedPoints.get(i + 1);

                assertFalse(Calcs.compare(prev.julianDay, next.julianDay, Timeline.getEquivUnitDays()) > 0);
                assertFalse(Timeline.compare(prev.julianDay, next.julianDay) > 0);

                if (prev.equals(next))
                    assertConsistentEquivalence(prev, next, AUTO_CMP);
                else
                    assertConsistentNonEquivalence(prev, next, AUTO_CMP);
            }
        }
    }
}
