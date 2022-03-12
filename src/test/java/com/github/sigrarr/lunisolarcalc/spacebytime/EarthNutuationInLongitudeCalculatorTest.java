package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EarthNutuationInLongitudeCalculatorTest {

    private EarthNutuationCalculator calculator = new EarthNutuationInLongitudeCalculator();

    @Test
    public void shouldCalculateDeltaPsi() {
        // Meeus 1998, Example 22.a, p. 148
        double cT = -0.127296372348;
        double actualDeltaPsi = calculator.calculateNutuation(cT, new EarthNutuationElements(cT));
        assertEquals(-3.788, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 25.b, p. 169
        cT = Timeline.julianDayToCenturialT(2448908.5);
        actualDeltaPsi = calculator.calculateNutuation(cT, new EarthNutuationElements(cT));
        assertEquals(15.908, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 27.b, pp. 180-181
        cT = Timeline.julianDayToCenturialT(2437837.38589);
        actualDeltaPsi = calculator.calculateNutuation(cT, new EarthNutuationElements(cT));
        assertEquals(-12.965, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 47.a, pp. 342-343
        cT = -0.077221081451;
        actualDeltaPsi = calculator.calculateNutuation(cT, new EarthNutuationElements(cT));
        assertEquals(16.595, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));
    }
}
