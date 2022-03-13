package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder.*;
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
    public void shouldThrowMeanPrecisionSettingTooLowException() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            MeanPrecisionSettingTooLowException exception = assertThrows(MeanPrecisionSettingTooLowException.class, () -> finder.getMeanPrecisionRadians(0));
            assertEquals(0, exception.getMeanPrecisionSeconds());
            
            exception = assertThrows(MeanPrecisionSettingTooLowException.class, () -> finder.getMeanPrecisionRadians(-1));
            assertEquals(-1, exception.getMeanPrecisionSeconds());
        }
    }

    @Test
    public void shouldConvertMeanPrecisionFromSecondsToRadians() {
        int[] secondSettings = {1, 100, 1000};
        for (int seconds : secondSettings)
            for (CyclicPhenomenonFinderAbstract finder : finders)
                assertEquals(finder.cycleTemporalApproximate.radiansPerTimeSeconds(seconds), finder.getMeanPrecisionRadians(seconds), Calcs.EPSILON_MIN);
    }
}
