package com.github.sigrarr.lunisolarcalc.phenomena;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

import org.junit.jupiter.api.Test;

/**
 * + {@link com.github.sigrarr.lunisolarcalc.phenomena.MoonPhaseFinderTest#shouldOutputOfDefaultApproximatorBeCloseToResult}
 */
public class MoonPhaseApproximatorTest {

    static final Map<CalendarPoint, MoonPhase> DATE_TO_PHASE = new HashMap<CalendarPoint, MoonPhase>() {{
        // Meeus 1998, Table 49.A, p. 354
        put(new CalendarPoint(1903,  6, 25), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2035,  6,  6), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2053,  6, 16), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2071,  6, 27), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1955, 12, 14), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1973, 12, 24), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1903, 7, 24), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2035, 7,  5), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2053, 7, 15), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2071, 7, 27), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1956, 1, 13), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1974, 1, 23), MoonPhase.NEW_MOON);

        // Meeus 1998, Example 49.a-b, p. 353
        put(new CalendarPoint(1977, 2, 18), MoonPhase.NEW_MOON);
        put(new CalendarPoint(2044, 1, 21, 23, 48, 0), MoonPhase.THIRD_QUARTER);

        // https://www.timeanddate.com/moon/phases/spain/madrid?year=1600 UTC -0:14:44
        put(new CalendarPoint(1600, 1, 16, 4, 51, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 2, 14, 17, 16, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 3, 15, 3, 27, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 4, 13, 11, 53, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 5, 12, 19, 27, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 6, 11, 3, 15, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 7, 10, 12, 16, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 8, 8, 23, 17, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 9, 7, 12, 42, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 10, 7, 4, 38, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 11, 5, 22, 38, 0), MoonPhase.NEW_MOON);
        put(new CalendarPoint(1600, 12, 5, 17, 42, 0), MoonPhase.NEW_MOON);
        //
        put(new CalendarPoint(1600, 1, 8), MoonPhase.THIRD_QUARTER);
        put(new CalendarPoint(1600, 1, 30), MoonPhase.FULL_MOON);
        put(new CalendarPoint(1600, 2, 21), MoonPhase.FIRST_QUARTER);
        put(new CalendarPoint(1600, 4, 20), MoonPhase.FIRST_QUARTER);
        put(new CalendarPoint(1600, 5, 28), MoonPhase.FULL_MOON);
        put(new CalendarPoint(1600, 7, 3), MoonPhase.THIRD_QUARTER);
        put(new CalendarPoint(1600, 7, 26), MoonPhase.FULL_MOON);
        put(new CalendarPoint(1600, 8, 16), MoonPhase.FIRST_QUARTER);
        put(new CalendarPoint(1600, 10, 15), MoonPhase.FIRST_QUARTER);
        put(new CalendarPoint(1600, 11, 20), MoonPhase.FULL_MOON);
        put(new CalendarPoint(1600, 12, 27), MoonPhase.THIRD_QUARTER);
    }};

    private MoonPhaseApproximator approximator = new MoonPhaseApproximator();

    @Test
    public void shouldApproximateBeGenerallyNotFurtherAwayFromResultDateThanArgument() {
        // with the dataset's precision the offset of abs <= 1.0 wouldn't make much sense
        double[] argumentOffsetsFromCenter = new double[] {-12.5, -9.0, -2.0, -25.0/24.0, 25.0/24.0, 3.0, 7.0, 10.0};

        DATE_TO_PHASE.entrySet().stream().forEach((e) -> {
            TimelinePoint center = TimelinePoint.ofCalendarPoint(e.getKey());
            for (double offset : argumentOffsetsFromCenter) {
                TimelinePoint argument = new TimelinePoint(center.julianDay + offset);
                double approximateJde = approximator.approximateJulianEphemerisDayAround(argument, e.getValue());
                double diff = Math.abs(Time.shiftDaysToTimeType(approximateJde, TimeType.UNIVERSAL, e.getKey().y) - center.julianDay);
                assertTrue(diff < Math.abs(offset));
            }
        });
    }

    @Test
    public void souldApproximateBeCloseToArgumentAcrossWholeTimeline() {
        double step = MeanCycle.LUNATION.epochalLengthDays * 0.51;
        double startJde = Timeline.JULIAN_PERIOD_START_JD + step;
        double endJde = Timeline.JULIAN_PERIOD_END_JD - step;
        double delta = MeanCycle.LUNATION.epochalLengthDays * 0.5 + Calcs.Time.timeToDays(0, 1, 0);
        MoonPhase[] phases = MoonPhase.values();

        for (double jde = startJde; jde < endJde; jde += step) {
            TimelinePoint argument = TimelinePoint.ofJulianEphemerisDay(jde);
            MoonPhase phase = phases[(int) jde % 4];
            double approximate = approximator.approximateJulianEphemerisDayAround(argument, phase);

            assertTrue(
                Math.abs(approximate - jde) < delta,
                "Too far away ~" + Calcs.roundToDelta(Math.abs(approximate - jde), 0.01)
                + ": ARG. " + argument.toCalendarPoint().formatDateTimeToMinutes()
                + "; APPROX. " + TimelinePoint.ofJulianEphemerisDay(approximate).formatCalendrically()
            );
        }
    }

    @Test
    public void shouldApproximateBeCloseToArgumentInSpecificCases() {
        // The case when the Meeus' approximator fails and returns a 2000s date...
        TimelinePoint tx = TimelinePoint.ofCalendarPoint(new CalendarPoint(0, 1, 1));
        double actualApproximateJde = approximator.approximateJulianEphemerisDayAround(tx, MoonPhase.FULL_MOON);
        if (Math.abs(actualApproximateJde - tx.julianDay) > MeanCycle.LUNATION.epochalLengthDays * 0.5 + Calcs.Time.timeToDays(0, 1, 0)) {
            fail(
                "Too far away: approximate(" + tx.toCalendarPoint().formatDateTimeToMinutes()
                + ") = " + TimelinePoint.ofJulianEphemerisDay(actualApproximateJde).formatCalendrically()
            );
        }
    }
}
