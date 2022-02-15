package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion.degreesPerTimeMiliseconds;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunGeometricLongitudeCalculatorTest {

    private SunGeometricLongitudeCalculator calculator = new SunGeometricLongitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLongitude() {
        // Meeus 1998, Example 25.a-b, p. 165, 169
        double heliocentricLongitude = Math.toRadians(19.907372);
        double actualGeometricLongitude = calculator.calculateGeometricLongitude(heliocentricLongitude);

        // Meeus 1998, Example 25.b, p. 169
        assertEquals(199.907347, Math.toDegrees(actualGeometricLongitude), autoDelta(199.907347));

        // Meeus 1998, Example 25.a, p. 165
        double trueVSOP87GeometricLongitudeDegrees = Calcs.toSingleDegreesValue(199, 54, 26.18);
        assertEquals(trueVSOP87GeometricLongitudeDegrees, Math.toDegrees(actualGeometricLongitude), degreesPerTimeMiliseconds(6550));
    }
}
