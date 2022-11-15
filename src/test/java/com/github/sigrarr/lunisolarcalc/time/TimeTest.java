package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimeTest {

    @Test
    public void shouldConvertHMinSToFractionalDays() {
        assertEquals(0.0,        Time.timeToDays( 0,  0,  0), Calcs.EPSILON_MIN);
        assertEquals(0.5,        Time.timeToDays(12,  0,  0), Calcs.EPSILON_MIN);
        assertEquals(0.50694444, Time.timeToDays(12, 10,  0), decimalAutoDelta(0.00000001));
        assertEquals(0.50716435, Time.timeToDays(12, 10, 19), decimalAutoDelta(0.00000001));
    }

    @Test
    public void shouldDeltaTBeEqualToDynamicalSubtractUniversal() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int y = random.nextInt(Timeline.JULIAN_PERIOD_YEARS + 1) + Timeline.JULIAN_PERIOD_START_Y;
            int seconds = random.nextInt(3601);
            int deltaT = Time.getDeltaTSeconds(y);
            assertEquals(deltaT, seconds - Time.shiftSecondsToTimeType(seconds, TimeType.UNIVERSAL, y));
            assertEquals(deltaT, Time.shiftSecondsToTimeType(seconds, TimeType.DYNAMICAL, y) - seconds);
        }
    }

}
