package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class CyclicPhenomenonFinderAbstractTest {

    private final CyclicPhenomenonFinderAbstract[] finders = {new SunSeasonPointFinder(), new MoonPhaseFinder()};

    @Test
    public void shouldThrowCalculationLimitExceededException() {
        int[] limitSettings = {1, 2, 5, 10, 15, 25};
        for (int limit : limitSettings) {
            for (CyclicPhenomenonFinderAbstract finder : finders) {
                finder.resetFinding();
                finder.setCoreCalculationsLimit(limit);
                for (int i = 0; i < limit; i++)
                    finder.calculateStageIndicatingAngle(0.0);
                CalculationLimitExceededException exception = assertThrows(CalculationLimitExceededException.class, () -> finder.calculateStageIndicatingAngle(0.0));
                assertEquals(limit, exception.getLimit());
            }
        }
    }

    @Test
    public void shouldThrowDeltaTooLowException() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            DeltaTimeNotPositiveException exT = assertThrows(DeltaTimeNotPositiveException.class, () -> finder.setAngularDeltaTime(0));
            assertEquals(0, exT.getSeconds());

            exT = assertThrows(DeltaTimeNotPositiveException.class, () -> finder.setAngularDeltaTime(-1));
            assertEquals(-1, exT.getSeconds());

            DeltaAngleTooSmallException exA = assertThrows(DeltaAngleTooSmallException.class, () -> finder.setAngularDelta(0.0));
            assertEquals(0.0, exA.getRadians());

            exA = assertThrows(DeltaAngleTooSmallException.class, () -> finder.setAngularDelta(
                CyclicPhenomenonFinderAbstract.MIN_ANGULAR_DELTA_RADIANS - Calcs.EPSILON_MIN
            ));
            exA = assertThrows(DeltaAngleTooSmallException.class, () -> finder.setAngularDelta(-1.0));
            assertEquals(-1.0, exA.getRadians(), Calcs.EPSILON_MIN);
        }
    }

    @Test
    public void shouldMinTimeDeltaYieldLegalAngularDelta() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            finder.setAngularDeltaTime(1);
            double radians = finder.getMeanCycle().radiansPerTimeSeconds(1);
            assertEquals(radians, finder.getAngularDelta(), Calcs.EPSILON_MIN);
            assertDoesNotThrow(() -> finder.setAngularDelta(radians));
        }
    }

    @Test
    public void shouldConvertDeltaBetweenSecondsAndRadians() {
        int[] secondSettings = {2, 4, 13, 119};
        for (int seconds : secondSettings)
            for (CyclicPhenomenonFinderAbstract finder : finders) {
                finder.setAngularDeltaTime(seconds);
                double radians = finder.getMeanCycle().radiansPerTimeSeconds(seconds);
                assertEquals(radians, finder.getAngularDelta(), Calcs.EPSILON_MIN);

                finder.setAngularDelta(radians);
                assertEquals(seconds, Math.round(finder.getAngularDeltaTimeSeconds()));
            }
    }
}
