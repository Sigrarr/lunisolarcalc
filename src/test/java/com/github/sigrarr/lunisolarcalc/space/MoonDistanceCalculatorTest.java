package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;

import org.junit.Test;

public class MoonDistanceCalculatorTest {

    private MoonDistanceCalculator calculator = new MoonDistanceCalculator();

    @Test
    public void shouldCalculateDistance() {
        // Meeus 1998, Example 47.a, p. 342-343
        double cT = -0.077221081451;
        double actualDistance = calculator.calculateCoordinate(cT, new MoonCoordinateElements(cT));
        assertEquals(368409.7, actualDistance, autoDelta(0.1));        
    }
}
