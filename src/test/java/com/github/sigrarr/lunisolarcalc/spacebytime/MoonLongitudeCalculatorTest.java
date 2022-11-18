package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.time.*;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

public class MoonLongitudeCalculatorTest {

    private MoonLongitudeCalculator calculator = new MoonLongitudeCalculator();

    @Test
    public void shouldCalculateLongitude() {
        // Meeus 1998, Example 47.a, p. 342-343
        TimelinePoint tx = TimelinePoint.ofCenturialT(-0.077221081451, TimeType.DYNAMICAL);
        double actualLongitude = calculator.calculateCoordinate(tx, new MoonCoordinateElements(tx));
        assertEquals(133.162655, Math.toDegrees(actualLongitude), decimalAutoDelta(133.162655));
    }
}
