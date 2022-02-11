package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Meeus 1998, Example 25.a-b, p. 165, 169.
 * The difference of 0.275 arcsec corresponds to less than 7 sec of time in Sun's mean motion. 
 */
public class SunApparentLongitudeCalculatorTest {

    private final static double TAU = Timeline.julianDayToMillenialTau(2448908.5);
    private final static double TRUE_VSOP87_LAMBDA_DEGREES = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(21.56);
    private final static double EXAMPLE_LAMBDA_DEGREES = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(21.818);

    private SunApparentLongitudeCalculator calculator = new SunApparentLongitudeCalculator();

    @Test
    public void shouldCalculateLambda() {
        double actualLambdaDegrees = Math.toDegrees(calculator.calculateApparentLongitude(TAU));
        assertEquals(EXAMPLE_LAMBDA_DEGREES, actualLambdaDegrees, Calcs.arcsecondsToDegrees(0.02));
        assertEquals(TRUE_VSOP87_LAMBDA_DEGREES, actualLambdaDegrees, Calcs.arcsecondsToDegrees(0.275));
    }
}
