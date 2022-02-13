package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion.arcsecondsPerTimeMiliseconds;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EarthNutuationInLongitudeCalculatorTest {

    private EarthNutuationCalculator deltaPsiCalculator = new EarthNutuationInLongitudeCalculator();

    @Test
    public void shouldCalculateDeltaPsi() {
        // Meeus 1998, Example 22.a, p. 148
        double actualDeltaPsi = deltaPsiCalculator.calculateNutuation(-0.127296372348);
        assertEquals(-3.788, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), arcsecondsPerTimeMiliseconds(40));

        // Meeus 1998, Example 25.b, p. 169
        actualDeltaPsi = deltaPsiCalculator.calculateNutuation(Timeline.julianDayToCenturialT(2448908.5));
        assertEquals(15.908, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), arcsecondsPerTimeMiliseconds(230));

        // Meeus 1998, Example 27.b, pp. 180-181
        actualDeltaPsi = deltaPsiCalculator.calculateNutuation(Timeline.julianDayToCenturialT(2437837.38589));
        assertEquals(-12.965, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), arcsecondsPerTimeMiliseconds(230));

        // Meeus 1998, Example 47.a, pp. 342-343
        actualDeltaPsi = deltaPsiCalculator.calculateNutuation(-0.077221081451);
        assertEquals(16.595, Calcs.toArcseconds(Math.toDegrees(actualDeltaPsi)), arcsecondsPerTimeMiliseconds(115));
    }
}
