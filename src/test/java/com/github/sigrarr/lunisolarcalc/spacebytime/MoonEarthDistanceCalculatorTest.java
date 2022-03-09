package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import org.junit.jupiter.api.Test;

public class MoonEarthDistanceCalculatorTest {

    private MoonEarthDistanceCalculator calculator = new MoonEarthDistanceCalculator();

    @Test
    public void shouldCalculateDistance() {
        // Meeus 1998, Example 47.a, p. 342-343
        double cT = -0.077221081451;
        double actualDistance = calculator.calculateCoordinate(cT, new MoonCoordinateElements(cT));
        assertEquals(368409.7, actualDistance, autoDelta(0.1));        
    }
}
