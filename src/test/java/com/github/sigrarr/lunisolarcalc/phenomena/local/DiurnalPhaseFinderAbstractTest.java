package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;

public class DiurnalPhaseFinderAbstractTest {

    DiurnalPhaseFinderAbstract[] finders = {new MoonDiurnalPhaseFinder(), new SunDiurnalPhaseFinder()};

    @Test
    public void shouldHandleApproximationMode() {
        for (DiurnalPhaseFinderAbstract finder : finders) {
            assertFalse(finder.isApproximationModeOn());
            UniversalTimelinePoint correctResult = finder.find(
                Timeline.EPOCH_2000_UT.toCalendarPoint(), GeoCoords.ofPlanetographic(0, 0), DiurnalPhase.TRANSIT
            ).get().getTimelinePoint();
            assertTrue(finder.core.iterationLevel.getCorrectionsCount() > 0);

            finder.setApproximationMode(false);
            assertFalse(finder.isApproximationModeOn());
            finder.setApproximationModeOn();
            assertTrue(finder.isApproximationModeOn());

            UniversalTimelinePoint approximateResult = finder.find(
                Timeline.EPOCH_2000_UT.toCalendarPoint(), GeoCoords.ofPlanetographic(0, 0), DiurnalPhase.TRANSIT
            ).get().getTimelinePoint();
            assertEquals(0, finder.core.iterationLevel.getCorrectionsCount());

            double absDiff = Math.abs(approximateResult.julianDay - correctResult.julianDay);
            assertTrue(absDiff < 1.0/24);
        }
    }
}
