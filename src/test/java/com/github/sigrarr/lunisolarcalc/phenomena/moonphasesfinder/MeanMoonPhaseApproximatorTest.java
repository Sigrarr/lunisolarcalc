package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;

public class MeanMoonPhaseApproximatorTest {

    private MeanMoonPhaseApproximator approximator = new MeanMoonPhaseApproximator();

    @Test
    public void shouldApproximateJulianEphemerisDayAround() {
        // Meeus 1998, Example 49.a, p. 353
        RomanCalendarPoint rcp = new RomanCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, rcp.toYearWithFraction(), Calcs.EPSILON);
        assertEquals(2443192.94102, approximator.approximateJulianEphemerisDayAround(rcp, MoonPhase.NEW_MOON), decimalAutoDelta(2443192.94102));

        // Meeus 1998, Example 49.b, p. 353
        rcp = new RomanCalendarPoint(2044, 1, 1.0);
        assertEquals(2467636.88597, approximator.approximateJulianEphemerisDayAround(rcp, MoonPhase.THIRD_QUARTER), decimalAutoDelta(2467636.88597));
    }

    @Test
    public void shouldApproximateJulianEphemerisDayInDirection() {
        double startJde = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(-700, 1, 1.0));
        double endJde = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(2000, 12, 30.999));

        RomanCalendarPoint roman = new RomanCalendarPoint();
        for (double jde = startJde; jde <= endJde; jde += 9.0) {
            Timeline.setByJulianDay(roman, jde);

            for (MoonPhase phase : MoonPhase.values()) {
                double approximateJde = approximator.approximateJulianEphemerisDayAround(roman, phase);
                double approximateJdeForward = approximator.approximateJulianEphemerisDayForward(roman, phase);
                double approximateJdeBackward = approximator.approximateJulianEphemerisDayBackward(roman, phase);

                if (Math.abs(approximateJde - jde) < Time.timeToDays(0, 1, 0)) {
                    assertEquals(approximateJdeForward, approximateJde, 0.01);
                    assertEquals(approximateJdeBackward, approximateJde, 0.01);
                } else if (approximateJde > jde) {
                    assertEquals(approximateJdeForward, approximateJde, 0.01);
                    assertEquals(MeanValueApproximations.LUNATION_MEAN_DAYS, approximateJde - approximateJdeBackward, 1.5);
                } else if (approximateJde < jde) {
                    assertEquals(approximateJdeBackward, approximateJde, 0.01);
                    assertEquals(MeanValueApproximations.LUNATION_MEAN_DAYS, approximateJdeForward - approximateJde, 1.5);
                }
            }
        }        
    }
}
