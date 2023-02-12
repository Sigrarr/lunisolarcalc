package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EclipticMeanObliquityCalculatorTest {

    private EclipticMeanObliquityCalculator calculator = new EclipticMeanObliquityCalculator();

    @Test
    public void shouldCalculateEpsilonZero() {
        // Meeus 1998: Example 22.a, p. 148
        TimelinePoint tx = DynamicalTimelinePoint.ofCalendaricParameters(1987, 4, 10.0);
        double actualEpsilonZeroDegrees = Math.toDegrees(calculator.calculate(tx));
        double expectedEpsilonZeroDegrees = toSingleDegreesValue(23, 26, 27.407);
        double delta = decimalAutoDelta(0.001) * Calcs.ARCSECOND_TO_DEGREE;
        assertEquals(expectedEpsilonZeroDegrees, actualEpsilonZeroDegrees, delta);

        // Meeus 1998, Example 25.b, p. 169
        tx = new DynamicalTimelinePoint(2448908.5);
        actualEpsilonZeroDegrees = Math.toDegrees(calculator.calculate(tx));
        double referenceTrueEpsilonDegrees = 23.4401443;
        double referenceDeltaEpsilonDegrees = arcsecondsToDegrees(-0.308);
        double impliedExpectedEpsilonZeroDegrees = referenceTrueEpsilonDegrees - referenceDeltaEpsilonDegrees;
        delta = 0.001 * Calcs.ARCSECOND_TO_DEGREE;
        assertEquals(impliedExpectedEpsilonZeroDegrees, actualEpsilonZeroDegrees, delta);
    }
}
