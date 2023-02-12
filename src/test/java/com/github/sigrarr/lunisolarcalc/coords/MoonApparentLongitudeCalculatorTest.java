package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class MoonApparentLongitudeCalculatorTest {

    private MoonApparentLongitudeCalculator calculator = new MoonApparentLongitudeCalculator();

    @Test
    public void shouldCalculateApparentLongitude() {
        // Meeus 1998, Example 47.a, p. 342-343
        double baseLongitude = Math.toRadians(133.162655);
        double deltaPsi = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(16.595));
        double actualLongitudeDegrees = Math.toDegrees(calculator.calculate(baseLongitude, deltaPsi));
        assertEquals(133.167265, actualLongitudeDegrees, decimalAutoDelta(0.000001));

        double trueELP2k82ApparentLongitudeDegrees = Calcs.Angle.toSingleDegreesValue(133, 10, 0.0);
        assertEquals(trueELP2k82ApparentLongitudeDegrees, actualLongitudeDegrees, Calcs.Angle.arcsecondsToDegrees(30));
    }
}
