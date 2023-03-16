package com.github.sigrarr.lunisolarcalc.util;

import java.util.*;

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
        if (values.length != 3)
            throw new IllegalArgumentException();

        double a = values[1] - values[0];
        double b = values[2] - values[1];
        double c = b - a;

        return values[1] + 0.5 * interpolatingFactor * (a + b + interpolatingFactor * c);
    }

    /**
     * Seeks the zero point's argument by linear interpolation based on three tabular values.
     *
     * Unlike the Meeus's method, solves a quadratic equation (instead of
     * starting with an approximate interpolation factor and correcting it in iterations).
     *
     * @param arguments     array of three arguments of a function
     * @param values        array of three values corresponding to the arguments
     * @return              argument for which the function's value should be zero,
     *                      or an empty optional object if the function seems to have
     *                      no zero point between the given arguments
     */
    public static OptionalDouble interpolateZeroValueArgumentFromThreePoints(double arguments[], double values[])
    {
        if (arguments.length != 3 || values.length != 3)
            throw new IllegalArgumentException();

        double a = values[1] - values[0];
        double b = values[2] - values[1];
        double c = b - a;

        /**
         * n0 = -2*y2 / (a + b + c*n0) => c*n0^2 + (a+b)n0 + 2*y2 = 0
         */
        double qA = c;
        double qB = (a + b);
        double qC = 2 * values[1];
        double qD = (qB * qB) - (4 * qA * qC);
        int qDSgn = (int) Math.signum(Double.compare(qD, 0.0));

        if (qDSgn < 0)
            return OptionalDouble.empty();

        double factor;
        if (qDSgn == 0) {
            factor = -qB / (2 * qA);
        } else {
            double qDSqrt = Math.sqrt(qD);
            double factor1 = (-qB - qDSqrt)/(2 * qA);
            double factor2 = (-qB + qDSqrt)/(2 * qA);
            factor = Double.compare(Math.abs(factor1), Math.abs(factor2)) > 0 ? factor2 : factor1;
        }
        return OptionalDouble.of(arguments[1] + factor);
    }

    private static void validateGeneralParameters(double arguments[], double[] values, double x) {
        boolean wrong = arguments.length < 3 || arguments.length != values.length
            || Double.compare(x, arguments[0]) < 0 || Double.compare(x, arguments[arguments.length - 1]) > 0;
        if (wrong)
            throw new IllegalArgumentException();
    }
}
