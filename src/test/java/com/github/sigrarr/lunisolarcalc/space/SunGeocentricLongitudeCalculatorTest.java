package com.github.sigrarr.lunisolarcalc.space;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;

public class SunGeocentricLongitudeCalculatorTest {

    private SunGeocentricLongitudeCalculator calculator = new SunGeocentricLongitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLongitude() {
        // Meeus 1998, Example 25.b, p. 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualGeometricLongitude = calculator.calculateGeometricLongitude(tau);
        assertEquals(199.907347, Math.toDegrees(actualGeometricLongitude), autoDelta(199.907347));
    }
}
