package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

public class MoonEarthDistanceCalculatorTest {

    private MoonEarthDistanceCalculator calculator = new MoonEarthDistanceCalculator();

    @Test
    public void shouldCalculateDistance() {
        // Meeus 1998, Example 47.a, p. 342-343
        TimelinePoint tx = TimelinePoint.ofCenturialT(-0.077221081451);
        double actualDistance = calculator.calculateCoordinate(tx, new MoonCoordinateElements(tx));
        assertEquals(368409.7, actualDistance, decimalAutoDelta(0.1));        
    }
}
