package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class TimeTest {

    @Test
    public void shouldDeltaTBeEqualToDynamicalSubtractUniversal() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int y = random.nextInt(Timeline.JULIAN_PERIOD_YEARS + 1) - 4712;
            int seconds = random.nextInt(3601);
            int deltaT = Time.getDeltaTSeconds(y);
            assertEquals(deltaT, seconds - Time.shiftSecondsToTimeType(seconds, TimeType.UNIVERSAL, y));
            assertEquals(deltaT, Time.shiftSecondsToTimeType(seconds, TimeType.DYNAMICAL, y) - seconds);
        }
    }

}
