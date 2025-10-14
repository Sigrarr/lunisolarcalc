package com.github.sigrarr.lunisolarcalc.coords.global;

import static com.github.sigrarr.lunisolarcalc.testing.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;

public class EarthLatitudeCalculatorTest {

    private EarthLatitudeCalculator calculator = new EarthLatitudeCalculator();

    @Test
    public void shouldCalculateLatitude() {
        // Meeus 1998: Example 25.b, p. 169
        TimelinePoint tx = new DynamicalTimelinePoint(2448908.5);
        double actualLatitude = calculator.calculate(tx);
        assertEquals(-0.000179, Math.toDegrees(actualLatitude), decimalAutoDelta(-0.000179));
    }
}
