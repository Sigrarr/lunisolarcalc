package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.TimeScale;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EarthNutuationInLongitudeCalculatorTest {

    private EarthNutuationInLongitudeCalculator calculator = new EarthNutuationInLongitudeCalculator();

    @Test
    public void shouldCalculateDeltaPsi() {
        // Meeus 1998, Example 22.a, p. 148
        TimelinePoint tx = TimelinePoint.ofCenturialT(-0.127296372348, TimeScale.DYNAMICAL);
        double actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(-3.788, Calcs.Angle.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 25.b, p. 169
        tx = TimelinePoint.ofJulianEphemerisDay(2448908.5);
        actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(15.908, Calcs.Angle.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 27.b, pp. 180-181
        tx = TimelinePoint.ofJulianEphemerisDay(2437837.38589);
        actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(-12.965, Calcs.Angle.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 47.a, pp. 342-343
        tx = TimelinePoint.ofCenturialT(-0.077221081451, TimeScale.DYNAMICAL);
        actualDeltaPsi = calculator.calculate(tx, new EarthNutuationElements(tx));
        assertEquals(16.595, Calcs.Angle.toArcseconds(Math.toDegrees(actualDeltaPsi)), decimalAutoDelta(0.001));
    }
}
