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
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualGeometricLatitudeArcseconds = Calcs.toArcseconds(Math.toDegrees(calculator.calculateGeometricLatitude(tau)));
        assertEquals(0.62, actualGeometricLatitudeArcseconds, autoDelta(0.62));
    }
}
