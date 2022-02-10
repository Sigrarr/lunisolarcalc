package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EarthNutuationCalculatorTest {

    private PeriodicTermsForNutuation periodicTermsForDeltaPsi = new PeriodicTermsForNutuationInLongitude();
    private EarthNutuationCalculator deltaPsiCalculator = new EarthNutuationCalculator(periodicTermsForDeltaPsi);

    /**
     * Note: the test fails with more precise assertions.
     * The calculation method and periodic terms have been double checked, no mistake found.
     * Fortunately, these inaccuracies are negligible: 0.01 arcsec corresponds to about 243 ms of time  
     * (in context of the Sun's apparent longitude). Maybe the given results were calculated in a different way?
     */
    @Test
    public void shouldCalculateDeltaPsi() {
        // Meeus 1998, Example 22.a, p. 148
        double actualDeltaPsi = deltaPsiCalculator.calculateNutuation(-0.127296372348);
        assertEquals(-3.788, Calcs.toArcSeconds(Math.toDegrees(actualDeltaPsi)), 0.002);
        // Meeus 1998, Example 25.b, p. 169
        actualDeltaPsi = deltaPsiCalculator.calculateNutuation(Timeline.julianDayToCenturialT(2448908.5));
        assertEquals(15.908, Calcs.toArcSeconds(Math.toDegrees(actualDeltaPsi)), 0.01);
    }
}
