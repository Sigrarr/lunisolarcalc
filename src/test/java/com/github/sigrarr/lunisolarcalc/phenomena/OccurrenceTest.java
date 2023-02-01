package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class OccurrenceTest {

    private final static Comparator<Occurrence<?>> AUTO_CMP = (a, b) -> {
        if (a.getTimelinePoint().getTimeScale() != b.getTimelinePoint().getTimeScale())
            throw new IllegalArgumentException();
        switch (a.getTimelinePoint().getTimeScale()) {
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
    public void shouldEquate() {
        TimelinePoint x1 = Timeline.EPOCH_2000_TT.add(Calcs.Time.timeToDays(1, 15, 30));
        Occurrence<ABC> x1A = new DynamicalOccurrence<>(x1, ABC.A);
        assertEquivalence(x1A, new DynamicalOccurrence<>(x1, ABC.A));
        assertNonEquivalence(x1A, new DynamicalOccurrence<>(x1, ABC.B));
        assertNonEquivalence(x1A, new DynamicalOccurrence<>(x1.add(Timeline.getEquivUnitDays() + Calcs.EPSILON), ABC.A));

        assertNonEquivalence(x1A, new DynamicalOccurrence<>(x1, GreekAbg.ALPHA));
        assertNonEquivalence(x1A, new UniversalOccurrence<>(x1, ABC.A));
        assertNonEquivalence(x1A, new UniversalOccurrence<>(new UniversalTimelinePoint(x1.julianDay), ABC.A));

        Timeline.setEquivUnit(1.0);
        TimelinePoint x2 = UniversalTimelinePoint.ofCalendaricParameters(-46, 12, 25.5);
        Occurrence<ABC> x2c = new UniversalOccurrence<>(x2, ABC.C);
        assertEquivalence(x2c, new UniversalOccurrence<>(x2.add(0.5 - Calcs.EPSILON), ABC.C));
        assertEquivalence(x2c, new UniversalOccurrence<>(x2.add(-0.5 + Calcs.EPSILON), ABC.C));
        assertNonEquivalence(x2c, new UniversalOccurrence<>(x2.add(0.5 + Calcs.EPSILON), ABC.C));
        assertNonEquivalence(x2c, new UniversalOccurrence<>(x2.add(-0.5 - Calcs.EPSILON), ABC.C));
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
        int secondsRadius = 1;
        ArrayList<Occurrence<?>> occurrences = new ArrayList<>(centerJds.length * (secondsRadius * 2) * 10 * 24);
        for (double centerJd : centerJds)
            for (int offsetDeciSeconds = -secondsRadius * 10; offsetDeciSeconds < secondsRadius * 10; offsetDeciSeconds++) {
                double jd = centerJd + (Calcs.SECOND_TO_DAY * offsetDeciSeconds * 0.1);
                for (ABC abc : ABC.values()) {
                    occurrences.add(new DynamicalOccurrence<ABC>(new DynamicalTimelinePoint(jd), abc));
                    occurrences.add(new UniversalOccurrence<ABC>(new UniversalTimelinePoint(jd), abc));
                    occurrences.add(new DynamicalOccurrence<ABC>(new UniversalTimelinePoint(jd), abc));
                    occurrences.add(new UniversalOccurrence<ABC>(new DynamicalTimelinePoint(jd), abc));
                }
                for (GreekAbg abg : GreekAbg.values()) {
                    occurrences.add(new DynamicalOccurrence<GreekAbg>(new DynamicalTimelinePoint(jd), abg));
                    occurrences.add(new UniversalOccurrence<GreekAbg>(new UniversalTimelinePoint(jd), abg));
                    occurrences.add(new DynamicalOccurrence<GreekAbg>(new UniversalTimelinePoint(jd), abg));
                    occurrences.add(new UniversalOccurrence<GreekAbg>(new DynamicalTimelinePoint(jd), abg));
                }
            }

        Collections.shuffle(occurrences);

        Map<TimeScale, List<Occurrence<?>>> sortedInTimeScale = new EnumMap<>(TimeScale.class);
        sortedInTimeScale.put(TimeScale.DYNAMICAL, occurrences.stream()
            .map(Occurrence::toDynamicalTime)
            .sorted()
            .collect(Collectors.toList()));
        sortedInTimeScale.put(TimeScale.UNIVERSAL, occurrences.stream()
            .map(Occurrence::toUniversalTime)
            .sorted()
            .collect(Collectors.toList()));

        for (TimeScale timeScale : TimeScale.values()) {
            List<Occurrence<?>> sortedOccurrences = sortedInTimeScale.get(timeScale);
            for (int i = 0; i < sortedOccurrences.size() - 1; i++) {
                Occurrence<?> prev = sortedOccurrences.get(i);
                Occurrence<?> next = sortedOccurrences.get(i + 1);

                assertFalse(Calcs.compare(prev.getTimelinePoint().julianDay, next.getTimelinePoint().julianDay, Timeline.getEquivUnitDays()) > 0);

                int chronoCmpInForce = Timeline.compare(prev.getTimelinePoint().julianDay, next.getTimelinePoint().julianDay);
                assertFalse(chronoCmpInForce > 0);

                if (chronoCmpInForce == 0) {
                    int typeClassCmp = prev.type.getClass().getName().compareTo(next.type.getClass().getName());
                    assertFalse(typeClassCmp > 0);
                    if (typeClassCmp == 0)
                        assertFalse(Integer.compare(prev.type.ordinal(), next.type.ordinal()) > 0);
                }

                if (prev.equals(next))
                    assertConsistentEquivalence(prev, next, AUTO_CMP);
                else
                    assertConsistentNonEquivalence(prev, next, AUTO_CMP);
            }
        }
    }

    private enum ABC {
        A, B, C
    }

    private enum GreekAbg {
        ALPHA, BETA, GAMMA
    }
}
