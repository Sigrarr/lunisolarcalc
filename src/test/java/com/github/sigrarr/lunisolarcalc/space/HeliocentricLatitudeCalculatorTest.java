package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;


public class HeliocentricLatitudeCalculatorTest {

    private HeliocentricLatitudeCalculator calculator = new HeliocentricLatitudeCalculator();

    @Test
    public void shouldCalculateLatitude() {
        // Meeus 1998: Example 25.b, p. 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualLatitude = calculator.calculate(tau);
        assertEquals(-0.000179, Math.toDegrees(actualLatitude), autoDelta(-0.000179));
    }
}
