package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.time.*;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

public class MoonEarthDistanceCalculatorTest {

    private MoonEarthDistanceCalculator calculator = new MoonEarthDistanceCalculator();

    @Test
    public void shouldCalculateDistance() {
        // Meeus 1998, Example 47.a, p. 342-343
        TimelinePoint tx = DynamicalTimelinePoint.ofCenturialT(-0.077221081451);
        double actualDistance = calculator.calculate(tx, new MoonCoordinateElements(tx));
        assertEquals(368409.7, actualDistance, decimalAutoDelta(0.1));
    }
}
