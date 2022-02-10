package com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.autoDelta;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EarthNutuationElementsVectorTest {

    private EarthNutuationElementsVector vector = new EarthNutuationElementsVector();

    @Test
    public void shouldCalculateElements() {
        // Meeus 1998, Example 22.a, p. 148
        vector.calculate(-0.127296372348);
        assertEquals(136.9623, Math.toDegrees(vector.getD()),       autoDelta(0.0001));
        assertEquals( 94.9792, Math.toDegrees(vector.getM()),       autoDelta(0.0001));
        assertEquals(229.2784, Math.toDegrees(vector.getMPrim()),   autoDelta(0.0001));
        assertEquals(143.4079, Math.toDegrees(vector.getF()),       autoDelta(0.0001));
        assertEquals( 11.2531, Math.toDegrees(vector.getOmega()),   autoDelta(0.0001));
    }
}
