package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.toArcseconds;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;

public class EarthNutuationInObliquityCalculatorTest {

    private EarthNutuationInObliquityCalculator calculator = new EarthNutuationInObliquityCalculator();

    @Test
    public void shouldCalculateDeltaEpsilon() {
        // Meeus 1998: Example 22.a, p. 148
        TimelinePoint tx = DynamicalTimelinePoint.ofCalendaricParameters(1987, 4, 10.0);
        double actualDeltaEpsilon = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(9.443, toArcseconds(Math.toDegrees(actualDeltaEpsilon)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 25.b, p. 169
        tx = new DynamicalTimelinePoint(2448908.5);
        actualDeltaEpsilon = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(-0.308, toArcseconds(Math.toDegrees(actualDeltaEpsilon)), decimalAutoDelta(0.001));
    }
}
