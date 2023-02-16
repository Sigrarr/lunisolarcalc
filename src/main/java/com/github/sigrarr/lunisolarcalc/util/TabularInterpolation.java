package com.github.sigrarr.lunisolarcalc.util;

import java.util.Arrays;

/**
 * Simple interpolation based on three tabular values.
 *
 * @see "Meeus 1998: Ch. 3 (pp. 23-25)"
 */
public abstract class TabularInterpolation {
    /**
     * Performs simple interpolation based on three tabular values.
     *
     * The passed arrays of arguments and values may be longer,
     * the base points will be then selected automatically.
     *
     * @param arguments     array of arguments (with known values) of a function
     * @param values        array of values corresponding to the arguments
     *                      (both arrays must have the same length)
     * @param x             argument whose value is to be found
     *                      (must fit between arguments given in array)
     * @return              interpolated value corresponding to the requested argument
     */
    public static double interpolate(double[] arguments, double[] values, double x) {
        validateGeneralParameters(arguments, values, x);

        int centerIndex = -1;
        double interpolatingFactor = Double.MAX_VALUE;
        for (int i = 1; i < arguments.length - 1; i++) {
            double argumentDiff = x - arguments[i];
            if (Math.abs(argumentDiff) < Math.abs(interpolatingFactor)) {
                centerIndex = i;
                interpolatingFactor = argumentDiff;
            }
        }

        return interpolateFromThreeValuesAndFactor(
            Arrays.copyOfRange(values, centerIndex - 1, centerIndex + 2),
            interpolatingFactor
        );
    }

    /**
     * Performs simple interpolation based on three tabular values.
     *
     * @param values                array of three known values of a function
     * @param interpolatingFactor   interpolating factor (presumably the distance between
     *                              the argument of the middle value and the argument
     *                              whose value is to be found)
     * @return                      interpolated value
     */
    public static double interpolateFromThreeValuesAndFactor(double[] values, double interpolatingFactor) {
        validateThreeValuesParameters(values);

        double a = values[1] - values[0];
        double b = values[2] - values[1];
        double c = b - a;

        return values[1] + 0.5 * interpolatingFactor * (a + b + interpolatingFactor * c);
    }

    private static void validateGeneralParameters(double arguments[], double[] values, double x) {
        boolean wrong = arguments.length < 3 || arguments.length != values.length
            || Double.compare(x, arguments[0]) < 0 || Double.compare(x, arguments[arguments.length - 1]) > 0;
        if (wrong)
            throw new IllegalArgumentException();
    }

    private static void validateThreeValuesParameters(double values[]) {
        if (values.length != 3)
            throw new IllegalArgumentException();
    }
}
