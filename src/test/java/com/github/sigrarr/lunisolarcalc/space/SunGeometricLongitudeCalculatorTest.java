package com.github.sigrarr.lunisolarcalc.space;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Meeus 1998, Example 25.a-b, p. 165, 169.
 * The difference of 0.275 arcsec corresponds to less than 7 sec of time in Sun's mean motion. 
 */
public class SunGeometricLongitudeCalculatorTest {

    private final static double TAU = Timeline.julianDayToMillenialTau(2448908.5);
    private final static double TRUE_VSOP87_GEOMETRIC_LONGITUDE_DEGREES = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(26.18);
    private final static double EXAMPLE_GEOMETRIC_LONGITUDE_DEGREES = 199.907347;

    private SunGeometricLongitudeCalculator calculator = new SunGeometricLongitudeCalculator();

    @Test
    public void shouldCalculateFK5GeometricLongitude() {
        double actualGeometricLongitudeDegrees = Math.toDegrees(calculator.calculateGeometricLongitude(TAU));
        assertEquals(EXAMPLE_GEOMETRIC_LONGITUDE_DEGREES, actualGeometricLongitudeDegrees, autoDelta(EXAMPLE_GEOMETRIC_LONGITUDE_DEGREES));
        assertEquals(TRUE_VSOP87_GEOMETRIC_LONGITUDE_DEGREES, actualGeometricLongitudeDegrees, Calcs.arcsecondsToDegrees(0.275));
    }
}
