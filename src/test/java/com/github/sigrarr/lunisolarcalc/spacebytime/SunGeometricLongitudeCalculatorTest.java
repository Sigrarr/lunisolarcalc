package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;
import static com.github.sigrarr.lunisolarcalc.util.MeanMotionApproximate.TROPICAL_YEAR;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunGeometricLongitudeCalculatorTest {

    private SunGeometricLongitudeCalculator calculator = new SunGeometricLongitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLongitude() {
        // Meeus 1998, Example 25.a-b, p. 165, 169
        double heliocentricLongitude = Math.toRadians(19.907372);
        double actualGeometricLongitude = calculator.calculateGeometricLongitude(heliocentricLongitude);

        // Meeus 1998, Example 25.b, p. 169
        assertEquals(199.907347, Math.toDegrees(actualGeometricLongitude), decimalAutoDelta(199.907347));

        // Meeus 1998, Example 25.a, p. 165
        double trueVSOP87GeometricLongitudeDegrees = Calcs.toSingleDegreesValue(199, 54, 26.18);
        assertEquals(trueVSOP87GeometricLongitudeDegrees, Math.toDegrees(actualGeometricLongitude), TROPICAL_YEAR.degreesPerTimeMiliseconds(6550));
    }
}
