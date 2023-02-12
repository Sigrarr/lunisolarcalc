package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EclipticTrueObliquityCalculatorTest {

    private static final EclipticMeanObliquityCalculator ECLIPTIC_MEAN_OBLIQUITY_CALCULATOR = new EclipticMeanObliquityCalculator();
    private static final EarthNutuationInObliquityCalculator EARTH_NUTUATION_IN_OBLIQUITY_CALCULATOR = new EarthNutuationInObliquityCalculator();

    private EclipticTrueObliquityCalculator calculator = new EclipticTrueObliquityCalculator();

    @Test
    public void shouldCalculateEpsilon() {
        // Meeus 1998: Example 22.a, p. 148
        // TimelinePoint tx = DynamicalTimelinePoint.ofCalendaricParameters(1987, 4, 10.0);
        double referenceEpsilonZero = Math.toRadians(toSingleDegreesValue(23, 26, 27.407));
        double referenceDeltaEpsilon = Math.toRadians(arcsecondsToDegrees(9.443));
        double actualEpsilonDegrees = Math.toDegrees(calculator.calculate(referenceEpsilonZero, referenceDeltaEpsilon));
        double expectedEpsilonDegrees = toSingleDegreesValue(23, 26, 36.85);
        double delta = decimalAutoDelta(0.001) * Calcs.ARCSECOND_TO_DEGREE;
        assertEquals(expectedEpsilonDegrees, actualEpsilonDegrees, delta);

        // Meeus 1998, Example 25.b, p. 169
        TimelinePoint tx = new DynamicalTimelinePoint(2448908.5);
        double epsilonZero = ECLIPTIC_MEAN_OBLIQUITY_CALCULATOR.calculate(tx);
        referenceDeltaEpsilon = Math.toRadians(arcsecondsToDegrees(-0.308));
        actualEpsilonDegrees = Math.toDegrees(calculator.calculate(epsilonZero, referenceDeltaEpsilon));
        expectedEpsilonDegrees = 23.4401443;
        delta = 0.001 * Calcs.ARCSECOND_TO_DEGREE;
        assertEquals(expectedEpsilonDegrees, actualEpsilonDegrees, delta);

        // Meeus 1998, Example 47.a, pp. 342-343
        tx = DynamicalTimelinePoint.ofCenturialT(-0.077221081451);
        epsilonZero = ECLIPTIC_MEAN_OBLIQUITY_CALCULATOR.calculate(tx);
        double deltaEpsilon = EARTH_NUTUATION_IN_OBLIQUITY_CALCULATOR.calculate(tx, new EarthNutuationElements(tx));
        actualEpsilonDegrees = Math.toDegrees(calculator.calculate(epsilonZero, deltaEpsilon));
        expectedEpsilonDegrees = toSingleDegreesValue(23, 26, 26.29);
        delta = decimalAutoDelta(0.01) * Calcs.ARCSECOND_TO_DEGREE;
        assertEquals(expectedEpsilonDegrees, actualEpsilonDegrees, delta);
    }
}
