package com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EarthNutuationElementsTest {

    private EarthNutuationElements elements = new EarthNutuationElements();

    @Test
    public void shouldCalculateElements() {
        // Meeus 1998, Example 22.a, p. 148
        elements.calculate(-0.127296372348);
        assertEquals(136.9623, Math.toDegrees(elements.getD()),       autoDelta(0.0001));
        assertEquals( 94.9792, Math.toDegrees(elements.getM()),       autoDelta(0.0001));
        assertEquals(229.2784, Math.toDegrees(elements.getMPrim()),   autoDelta(0.0001));
        assertEquals(143.4079, Math.toDegrees(elements.getF()),       autoDelta(0.0001));
        assertEquals( 11.2531, Math.toDegrees(elements.getOmega()),   autoDelta(0.0001));
    }
}
