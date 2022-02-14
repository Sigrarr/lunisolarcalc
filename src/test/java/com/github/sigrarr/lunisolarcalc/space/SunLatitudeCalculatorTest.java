package com.github.sigrarr.lunisolarcalc.space;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunLatitudeCalculatorTest {

    private SunLatitudeCalculator calculator = new SunLatitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLatitude() {
        // Meeus 1998, Example 25b, p. 169.
        double cT = Timeline.julianDayToCenturialT(2448908.5);
        double heliocentricLatitude = -0.00000312;
        double heliocentricLongitude = Math.toRadians(19.907372);
        double actualGeometricLatitude = calculator.calculateLatitude(cT, heliocentricLatitude, heliocentricLongitude);
        assertEquals(0.62, Calcs.toArcseconds(Math.toDegrees(actualGeometricLatitude)), autoDelta(0.62));
    }
}
