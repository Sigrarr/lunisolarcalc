package com.github.sigrarr.lunisolarcalc.phenomena.global;

import static com.github.sigrarr.lunisolarcalc.tutil.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class SunSeasonPointApproximatorTest {

    private SunSeasonPointApproximator approximatior = new SunSeasonPointApproximator();

    @Test
    public void shouldApproximateSunSeasonPointJde() {
        // Meeus 1998, Example 27.a, p. 180
        double actualApproximationJde = approximatior.approximateJulianEphemerisDay(1962, SunSeasonPoint.JUNE_SOLSTICE);
        assertEquals(2437837.38589, actualApproximationJde, decimalAutoDelta(0.00001));
    }
}
