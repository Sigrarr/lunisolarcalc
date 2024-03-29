package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class DiurnalPhaseFinderAbstractTest {

    private final DiurnalPhaseFinderAbstract[] finders = {new SunDiurnalPhaseFinder(), new MoonDiurnalPhaseFinder()};

    @AfterEach
    public void resetTimelineEquivUnit() {
        Timeline.setEquivUnit(Timeline.DEFAULT_EQUIV_UNIT_DAYS);
    }

    @Test
    public void shouldHandleVariousParameterLists() {
        Timeline.setEquivUnit(1.5 * Calcs.SECOND_TO_DAY);
        CalendarPoint[] baseDates = {new CalendarPoint(1, 1, 1.5), new CalendarPoint(1800, 3, 21.5), new CalendarPoint(2010, 10, 10.5)};
        GeoCoords geoCoords = GeoCoords.ofConventional(0.0, 0.0);

        for (DiurnalPhaseFinderAbstract finder : finders)
            for (CalendarPoint baseDate : baseDates) {
                ArrayList<Optional<UniversalOccurrence<BodyDiurnalPhase>>> all = finder
                    .findMany(baseDate, geoCoords)
                    .limit(45)
                    .collect(Collectors.toCollection(() -> new ArrayList<>(30)));

                Iterator<Optional<UniversalOccurrence<BodyDiurnalPhase>>> transitsIt = IntStream.range(0, 45)
                    .filter(i -> i % 3 == 1)
                    .mapToObj(i -> all.get(i))
                    .iterator();
                finder.findMany(baseDate, geoCoords, DiurnalPhase.TRANSIT)
                    .limit(15)
                    .forEach(optOcc -> assertEquals(transitsIt.next(), optOcc));

                Iterator<Optional<UniversalOccurrence<BodyDiurnalPhase>>> extremeIt = IntStream.range(0, 45)
                    .filter(i -> i % 3 != 1)
                    .mapToObj(i -> all.get(i))
                    .iterator();
                finder.findMany(baseDate, geoCoords, EnumSet.of(DiurnalPhase.RISE, DiurnalPhase.SET))
                    .limit(30)
                    .forEach(optOcc -> assertEquals(extremeIt.next(), optOcc));

                Iterator<Optional<UniversalOccurrence<BodyDiurnalPhase>>> it = all.iterator();
                for (int d = 0; d < 10; d++) {
                    CalendarPoint date = UniversalTimelinePoint.ofCalendar(baseDate).add(d).toCalendarPoint();
                    for (int ph = 0; ph < 3; ph ++) {
                        DiurnalPhase phase = DiurnalPhase.values()[ph % 3];
                        assertEquals(it.next(), finder.find(date, geoCoords, phase));
                    }
                }
            }
    }
}
