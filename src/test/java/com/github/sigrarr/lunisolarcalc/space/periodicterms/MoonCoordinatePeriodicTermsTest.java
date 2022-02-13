package com.github.sigrarr.lunisolarcalc.space.periodicterms;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import org.junit.Test;

public class MoonCoordinatePeriodicTermsTest {
    
    @Test
    public void shouldCalculateE() {
        // Meeus 1998, Example 47.a, p. 342
        double actualE = MoonCoordinatePeriodicTerms.calculateEarthOrbitEccentricityFactor(-0.077221081451);
        assertEquals(1.000194, actualE, autoDelta(1.000194));
    }
}
