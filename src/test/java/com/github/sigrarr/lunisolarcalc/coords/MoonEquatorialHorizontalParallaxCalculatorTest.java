package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.TestUtils;

public class MoonEquatorialHorizontalParallaxCalculatorTest {

    private MoonEquatorialHorizontalParallaxCalculator calculator = new MoonEquatorialHorizontalParallaxCalculator();

    @Test
    public void shouldCalculatePi() {
        double refDistanceKm = 368409.7;
        double actualPi = calculator.calculate(refDistanceKm);
        assertEquals(0.991990, Math.toDegrees(actualPi), TestUtils.decimalAutoDelta(0.000001));
    }
}
