package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.GregorianCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;

public class MeanMoonPhaseApproximatorTest {

    private MeanMoonPhaseApproximator approximator = new MeanMoonPhaseApproximator();

    @Test
    public void shouldApproximateJulianEphemerisDayAround() {
        // Meeus 1998, Example 49.a, p. 353
        GregorianCalendarPoint gcp = new GregorianCalendarPoint(1977, 2, 17, 10, 48, 0);
        assertEquals(1977.13, gcp.toYearWithFraction(), Calcs.EPSILON);
        TimelinePoint tx = TimelinePoint.ofCalendarPoint(gcp);
        assertEquals(2443192.94102, approximator.approximateJulianEphemerisDayAround(tx, MoonPhase.NEW_MOON), decimalAutoDelta(2443192.94102));

        // Meeus 1998, Example 49.b, p. 353
        tx = TimelinePoint.ofCalendarPoint(new GregorianCalendarPoint(2044, 1, 1.0));
        assertEquals(2467636.88597, approximator.approximateJulianEphemerisDayAround(tx, MoonPhase.THIRD_QUARTER), decimalAutoDelta(2467636.88597));
    }

    @Test
    public void shouldApproximateJulianEphemerisDayInDirection() {
        double startJde = Timeline.calendarToJulianDay(new GregorianCalendarPoint(-700, 1, 1.0));
        double endJde = Timeline.calendarToJulianDay(new GregorianCalendarPoint(2200, 12, 31.999));

        for (double jde = startJde; jde <= endJde; jde += 9.0) {
            TimelinePoint tx = new TimelinePoint(jde, TimeType.DYNAMICAL);

            for (MoonPhase phase : MoonPhase.values()) {
                double approximateJde = approximator.approximateJulianEphemerisDayAround(tx, phase);
                double approximateJdeForward = approximator.approximateJulianEphemerisDayForward(tx, phase);
                double approximateJdeBackward = approximator.approximateJulianEphemerisDayBackward(tx, phase);

                if (Math.abs(approximateJde - jde) < approximator.getDirectedApproximationToleranceDays()) {
                    assertEquals(approximateJdeForward, approximateJde, 0.01);
                    assertEquals(approximateJdeBackward, approximateJde, 0.01);
                } else if (approximateJde > jde) {
                    assertEquals(approximateJdeForward, approximateJde, 0.01);
                    assertEquals(MeanMotionApproximate.SYNODIC_MONTH.lengthDays, approximateJde - approximateJdeBackward, 1.5);
                } else if (approximateJde < jde) {
                    assertEquals(approximateJdeBackward, approximateJde, 0.01);
                    assertEquals(MeanMotionApproximate.SYNODIC_MONTH.lengthDays, approximateJdeForward - approximateJde, 1.5);
                }
            }
        }
    }
}
