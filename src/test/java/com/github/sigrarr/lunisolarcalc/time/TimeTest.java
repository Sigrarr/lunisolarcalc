package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimeTest {
    
    private static final int[] YEARS = {-333, 0, 300, 345, 888, 1267, 1600, 2000, 2145};
    private Random random = new Random();

    @Test
    public void shouldConvertHISToFractionalDays() {
        assertEquals(0.0,        Time.timeToDays( 0,  0,  0), Calcs.EPSILON_MIN);
        assertEquals(0.5,        Time.timeToDays(12,  0,  0), Calcs.EPSILON_MIN);
        assertEquals(0.50694444, Time.timeToDays(12, 10,  0), autoDelta(0.00000001));
        assertEquals(0.50716435, Time.timeToDays(12, 10, 19), autoDelta(0.00000001));
    }

    @Test
    public void shouldDeltaTBeEqualToDynamicalSubtractUniversal() {
        for (int y : YEARS) {
            int deltaT = Time.getDeltaTSeconds(y);
            for (int r = 0; r < 10; r++) {
                int randomSeconds = random.nextInt(24 * 60 * 60);
                assertEquals(deltaT, Time.universalToDynamicSeconds(randomSeconds, y) - randomSeconds);
                assertEquals(deltaT, randomSeconds - Time.dynamicToUniversalSeconds(randomSeconds, y));
            }
        }
    }
}
