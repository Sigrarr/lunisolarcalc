package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.Test;

public class MeanMoonPhaseApproximatorTest {

    private MeanMoonPhaseApproximator approximator = new MeanMoonPhaseApproximator();

    @Test
    public void shouldApproximateJulianEphemerisDay() {
        // Meeus 1998, Example 49.a, p. 353
        RomanCalendarPoint rcp = new RomanCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, rcp.toYearWithFraction(), Calcs.EPSILON);
        assertEquals(2443192.94102, approximator.approximateJulianEphemerisDay(rcp, MoonPhase.NEW_MOON), autoDelta(2443192.94102));
    }

    @Test
    public void shouldConvertKToCenturialT() {
        // Meeus 1998, Example 49.a, p. 353
        RomanCalendarPoint rcp = new RomanCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, rcp.toYearWithFraction(), Calcs.EPSILON);
        double k = -283.0;
        assertEquals(-0.22881, approximator.kToCenturialT(k), autoDelta(0.22881));
    }
}
