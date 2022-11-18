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
    public void shouldThrowEpsilonSettingTooLowException() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            EpsilonTimeSettingTooLowException exT = assertThrows(EpsilonTimeSettingTooLowException.class, () -> finder.setAngularEpsilonTime(0));
            assertEquals(0, exT.getSeconds());

            exT = assertThrows(EpsilonTimeSettingTooLowException.class, () -> finder.setAngularEpsilonTime(-1));
            assertEquals(-1, exT.getSeconds());

            EpsilonAngleSettingTooLowException exA = assertThrows(EpsilonAngleSettingTooLowException.class, () -> finder.setAngularEpsilon(0.0));
            assertEquals(0.0, exA.getRadians());

            exA = assertThrows(EpsilonAngleSettingTooLowException.class, () -> finder.setAngularEpsilon(Calcs.EPSILON_MIN));
            exA = assertThrows(EpsilonAngleSettingTooLowException.class, () -> finder.setAngularEpsilon(-1.0));
            assertEquals(-1.0, exA.getRadians(), Calcs.EPSILON_MIN);
        }
    }

    @Test
    public void shouldMinTimeEpsilonYieldLegalAngularEpsilon() {
        for (CyclicPhenomenonFinderAbstract finder : finders) {
            finder.setAngularEpsilonTime(1);
            double radians = finder.cycleTemporalApproximate.radiansPerTimeSeconds(1);
            assertEquals(radians, finder.getAngularEpsilon(), Calcs.EPSILON_MIN);
            assertDoesNotThrow(() -> finder.setAngularEpsilon(radians));
        }
    }

    @Test
    public void shouldConvertEpsilonBetweenSecondsAndRadians() {
        int[] secondSettings = {2, 4, 13, 119};
        for (int seconds : secondSettings)
            for (CyclicPhenomenonFinderAbstract finder : finders) {
                finder.setAngularEpsilonTime(seconds);
                double radians = finder.cycleTemporalApproximate.radiansPerTimeSeconds(seconds);
                assertEquals(radians, finder.getAngularEpsilon(), Calcs.EPSILON_MIN);

                finder.setAngularEpsilon(radians);
                assertEquals(seconds, Math.round(finder.getAngularEpsilonTimeSeconds()));
            }
    }
}
