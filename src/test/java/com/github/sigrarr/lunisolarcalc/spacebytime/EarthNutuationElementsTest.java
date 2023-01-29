package com.github.sigrarr.lunisolarcalc.spacebytime;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.time.*;

import org.junit.jupiter.api.Test;

public class EarthNutuationElementsTest {

    private EarthNutuationElements elements = EarthNutuationElements.makeUnevaluatedInstance();

    @Test
    public void shouldCalculateElements() {
        // Meeus 1998, Example 22.a, p. 148
        elements.calculate(TimelinePoint.ofCenturialT(-0.127296372348, TimeScale.DYNAMICAL));
        assertEquals(136.9623, Math.toDegrees(elements.getD()),       decimalAutoDelta(0.0001));
        assertEquals( 94.9792, Math.toDegrees(elements.getM()),       decimalAutoDelta(0.0001));
        assertEquals(229.2784, Math.toDegrees(elements.getMPrim()),   decimalAutoDelta(0.0001));
        assertEquals(143.4079, Math.toDegrees(elements.getF()),       decimalAutoDelta(0.0001));
        assertEquals( 11.2531, Math.toDegrees(elements.getOmega()),   decimalAutoDelta(0.0001));
    }
}
