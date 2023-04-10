package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs.Time;

public class TabularInterpolationTest {

    private static final double[] EXAMPLE_3A_ARGUMENTS = {5.0, 6.0, 7.0, 8.0, 9.0};
    private static final double[] EXAMPLE_3A_VALUES = {0.898013, 0.891109, 0.884226, 0.877366, 0.870531};
    private static final double[] EXAMPLE_15_A_ASCENSION_VALUES = {40.68021, 41.73129, 42.78204};
    private static final double[] EXAMPLE_15_A_DECLINATION_VALUES = {18.04761, 18.44092, 18.82742};
    private static final double[] EXAMPLE_3C_ARGUMENTS = {26.0, 27.0, 28.0};
    private static final double[] EXAMPLE_3C_VALUES = {-(28.0*60 + 13.4), 6.0*60 + 46.3, 38*60 + 23.2};
    private static final double[] EXAMPLE_3D_ARGUMENTS = {-1.0, 0.0, 1.0};
    private static final double[] EXAMPLE_3D_VALUES = {-2.0, 3.0, 2.0};
    private static final double[] EXAMPLE_3E_ARGUMENTS = {27.0, 27.5, 28.0, 28.5, 29.0};
    private static final double[] EXAMPLE_3E_VALUES = {
        Calcs.Angle.toSingleArcsecondsValue(0, 54, 36.125),
        Calcs.Angle.toSingleArcsecondsValue(0, 54, 24.606),
        Calcs.Angle.toSingleArcsecondsValue(0, 54, 15.486),
        Calcs.Angle.toSingleArcsecondsValue(0, 54,  8.694),
        Calcs.Angle.toSingleArcsecondsValue(0, 54,  4.133),
    };

    @Test
    public void shouldInterpolateFromThreeValuesAndFactor() {
        // Meeus 1998: Example 3.a, p. 25
        double[] values = Arrays.copyOfRange(EXAMPLE_3A_VALUES, 2, 5);
        double n = 0.18125;
        double actualResult = TabularInterpolation.interpolateFromThreeValuesAndFactor(values, n);
        double delta = decimalAutoDelta(0.000001);
        assertEquals(0.876125, actualResult, delta);

        // Meeus 1998: Example 15.a, pp. 103-104
        n = 0.51882;
        delta = decimalAutoDelta(0.00001);
        assertEquals(42.27648, TabularInterpolation.interpolateFromThreeValuesAndFactor(EXAMPLE_15_A_ASCENSION_VALUES, n), delta);
        assertEquals(18.64229, TabularInterpolation.interpolateFromThreeValuesAndFactor(EXAMPLE_15_A_DECLINATION_VALUES, n), delta);
    }

    @Test
    public void shouldInterpolateFromFiveValuesAndFactor() {
        // Meeus 1998: Example 3.e, pp. 28-29
        double n = (3.0 + 1.0/3.0) / 12.0;
        double actualResult = TabularInterpolation.interpolateFromFiveValuesAndFactor(EXAMPLE_3E_VALUES, n);
        double expectedResult = Calcs.Angle.toSingleArcsecondsValue(0, 54, 13.369);
        assertEquals(expectedResult, actualResult, decimalAutoDelta(0.001));
    }

    @Test
    public void shouldInterpolateFromGeneralParameters() {
        // Meeus 1998: Example 3.a, p. 25
        double x = 8.0 + Time.timeToDays(4, 21, 0);
        double actualResult = TabularInterpolation.interpolate(EXAMPLE_3A_ARGUMENTS, EXAMPLE_3A_VALUES, x);
        assertEquals(0.876125, actualResult, decimalAutoDelta(0.000001));

        // Meeus 1998: Example 3.e, pp. 28-29
        x = 28.0 + Time.timeToDays(3, 20, 0);
        actualResult = TabularInterpolation.interpolate(EXAMPLE_3E_ARGUMENTS, EXAMPLE_3E_VALUES, x);
        double expectedResult = Calcs.Angle.toSingleArcsecondsValue(0, 54, 13.369);
        assertEquals(expectedResult, actualResult, decimalAutoDelta(0.001));
    }

    @Test
    public void shouldInterpolateZeroPoint() {
        // Meeus 1998: Example 3.c, p. 26
        OptionalDouble actualResult = TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(EXAMPLE_3C_ARGUMENTS, EXAMPLE_3C_VALUES);
        assertEquals(26.79873, actualResult.getAsDouble(), decimalAutoDelta(0.00001));

        // Meeus 1998: Example 3.d, p. 27
        actualResult = TabularInterpolation.interpolateZeroPointFactorFromThreePoints(EXAMPLE_3D_ARGUMENTS, EXAMPLE_3D_VALUES);
        assertEquals(-0.720759220056, actualResult.getAsDouble(), decimalAutoDelta(0.000000000001));
        assertEquals(actualResult, TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(EXAMPLE_3D_ARGUMENTS, EXAMPLE_3D_VALUES));

        actualResult = TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(EXAMPLE_3D_ARGUMENTS, new double[] {2.0, 1.0, 2.0});
        assertFalse(actualResult.isPresent());
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

        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateFromFiveValuesAndFactor(
            new double[] {1.0, 2.0, 3.0, 4.0}, 0.1
        ));
        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateFromFiveValuesAndFactor(
            new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, 0.1
        ));

        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(
            new double[] {1.0, 2.0}, new double[] {1.0, 2.0, 3.0}
        ));
        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(
            new double[] {1.0, 2.0, 3.0}, new double[] {1.0, 2.0}
        ));
        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(
            new double[] {1.0, 2.0, 3.0, 4.0}, new double[] {1.0, 2.0, 3.0}
        ));
        assertThrows(IllegalArgumentException.class, () -> TabularInterpolation.interpolateZeroPointArgumentFromThreePoints(
            new double[] {1.0, 2.0, 3.0}, new double[] {1.0, 2.0, 3.0, 4.0}
        ));
    }
}
