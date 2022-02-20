package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import org.junit.Test;

public class MoonLongitudeCalculatorTest {
    
    private MoonLongitudeCalculator calculator = new MoonLongitudeCalculator();

    @Test
    public void shouldCalculateLongitude() {
        // Meeus 1998, Example 47.a, p. 342-343
        double cT = -0.077221081451;
        double actualLongitude = calculator.calculateCoordinate(cT, new MoonCoordinateElements(cT));
        assertEquals(133.162655, Math.toDegrees(actualLongitude), autoDelta(133.162655));
    }
}
