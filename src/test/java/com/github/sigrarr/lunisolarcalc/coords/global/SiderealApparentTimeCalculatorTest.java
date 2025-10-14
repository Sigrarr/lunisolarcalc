package com.github.sigrarr.lunisolarcalc.coords.global;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.testing.TestUtils;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.CalcComposition;

public class SiderealApparentTimeCalculatorTest {

    private SiderealApparentTimeCalculator calculator = new SiderealApparentTimeCalculator();
    private CalcComposition<GlobalCoord, TimelinePoint> composedCalculator = CoordsCalcCompositions.compose(GlobalCoord.SIDEREAL_APPARENT_TIME_0);

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

        // Meeus 1998: Example 40.a, p. 280
        UniversalTimelinePoint tx = UniversalTimelinePoint.ofCalendaricParameters(2003,  8, 28,  3, 17,  0);
        expectedThetaZero = 360.0 * Calcs.Time.timeToDays(1, 40, 45);
        delta = TestUtils.decimalAutoDelta(1.0) * 360.0 * Calcs.SECOND_TO_DAY;
        assertEquals(expectedThetaZero, (Double) composedCalculator.calculate(tx), delta);
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
