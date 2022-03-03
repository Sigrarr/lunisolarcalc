package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.Test;

public class MoonCoordinatePeriodicTermsTest {

    private static final Map<Short, Integer> NON_ZERO_M_MULTIPLIER_TO_POWER_OF_ELEMENT = new HashMap<Short, Integer>() {{
        put((short) -2, 2);
        put((short) -1, 1);
        put((short)  1, 1);
        put((short)  2, 2);
    }};

    private MoonCoordinatePeriodicTerms periodicTerms = new MoonLongitudePeriodicTerms();

    @Test
    public void shouldCalculateE() {
        // Meeus 1998, Example 47.a, p. 342
        double actualE = periodicTerms.calculateEarthOrbitEccentricityElement(-0.077221081451);
        assertEquals(1.000194, actualE, autoDelta(1.000194));
    }

    @Test
    public void shouldCalculateEBasedCoefficient() {
        double cTLimit = Timeline.julianDayToCenturialT(Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(2200, 12, 31.5)));
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double cT = random.nextDouble() * cTLimit;
            double e = periodicTerms.calculateEarthOrbitEccentricityElement(cT);
            for (Entry<Short, Integer> entry : NON_ZERO_M_MULTIPLIER_TO_POWER_OF_ELEMENT.entrySet()) {
                double actualCoefficient = periodicTerms.calculateEarthOrbitEccentricityCoefficient(e, entry.getKey());
                assertEquals(Math.pow(e, entry.getValue()), actualCoefficient, Calcs.EPSILON_MIN);
            }
        }
    }
}
