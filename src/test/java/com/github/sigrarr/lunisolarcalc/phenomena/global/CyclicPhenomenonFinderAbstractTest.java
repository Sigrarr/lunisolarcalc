package com.github.sigrarr.lunisolarcalc.phenomena.global;

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
    public void shouldValidatePrecisionSetting() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            PrecisionTimeNotPositiveException exT = assertThrows(PrecisionTimeNotPositiveException.class, () -> finder.setPrecisionTime(0));
            assertEquals(0, exT.getSeconds());

            exT = assertThrows(PrecisionTimeNotPositiveException.class, () -> finder.setPrecisionTime(-1));
            assertEquals(-1, exT.getSeconds());

            PrecisionAngleTooSmallException exA = assertThrows(PrecisionAngleTooSmallException.class, () -> finder.setPrecision(0.0));
            assertEquals(0.0, exA.getRadians());

            exA = assertThrows(PrecisionAngleTooSmallException.class, () -> finder.setPrecision(
                CyclicPhenomenonFinderAbstract.MIN_PRECISION_RADIANS - Calcs.EPSILON_MIN
            ));
            exA = assertThrows(PrecisionAngleTooSmallException.class, () -> finder.setPrecision(-1.0));
            assertEquals(-1.0, exA.getRadians(), Calcs.EPSILON_MIN);
        }
    }

    @Test
    public void shouldMinTimeDeltaYieldLegalPrecision() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            finder.setPrecisionTime(1);
            double radians = finder.getMeanCycle().radiansPerTimeSeconds(1);
            assertEquals(radians, finder.getPrecision(), Calcs.EPSILON_MIN);
            assertDoesNotThrow(() -> finder.setPrecision(radians));
        }
    }

    @Test
    public void shouldConvertDeltaBetweenSecondsAndRadians() {
        int[] secondSettings = {2, 4, 13, 119};
        for (int seconds : secondSettings)
            for (CyclicPhenomenonFinderAbstract finder : finders) {
                finder.setPrecisionTime(seconds);
                double radians = finder.getMeanCycle().radiansPerTimeSeconds(seconds);
                assertEquals(radians, finder.getPrecision(), Calcs.EPSILON_MIN);

                finder.setPrecision(radians);
                assertEquals(seconds, Math.round(finder.getPrecisionTimeSeconds()));
            }
    }
}
