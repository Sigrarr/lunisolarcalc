package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunApparentLongitudeCalculatorTest {

    private SunApparentLongitudeCalculator calculator = new SunApparentLongitudeCalculator();

    @Test
    public void shouldCalculateLambda() {
        // Meeus 1998, Example 25.a-b, pp. 165, 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualLambdaDegrees = Math.toDegrees(calculator.calculateApparentLongitude(tau));

        // Meeus 1998, Example 25.b, p. 169.
        double exampleLambdaDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(21.818);
        assertEquals(exampleLambdaDegrees, actualLambdaDegrees, Calcs.arcsecondsToDegrees(0.02));

        // Meeus 1998, Example 25.a, pp. 165
        double trueVSOP87LambdaDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(21.56);
        assertEquals(trueVSOP87LambdaDegrees, actualLambdaDegrees, Calcs.arcsecondsToDegrees(0.275));

        // Meeus 1998, Example 27.b, pp. 180-181
        tau = Timeline.julianDayToMillenialTau(2437837.38589);
        actualLambdaDegrees = Math.toDegrees(calculator.calculateApparentLongitude(tau));
        assertEquals(89.994045, actualLambdaDegrees, Calcs.arcsecondsToDegrees(0.02));
    }
}
