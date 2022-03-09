package com.github.sigrarr.lunisolarcalc.spacebytime;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MoonCoordinateElementsTest {
    
    private MoonCoordinateElements elements = new MoonCoordinateElements();

    @Test
    public void shouldCalculateElements() {
        // Meeus 1998, Example 47.a, p. 342
        elements.calculate(-0.077221081451);
        assertEquals(134.290182, Math.toDegrees(elements.getLPrim()), autoDelta(0.000001));
        assertEquals(113.842304, Math.toDegrees(elements.getD()),     autoDelta(0.000001));
        assertEquals( 97.643514, Math.toDegrees(elements.getM()),     autoDelta(0.000001));
        assertEquals(  5.150833, Math.toDegrees(elements.getMPrim()), autoDelta(0.000001));
        assertEquals(219.889721, Math.toDegrees(elements.getF()),     autoDelta(0.000001));
        assertEquals(109.57, Math.toDegrees(elements.getA1()), autoDelta(0.01));
        assertEquals(123.78, Math.toDegrees(elements.getA2()), autoDelta(0.01));
        assertEquals(229.53, Math.toDegrees(elements.getA3()), autoDelta(0.01));
    }
}
