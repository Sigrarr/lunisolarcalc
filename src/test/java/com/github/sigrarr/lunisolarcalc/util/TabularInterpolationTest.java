package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs.Time;

public class TabularInterpolationTest {

    private static final double[] EXAMPLE_ARGUMENTS = {5.0, 6.0, 7.0, 8.0, 9.0};
    private static final double[] EXAMPLE_VALUES = {0.898013, 0.891109, 0.884226, 0.877366, 0.870531};

    @Test
    public void shouldInterpolateFromThreeValuesAndFactor() {
        // Meeus 1998: Example 3.a, p. 25
        double[] values = Arrays.copyOfRange(EXAMPLE_VALUES, 2, 5);
        double n = 0.18125;
        double actualResult = TabularInterpolation.interpolateFromThreeValuesAndFactor(values, n);
        assertEquals(0.876125, actualResult, decimalAutoDelta(0.000001));
    }

    @Test
    public void shouldInterpolateFromGeneralParameters() {
        // Meeus 1998: Example 3.a, p. 25
        double x = 8.0 + Time.timeToDays(4, 21, 0);
        double actualResult = TabularInterpolation.interpolate(EXAMPLE_ARGUMENTS, EXAMPLE_VALUES, x);
        assertEquals(0.876125, actualResult, decimalAutoDelta(0.000001));
    }

    @Test
    public void shouldValidateParameters() {
        assertThrows(IllegalArgumentException.class, () -> {
            TabularInterpolation.interpolate(new double[] {1.0, 2.0}, new double[] {3.0, 4.0}, 1.5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            TabularInterpolation.interpolate(new double[] {1.0, 2.0, 3.0}, new double[] {3.0, 4.0}, 2.5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            TabularInterpolation.interpolate(new double[] {1.0, 2.0, 3.0}, new double[] {3.0, 4.0, 5.0, 6.0}, 2.5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            TabularInterpolation.interpolate(new double[] {1.0, 2.0, 3.0}, new double[] {3.0, 4.0, 5.0}, 0.5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            TabularInterpolation.interpolate(new double[] {1.0, 2.0, 3.0}, new double[] {3.0, 4.0, 5.0}, 3.5);
        });

        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {1.0, 2.0}, 0.1
        ));
        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateFromThreeValuesAndFactor(
            new double[] {1.0, 2.0, 3.0, 4.0}, 0.1
        ));
    }
}
