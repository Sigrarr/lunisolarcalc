package com.github.sigrarr.lunisolarcalc.space;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunGeometricLongitudeCalculatorTest {

    private SunGeometricLongitudeCalculator calculator = new SunGeometricLongitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLongitude() {
        // Meeus 1998, Example 25.a-b, p. 165, 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualGeometricLongitudeDegrees = Math.toDegrees(calculator.calculateGeometricLongitude(tau));

        // Meeus 1998, Example 25.b, p. 169
        assertEquals(199.907347, actualGeometricLongitudeDegrees, autoDelta(199.907347));

        // Meeus 1998, Example 25.a, p. 165
        double trueVSOP87GeometricLongitudeDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(26.18);
        assertEquals(trueVSOP87GeometricLongitudeDegrees, actualGeometricLongitudeDegrees, Calcs.arcsecondsToDegrees(0.275));

        // Meeus 1998, Example 27.b, pp. 180-181
        tau = Timeline.julianDayToMillenialTau(2437837.38589);
        actualGeometricLongitudeDegrees = Math.toDegrees(calculator.calculateGeometricLongitude(tau));
        double exampleGeometricLongitudeDegrees = 270.003272 - 180.0 + Calcs.arcsecondsToDegrees(0.09033);
        assertEquals(exampleGeometricLongitudeDegrees, actualGeometricLongitudeDegrees, Calcs.arcsecondsToDegrees(0.2));
    }
}
