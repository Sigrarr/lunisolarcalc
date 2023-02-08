package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.toArcseconds;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;

public class EarthNutuationInLongitudeCalculatorTest {

    private EarthNutuationInLongitudeCalculator calculator = new EarthNutuationInLongitudeCalculator();

    @Test
    public void shouldCalculateDeltaPsi() {
        // Meeus 1998, Example 22.a, p. 148
        TimelinePoint tx = DynamicalTimelinePoint.ofCenturialT(-0.127296372348);
        double actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(-3.788, toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 25.b, p. 169
        tx = new DynamicalTimelinePoint(2448908.5);
        actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(15.908, toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 27.b, pp. 180-181
        tx = new DynamicalTimelinePoint(2437837.38589);
        actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(-12.965, toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 47.a, pp. 342-343
        tx = DynamicalTimelinePoint.ofCenturialT(-0.077221081451);
        actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(16.595, toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));
    }
}
