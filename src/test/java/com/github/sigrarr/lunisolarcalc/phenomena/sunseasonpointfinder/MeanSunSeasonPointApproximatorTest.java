package com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.phenomena.SunSeasonPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class MeanSunSeasonPointApproximatorTest {

    private MeanSunSeasonPointApproximator approximatior = new MeanSunSeasonPointApproximator();

    @Test
    public void shouldApproximateSunSeasonPointJde() {
        // Meeus 1998, Example 27.a, p. 180
        double actualApproximationJde = approximatior.approximateJulianEphemerisDay(1962, SunSeasonPoint.JUNE_SOLSTICE);
        assertEquals(2437837.38589, actualApproximationJde, Calcs.decimalAutoDelta(0.00001));
    }
}
