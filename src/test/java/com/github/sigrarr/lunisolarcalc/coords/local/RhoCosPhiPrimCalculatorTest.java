package com.github.sigrarr.lunisolarcalc.coords.local;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.tutil.TestUtils;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class RhoCosPhiPrimCalculatorTest {

    @Test
    public void shouldCalculateRhoCosPhiPrim() {
        // Meeus 1998, example 11.a, p. 82-83
        GeoPosition pos = GeoPosition.ofConventionalDegrees(
            Calcs.Angle.toSingleDegreesValue(33, 21, 22),
            -116.865,
            1706
        );

        double actual = new RhoCosPhiPrimCalculator(pos).calculate();
        assertEquals(0.836339, actual, TestUtils.decimalAutoDelta(0.000001));
    }
}
