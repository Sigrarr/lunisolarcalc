package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;


public class EarthSunRadiusCalculatorTest {
    /**
     * Meeus 1998: Example 25.b, p. 169; Example 27.b, pp. 180-181
     */
    private final static Map<Double, Double> JDE_TO_EARTH_RADIUS_AU = new HashMap<Double, Double>() {{
        put(2448908.5,      0.99760775);
        put(2437837.38589,  1.0163018);
    }};

    private EarthSunRadiusCalculator calculator = new EarthSunRadiusCalculator();

    @Test
    public void shouldCalculateRadius() {
        for (Map.Entry<Double, Double> entry : JDE_TO_EARTH_RADIUS_AU.entrySet()) {
            TimelinePoint tx = TimelinePoint.ofJulianEphemerisDay(entry.getKey());
            double actualRadius = calculator.calculate(tx);
            assertEquals(entry.getValue(), actualRadius, decimalAutoDelta(entry.getValue()));
        }
    }
}
