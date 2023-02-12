package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;

public class EarthLongitudeCalculatorTest {
    /**
     * Meeus 1998: Example 25.b, p. 169; Example 27.b, pp. 180-181
     */
    private final static Map<Double, Double> JDE_TO_EARTH_LONGITUDE_DEGREES = new HashMap<Double, Double>() {{
        put(2448908.5,      19.907372);
        put(2437837.38589, 270.003272);
    }};

    private EarthLongitudeCalculator calculator = new EarthLongitudeCalculator();

    @Test
    public void shouldCalculateLongitude() {
        for (Map.Entry<Double, Double> entry : JDE_TO_EARTH_LONGITUDE_DEGREES.entrySet()) {
            TimelinePoint tx = new DynamicalTimelinePoint(entry.getKey());
            double actualLongitude = calculator.calculate(tx);
            assertEquals(entry.getValue(), Math.toDegrees(actualLongitude), decimalAutoDelta(entry.getValue()));
        }
    }
}
