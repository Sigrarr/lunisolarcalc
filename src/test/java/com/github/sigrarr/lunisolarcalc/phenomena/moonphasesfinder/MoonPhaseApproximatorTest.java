package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.GregorianCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;

public class MoonPhaseApproximatorTest {

    static final Map<GregorianCalendarPoint, MoonPhase> DATE_TO_PHASE = new HashMap<GregorianCalendarPoint, MoonPhase>() {{
        // Meeus 1998, Table 49.A, p. 354
        put(new GregorianCalendarPoint(1903,  6, 25), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(2035,  6,  6), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(2053,  6, 16), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(2071,  6, 27), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1955, 12, 14), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1973, 12, 24), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1903, 7, 24), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(2035, 7,  5), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(2053, 7, 15), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(2071, 7, 27), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1956, 1, 13), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1974, 1, 23), MoonPhase.NEW_MOON);

        // Meeus 1998, Example 49.a-b, p. 353
        put(Timeline.julianDayToGregorianCalendar(Calcs.roundToDelta(2443192.94102, 0.5)), MoonPhase.NEW_MOON);
        put(Timeline.julianDayToGregorianCalendar(Calcs.roundToDelta(2467636.88597, 0.5)), MoonPhase.THIRD_QUARTER);

        // https://www.timeanddate.com/moon/phases/spain/madrid?year=1600 UTC -0:14:44
        put(new GregorianCalendarPoint(1600, 1, 8), MoonPhase.THIRD_QUARTER);
        put(new GregorianCalendarPoint(1600, 1, 30), MoonPhase.FULL_MOON);
        put(new GregorianCalendarPoint(1600, 2, 21), MoonPhase.FIRST_QUARTER);
        put(new GregorianCalendarPoint(1600, 3, 15), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1600, 4, 20), MoonPhase.FIRST_QUARTER);
        put(new GregorianCalendarPoint(1600, 5, 28), MoonPhase.FULL_MOON);
        put(new GregorianCalendarPoint(1600, 7, 3), MoonPhase.THIRD_QUARTER);
        put(new GregorianCalendarPoint(1600, 7, 26), MoonPhase.FULL_MOON);
        put(new GregorianCalendarPoint(1600, 8, 16), MoonPhase.FIRST_QUARTER);
        put(new GregorianCalendarPoint(1600, 9, 7), MoonPhase.NEW_MOON);
        put(new GregorianCalendarPoint(1600, 10, 15), MoonPhase.FIRST_QUARTER);
        put(new GregorianCalendarPoint(1600, 11, 20), MoonPhase.FULL_MOON);
        put(new GregorianCalendarPoint(1600, 12, 27), MoonPhase.THIRD_QUARTER);
    }};

    private MoonPhaseApproximator approximator = new MoonPhaseApproximator();

    @Test
    public void shouldApproximateBeNotFurtherAwayFromResultDateThanArgument() {
        // with the dataset's precision the offset of abs <= 1.0 wouldn't make much sense
        double[] argumentOffsetsFromCenter = new double[] {-12.5, -9.0, -25.0/24.0, 25.0/24.0, 7.0, 10.0};

        DATE_TO_PHASE.entrySet().stream().forEach((e) -> {
            TimelinePoint center = TimelinePoint.ofCalendarPoint(e.getKey());
            for (double offset : argumentOffsetsFromCenter) {
                TimelinePoint argument = new TimelinePoint(center.julianDay + offset);
                double approximateJde = approximator.approximateJulianEphemerisDayAround(argument, e.getValue());
                double diff = Math.abs(Time.shiftDaysToTimeType(approximateJde, TimeType.UNIVERSAL, e.getKey().y) - center.julianDay);
                assertTrue(diff <= Math.abs(offset));
            }
        });
    }

    @Test
    public void souldApproximateBeCloseToArgumentAcrossWholeTimeline() {
        double startJde = Timeline.JULIAN_PERIOD_START_JD + (0.51 * MeanMotionApproximate.SYNODIC_MONTH.lengthDays);
        double endJde = Timeline.JULIAN_PERIOD_END_JD - (0.51 * MeanMotionApproximate.SYNODIC_MONTH.lengthDays);
        double delta = MeanMotionApproximate.SYNODIC_MONTH.lengthDays * 0.5 + Time.timeToDays(0, 1, 0);
        double step = MeanMotionApproximate.SYNODIC_MONTH.lengthDays * 0.75;

        for (double jde = startJde; jde < endJde; jde += step) {
            TimelinePoint argument = TimelinePoint.ofJulianEphemerisDay(jde);
            for (MoonPhase phase : MoonPhase.values()) {
                double approximate = approximator.approximateJulianEphemerisDayAround(argument, phase);

                assertTrue(
                    Math.abs(approximate - jde) < delta,
                    "Too far away ~" + Calcs.roundToDelta(Math.abs(approximate - jde), 0.01)
                    + ": ARG. " + argument.toGregorianCalendarPoint().formatYMDHMin()
                    + "; APPROX. " + Timeline.julianDayToGregorianCalendar(approximate).formatYMDHMin() + " [TD]"
                );
            }
        }
    }

    @Test
    public void shouldApproximateBeCloseToArgumentInSpecificCases() {
        // The case when the Meeus' approximator fails and returns a 2000s date...
        TimelinePoint tx = TimelinePoint.ofCalendarPoint(new GregorianCalendarPoint(0, 1, 1));
        double actualApproximateJde = approximator.approximateJulianEphemerisDayAround(tx, MoonPhase.FULL_MOON);
        if (Math.abs(actualApproximateJde - tx.julianDay) > MeanMotionApproximate.SYNODIC_MONTH.lengthDays * 0.5 + Time.timeToDays(0, 1, 0)) {
            fail(
                "Too far away: approximate(" + tx.toGregorianCalendarPoint().formatYMDHMin()
                + ") = " + TimelinePoint.ofJulianEphemerisDay(actualApproximateJde).toGregorianCalendarPoint().formatYMDHMin()
            );
        }
    }
}
