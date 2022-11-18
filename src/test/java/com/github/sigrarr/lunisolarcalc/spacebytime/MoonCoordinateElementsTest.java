package com.github.sigrarr.lunisolarcalc.spacebytime;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.time.*;

import org.junit.jupiter.api.Test;

public class MoonCoordinateElementsTest {

    private MoonCoordinateElements elements = new MoonCoordinateElements();

    @Test
    public void shouldCalculateElements() {
        // Meeus 1998, Example 47.a, p. 342
        elements.calculate(TimelinePoint.ofCenturialT(-0.077221081451, TimeType.DYNAMICAL));
        assertEquals(134.290182, Math.toDegrees(elements.getLPrim()), decimalAutoDelta(0.000001));
        assertEquals(113.842304, Math.toDegrees(elements.getD()),     decimalAutoDelta(0.000001));
        assertEquals( 97.643514, Math.toDegrees(elements.getM()),     decimalAutoDelta(0.000001));
        assertEquals(  5.150833, Math.toDegrees(elements.getMPrim()), decimalAutoDelta(0.000001));
        assertEquals(219.889721, Math.toDegrees(elements.getF()),     decimalAutoDelta(0.000001));
        assertEquals(109.57, Math.toDegrees(elements.getA1()), decimalAutoDelta(0.01));
        assertEquals(123.78, Math.toDegrees(elements.getA2()), decimalAutoDelta(0.01));
        assertEquals(229.53, Math.toDegrees(elements.getA3()), decimalAutoDelta(0.01));
    }
}
