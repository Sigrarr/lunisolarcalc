package com.github.sigrarr.lunisolarcalc.spacebytime;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunLatitudeCalculatorTest {

    private SunLatitudeCalculator calculator = new SunLatitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLatitude() {
        // Meeus 1998, Example 25b, p. 169.
        TimelinePoint tx = new TimelinePoint(2448908.5);
        double heliocentricLatitude = -0.00000312;
        double heliocentricLongitude = Math.toRadians(19.907372);
        double actualGeometricLatitude = calculator.calculateLatitude(tx, heliocentricLatitude, heliocentricLongitude);
        assertEquals(0.62, Calcs.toArcseconds(Math.toDegrees(actualGeometricLatitude)), decimalAutoDelta(0.62));
    }
}
