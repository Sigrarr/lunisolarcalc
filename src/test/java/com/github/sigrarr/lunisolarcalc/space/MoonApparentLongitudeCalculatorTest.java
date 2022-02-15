package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.MoonMotion.degreesPerTimeMiliseconds;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.Test;

public class MoonApparentLongitudeCalculatorTest {
    
    private MoonApparentLongitudeCalculator calculator = new MoonApparentLongitudeCalculator();

    @Test
    public void shouldCalculateApparentLongitude() {
        // Meeus 1998, Example 47.a, p. 342-343
        double baseLongitude = Math.toRadians(133.162655);
        double deltaPsi = Math.toRadians(Calcs.arcsecondsToDegrees(16.595));
        double actualLongitudeDegrees = Math.toDegrees(calculator.calculateApparentLongitude(baseLongitude, deltaPsi));
        assertEquals(133.167265, actualLongitudeDegrees, autoDelta(0.000001));

        double trueELP2k82ApparentLongitudeDegrees = Calcs.toSingleDegreesValue(133, 10, 0.0);
        assertEquals(trueELP2k82ApparentLongitudeDegrees, actualLongitudeDegrees, degreesPerTimeMiliseconds(4000));
    }
}
