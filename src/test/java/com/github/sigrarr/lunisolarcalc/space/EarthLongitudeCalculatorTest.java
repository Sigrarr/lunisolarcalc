package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import java.util.*;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;

public class EarthLongitudeCalculatorTest {
    /**
     * Meeus 1998: Example 25.b, p. 169; Example 27.b, pp. 180-181
     */
    private final static Map<Double, Double> JD_TO_EARTH_LONGITUDE_DEGREES = new HashMap<Double, Double>() {{
        put(2448908.5,      19.907372);
        put(2437837.38589, 270.003272);
    }};

    private EarthLongitudeCalculator calculator = new EarthLongitudeCalculator();

    @Test
    public void shouldCalculateLongitude() {
        for (Map.Entry<Double, Double> entry : JD_TO_EARTH_LONGITUDE_DEGREES.entrySet()) {
            double tau = Timeline.julianDayToMillenialTau(entry.getKey());
            double actualLongitude = calculator.calculate(tau);
            assertEquals(entry.getValue(), Math.toDegrees(actualLongitude), autoDelta(entry.getValue()));
        }
    }
}
