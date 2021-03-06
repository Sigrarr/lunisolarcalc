package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

public class EarthLatitudeCalculatorTest {

    private EarthLatitudeCalculator calculator = new EarthLatitudeCalculator();

    @Test
    public void shouldCalculateLatitude() {
        // Meeus 1998: Example 25.b, p. 169
        TimelinePoint tx = new TimelinePoint(2448908.5);
        double actualLatitude = calculator.calculateCoordinate(tx);
        assertEquals(-0.000179, Math.toDegrees(actualLatitude), decimalAutoDelta(-0.000179));
    }
}
