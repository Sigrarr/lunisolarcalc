package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator.MoonCoordinateElements;

import org.junit.Test;

public class MoonLatitudeCalculatorTest {
    
    private MoonLatitudeCalculator calculator = new MoonLatitudeCalculator();

    @Test
    public void shouldCalculateLatitude() {
        // Meeus 1998, Example 47.a, p. 342-343
        double cT = -0.077221081451;
        double actualLatitude = calculator.calculateCoordinate(cT, new MoonCoordinateElements(cT));
        assertEquals(-3.229126, Math.toDegrees(actualLatitude), autoDelta(-3.229126));
    }
}
