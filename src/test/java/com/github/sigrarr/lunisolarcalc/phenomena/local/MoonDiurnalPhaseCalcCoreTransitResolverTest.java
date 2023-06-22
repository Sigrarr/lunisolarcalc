package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.phenomena.global.*;
import com.github.sigrarr.lunisolarcalc.phenomena.local.MoonDiurnalPhaseCalcCore.TransitResolver;
import com.github.sigrarr.lunisolarcalc.time.*;

public class MoonDiurnalPhaseCalcCoreTransitResolverTest {

    private static final int RADIUS = 15;

    private TransitResolver transitResolver = (TransitResolver) new MoonDiurnalPhaseCalcCore().transitResolver;
    private MoonPhaseFinder phaseFinder = new MoonPhaseFinder();

    @Test
    public void shouldFindEmptyTransitNearFullMoonInNonPolarSituation() {
        GeoCoords[] exampleGeoCoords = {Example.WROCLAW, Example.LEJRE, Example.HONOLULU, Example.SYDNEY};
        TimelinePoint[] exampleAroundPoints = {Timeline.GREGORIAN_CALENDAR_START, Timeline.EPOCH_2000_UT};

        for (GeoCoords geoCoords : exampleGeoCoords)
            for (TimelinePoint around : exampleAroundPoints) {
                resetCoreToStartAtRadiusBefore(findFullMoonAround(around), geoCoords);

                LinkedList<Integer> absentIndexes = new LinkedList<>();
                for (int i = 0; i < RADIUS * 2; i++) {
                    OptionalDouble toTransit = transitResolver.findCentralNoonToTransitVector();
                    if (!toTransit.isPresent())
                        absentIndexes.add(i);
                    forward(toTransit);
                }

                assertEquals(1, absentIndexes.size());
                assertTrue(Math.abs(absentIndexes.getFirst() - RADIUS) <= 1);
            }
    }

    private UniversalTimelinePoint findFullMoonAround(TimelinePoint around) {
        return phaseFinder.findAround(around, MoonPhase.FULL_MOON).getTimelinePoint().toUniversalTime();
    }

    private void resetCoreToStartAtRadiusBefore(UniversalTimelinePoint fullMoon, GeoCoords geoCoords) {
        transitResolver.core.reset(new DiurnalPhaseCalcRequest(
            fullMoon.add(-RADIUS).toLocalTimeCalendarPoint(geoCoords),
            geoCoords,
            EnumSet.of(DiurnalPhase.TRANSIT)
        ));
    }

    private void forward(OptionalDouble noonToTransitVector) {
        transitResolver.core.getDay(0).setFinalTransitByVectorFromNoon(noonToTransitVector);
        transitResolver.core.get();
    }
}
