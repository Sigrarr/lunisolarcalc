package com.github.sigrarr.lunisolarcalc.coords.global;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunParallaxSineCalculatorTest {

    private SunParallaxSineCalculator calculator = new SunParallaxSineCalculator();

    @Test
    public void shouldCalculatePiSine() {
        // Meeus 1998, Example 40.a, p. 280
        // Example is about Mars, but it's the same formula.
        double refDistanceAu = 0.37276;
        double refParallax = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(23.592));
        double actualSine = calculator.calculate(refDistanceAu);

        double delta = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(0.0005));
        assertEquals(refParallax, Math.asin(actualSine), delta);
    }
}
