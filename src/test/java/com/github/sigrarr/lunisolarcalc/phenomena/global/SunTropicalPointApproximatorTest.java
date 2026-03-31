package com.github.sigrarr.lunisolarcalc.phenomena.global;

import static com.github.sigrarr.lunisolarcalc.testing.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class SunTropicalPointApproximatorTest {

    private SunTropicalPointApproximator approximatior = new SunTropicalPointApproximator();

    @Test
    public void shouldApproximateSunTropicalPointJde() {
        // Meeus 1998, Example 27.a, p. 180
        double actualApproximationJde = approximatior.approximateJulianEphemerisDay(1962, SunTropicalPoint.JUNE_SOLSTICE);
        assertEquals(2437837.38589, actualApproximationJde, decimalAutoDelta(0.00001));
    }
}
