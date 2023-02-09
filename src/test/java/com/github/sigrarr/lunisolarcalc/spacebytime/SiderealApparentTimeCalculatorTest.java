package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.*;

public class SiderealApparentTimeCalculatorTest {

    private SiderealApparentTimeCalculator calculator = new SiderealApparentTimeCalculator();

    @Test
    public void shouldCalculateThetaZero() {
        // Meeus 1998: Example 12.a, p. 88
        double referenceMeanSiderealTimeDegrees = 360.0 * Calcs.Time.timeToDays(13, 10, 46.3668);
        double referenceDeltaPsi = Math.toRadians(arcsecondsToDegrees(-3.788));
        double referenceEpsilon = Math.toRadians(toSingleDegreesValue(23, 26, 36.85));
        double actualThetaZero = calculator.calculate(referenceMeanSiderealTimeDegrees, referenceDeltaPsi, referenceEpsilon);
        double expectedThetaZero = 360.0 * Calcs.Time.timeToDays(13, 10, 46.1351);
        double delta = TestUtils.decimalAutoDelta(0.0001) * 360.0 * Calcs.SECOND_TO_DAY;
        assertEquals(expectedThetaZero, actualThetaZero, delta);
    }

    @Test
    public void shouldCalculateNutuationInRightAscension() {
        // Meeus 1998: Example 12.a, p. 88
        double referenceDeltaPsi = Math.toRadians(arcsecondsToDegrees(-3.788));
        double referenceEpsilon = Math.toRadians(toSingleDegreesValue(23, 26, 36.85));
        double actualNutuationDegrees = calculator.calculateNutuationInRightAscensionDegrees(referenceDeltaPsi, referenceEpsilon);
        double expectedNutuationDegrees = -0.2317 * 360.0 * Calcs.SECOND_TO_DAY;
        double delta = TestUtils.decimalAutoDelta(0.0001) * 360.0 * Calcs.SECOND_TO_DAY;
        assertEquals(expectedNutuationDegrees, actualNutuationDegrees, delta);
    }
}
