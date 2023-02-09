package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public class SiderealMeanTimeCalculatorTest {

    private SiderealMeanTimeCalculator calculator = new SiderealMeanTimeCalculator();

    @Test
    public void shouldCalculateThetaZero() {
        // Meeus 1998: Example 12.a, p. 88
        TimelinePoint tx = UniversalTimelinePoint.ofCalendaricParameters(1987, 4, 10.0);
        double actualThetaZero = calculator.calculate(tx);
        double expectedThetaZeroDegrees = 360.0 * Calcs.Time.timeToDays(13, 10, 46.3668);
        double delta = 360.0 * Calcs.SECOND_TO_DAY * TestUtils.decimalAutoDelta(0.0001);
        assertEquals(expectedThetaZeroDegrees, actualThetaZero, delta);

        // Meeus 1998: Example 12.b, p. 89
        tx = UniversalTimelinePoint.ofCalendaricParameters(1987, 4, 10, 19, 21, 0);
        actualThetaZero = calculator.calculate(tx);
        expectedThetaZeroDegrees = 128.7378734;
        assertEquals(expectedThetaZeroDegrees, actualThetaZero, delta);
    }
}
