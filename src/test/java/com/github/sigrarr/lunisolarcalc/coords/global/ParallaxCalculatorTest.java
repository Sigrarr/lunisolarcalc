package com.github.sigrarr.lunisolarcalc.coords.global;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

public class ParallaxCalculatorTest {

    ParallaxCalculator calculator = new ParallaxCalculator(null);

    @Test
    public void shouldApplyAsin() {
        for (int i = 0; i < 10; i++) {
            double sine = ThreadLocalRandom.current().nextDouble() * 2 - 1;
            assertEquals(Math.asin(sine), calculator.calculate(sine));
        }
    }
}
