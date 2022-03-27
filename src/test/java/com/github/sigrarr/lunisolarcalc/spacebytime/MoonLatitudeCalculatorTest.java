package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

public class MoonLatitudeCalculatorTest {
    
    private MoonLatitudeCalculator calculator = new MoonLatitudeCalculator();

    @Test
    public void shouldCalculateLatitude() {
        // Meeus 1998, Example 47.a, p. 342-343
        TimelinePoint tx = TimelinePoint.ofCenturialT(-0.077221081451);
        double actualLatitude = calculator.calculateCoordinate(tx, new MoonCoordinateElements(tx));
        assertEquals(-3.229126, Math.toDegrees(actualLatitude), decimalAutoDelta(-3.229126));
    }
}
