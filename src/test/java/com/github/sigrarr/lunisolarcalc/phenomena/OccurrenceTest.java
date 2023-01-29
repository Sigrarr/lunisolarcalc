package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.*;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class OccurrenceTest {

    private final Comparator<Occurrence<?>> equivOkCmp = Occurrence.equivalenceConsistentComparator();

    @AfterEach
    public void resetTimelineEquivUnit() {
        Timeline.setEquivUnit(Timeline.DEFAULT_EQUIV_UNIT_DAYS);
    }

    @Test
    public void shouldEquate() {
        TimelinePoint now = TimelinePoint.ofNow();
        Occurrence<ABC> nowA = new Occurrence<>(now, ABC.A);
        assertEquivalence(nowA, new Occurrence<>(now, ABC.A));
        assertNonEquivalence(nowA, new Occurrence<>(now, ABC.B));
        assertNonEquivalence(nowA, new Occurrence<>(now.add(Timeline.getEquivUnitDays() + Calcs.EPSILON), ABC.A));

        assertNonEquivalence(nowA, new Occurrence<>(now, GreekAbg.ALPHA));
        assertNonEquivalence(nowA, new Occurrence<>(now.toDynamicalTime(), ABC.A));

        Timeline.setEquivUnit(1.0);
        TimelinePoint tx = TimelinePoint.ofCalendarPoint(new CalendarPoint(-46, 12, 25.5));
        Occurrence<ABC> xc = new Occurrence<>(tx, ABC.C);
        assertEquivalence(xc, new Occurrence<>(tx.add(0.5 - Calcs.EPSILON), ABC.C));
        assertEquivalence(xc, new Occurrence<>(tx.add(-0.5 + Calcs.EPSILON), ABC.C));
        assertNonEquivalence(xc, new Occurrence<>(tx.add(0.5 + Calcs.EPSILON), ABC.C));
        assertNonEquivalence(xc, new Occurrence<>(tx.add(-0.5 - Calcs.EPSILON), ABC.C));
    }

    @Test
    public void shouldOrder() {
        Occurrence<?>[] occurrences = new Occurrence<?>[] {
            new Occurrence<>(TimelinePoint.ofCenturialT(0.0), ABC.B),
            new Occurrence<>(TimelinePoint.ofCenturialT(0.0).toDynamicalTime(), ABC.B),
            new Occurrence<>(TimelinePoint.ofCenturialT(0.0), GreekAbg.GAMMA),
            new Occurrence<>(TimelinePoint.ofCenturialT(0.0), ABC.A),
            new Occurrence<>(TimelinePoint.ofCenturialT(0.0), ABC.C),
            new Occurrence<>(TimelinePoint.ofCenturialT(0.0, TimeScale.DYNAMICAL), ABC.B),
            new Occurrence<>(TimelinePoint.ofCenturialT(-3.6), ABC.A),
            new Occurrence<>(TimelinePoint.ofCenturialT(-3.6).toDynamicalTime(), GreekAbg.BETA),
            new Occurrence<>(TimelinePoint.ofCenturialT(1.1), ABC.C)
        };

        Arrays.sort(occurrences);

        for (int i = 0; i < occurrences.length - 1; i++)
            assertFalse(occurrences[i].timelinePoint.compareTo(occurrences[i + 1].timelinePoint) > 0);

        Arrays.sort(occurrences, equivOkCmp);

        for (int i = 0; i < occurrences.length - 1; i++) {
            Occurrence<?> prev = occurrences[i];
            Occurrence<?> next = occurrences[i + 1];
            int chronoCmp = prev.timelinePoint.compareTo(next.timelinePoint);
            assertFalse(chronoCmp > 0);
            if (chronoCmp == 0) {
                int typeClassCmp = prev.type.getClass().getName().compareTo(next.type.getClass().getName());
                assertFalse(typeClassCmp > 0);
                if (typeClassCmp == 0) {
                    int typeCmp = Integer.compare(prev.type.ordinal(), next.type.ordinal());
                    assertFalse(typeCmp > 0);
                    if (typeCmp == 0)
                        assertFalse(prev.timelinePoint.timeScale.compareTo(next.timelinePoint.timeScale) > 0);
                }
            }
        }

        for (Occurrence<?> o : occurrences)
            for (Occurrence<?> p : occurrences)
                if (o.equals(p))
                    assertConsistentEquivalence(o, p, equivOkCmp);
                else
                    assertConsistentNonEquivalence(o, p, equivOkCmp);
    }

    private enum ABC {
        A, B, C
    }

    private enum GreekAbg {
        ALPHA, BETA, GAMMA
    }
}
