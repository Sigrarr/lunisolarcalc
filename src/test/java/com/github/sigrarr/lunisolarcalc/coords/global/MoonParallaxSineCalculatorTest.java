package com.github.sigrarr.lunisolarcalc.coords.global;

import static com.github.sigrarr.lunisolarcalc.testing.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MoonParallaxSineCalculatorTest {

    private MoonParallaxSineCalculator calculator = new MoonParallaxSineCalculator();

    @Test
    public void shouldCalculatePiSine() {
        // Meeus 1998, Example 47.a, pp. 342-343
        double refDistanceKm = 368409.7;
        double actualSine = calculator.calculate(refDistanceKm);
        assertEquals(0.991990, Math.toDegrees(Math.asin(actualSine)), decimalAutoDelta(0.000001));
    }
}
