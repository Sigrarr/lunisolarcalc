package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;

public class EarthLatitudeCalculatorTest {

    private EarthLatitudeCalculator calculator = new EarthLatitudeCalculator();

    @Test
    public void shouldCalculateLatitude() {
        // Meeus 1998: Example 25.b, p. 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualLatitude = calculator.calculateCoordinate(tau);
        assertEquals(-0.000179, Math.toDegrees(actualLatitude), autoDelta(-0.000179));
    }
}
