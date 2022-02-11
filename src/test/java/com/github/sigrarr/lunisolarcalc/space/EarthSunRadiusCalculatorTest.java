package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import java.util.*;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;


public class EarthSunRadiusCalculatorTest {
    /**
     * Meeus 1998: Example 25.b, p. 169; Example 27.b, pp. 180-181
     */
    private final static Map<Double, Double> JD_TO_EARTH_RADIUS_AU = new HashMap<Double, Double>() {{
        put(2448908.5,      0.99760775);
        put(2437837.38589,  1.0163018);
    }};

    private EarthSunRadiusCalculator calculator = new EarthSunRadiusCalculator();

    @Test
    public void shouldCalculateRadius() {
        for (Map.Entry<Double, Double> entry : JD_TO_EARTH_RADIUS_AU.entrySet()) {
            double tau = Timeline.julianDayToMillenialTau(entry.getKey());
            double actualRadius = calculator.calculate(tau);
            assertEquals(entry.getValue(), actualRadius, autoDelta(entry.getValue()));
        }
    }
}
