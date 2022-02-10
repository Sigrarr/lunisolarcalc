package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import java.util.*;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.space.HeliocentricCoordinateCalculator.Unit;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;

/**
 * Meeus 1998: Example 25.b, p. 169; Example 27.b, pp. 180-181
 */
public class HeliocentricCoordinateCalculatorTest {

    private final static Map<Double, Double> JD_TO_EARTH_LONGITUDE_DEGREES = new HashMap<Double, Double>() {{
        put(2448908.5,      19.907372);
        put(2437837.38589, 270.003272);
    }};
    private final static Map<Double, Double> JD_TO_EARTH_RADIUS_AU = new HashMap<Double, Double>() {{
        put(2448908.5,      0.99760775);
        put(2437837.38589,  1.0163018);
    }};

    private HeliocentricCoordinateCalculator earthLongitudeCalculator = new HeliocentricCoordinateCalculator(new PeriodicTermsForEarthLongitude(), Unit.RADIAN);
    private HeliocentricCoordinateCalculator earthRadiusCalculator = new HeliocentricCoordinateCalculator(new PeriodicTermsForEarthRadius(), Unit.ASTRONOMICAL_UNIT);

    @Test
    public void shouldCalculateLongitude() {
        for (Map.Entry<Double, Double> entry : JD_TO_EARTH_LONGITUDE_DEGREES.entrySet()) {
            double tau = Timeline.julianDayToMillenialTau(entry.getKey());
            double actualLongitude = earthLongitudeCalculator.calculateCoordinate(tau);
            assertEquals(entry.getValue(), Math.toDegrees(actualLongitude), autoDelta(entry.getValue()));
        }
    }

    @Test
    public void shouldCalculateRadius() {
        for (Map.Entry<Double, Double> entry : JD_TO_EARTH_RADIUS_AU.entrySet()) {
            double tau = Timeline.julianDayToMillenialTau(entry.getKey());
            double actualRadius = earthRadiusCalculator.calculateCoordinate(tau);
            assertEquals(entry.getValue(), actualRadius, autoDelta(entry.getValue()));
        }
    }
}
