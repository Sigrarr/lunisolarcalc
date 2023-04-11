package com.github.sigrarr.lunisolarcalc.util;

import java.util.*;

/**
 * Simple interpolation based on three or five tabular values.
 *
 * @see "Meeus 1998: Ch. 3 (pp. 23-25)"
 */
public abstract class TabularInterpolation {
    /**
     * Performs simple interpolation based on three or five tabular values.
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
        for (int i = arguments.length - 2; i > 0; i--) {
            double argumentDiff = x - arguments[i];
            if (Math.abs(argumentDiff) < Math.abs(interpolatingFactor)) {
                centerIndex = i;
                interpolatingFactor = argumentDiff;
            } else break;
        }

        double interval = Math.abs(arguments[centerIndex] - arguments[centerIndex + 1]);
        if (Double.compare(interval, 1.0) != 0)
            interpolatingFactor = interpolatingFactor / interval;

        boolean fiveValuesMode = values.length >= 5
            && centerIndex >= 2
            && centerIndex <= values.length - 3;

        return fiveValuesMode ?
            interpolateFromFiveValuesAndFactor(
                Arrays.copyOfRange(values, centerIndex - 2, centerIndex + 3),
                interpolatingFactor
            )
            : interpolateFromThreeValuesAndFactor(
                Arrays.copyOfRange(values, centerIndex - 1, centerIndex + 2),
                interpolatingFactor
            );
    }

    /**
     * Performs simple interpolation based on three tabular values.
     *
     * @param values                array of three known values of a function
     * @param interpolatingFactor   interpolating factor (presumably the relative distance between
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
     * Performs simple interpolation based on five tabular values.
     *
     * @param values                array of five known values of a function
     * @param interpolatingFactor   interpolating factor (presumably the relative distance between
     *                              the argument of the middle value and the argument
     *                              whose value is to be found)
     * @return                      interpolated value
     */
    public static double interpolateFromFiveValuesAndFactor(double[] values, double interpolatingFactor) {
        if (values.length != 5)
            throw new IllegalArgumentException();

        double a = values[1] - values[0];
        double b = values[2] - values[1];
        double c = values[3] - values[2];
        double d = values[4] - values[3];
        double e = b - a;
        double f = c - b;
        double g = d - c;
        double h = f - e;
        double j = g - f;
        double k = j - h;

        double n2 = interpolatingFactor * interpolatingFactor;
        return values[2]
            + interpolatingFactor / 2.0 * (b + c)
            + n2 / 2.0 * f
            + interpolatingFactor * (n2 - 1) / 12.0 * (h + j)
            + n2 * (n2 - 1) / 24.0  * k;
    }

    /**
     * Seeks the zero point's argument by linear interpolation based on three tabular values.
     *
     * Unlike the Meeus's method, solves a quadratic equation (instead of
     * starting with an approximate interpolating factor and correcting it in iterations).
     *
     * @param arguments     array of three arguments of a function
     * @param values        array of three values corresponding to the arguments
     * @return              argument for which the function's value should be zero,
     *                      or an empty optional object if the function seems to have
     *                      no zero point between the given arguments
     */
    public static OptionalDouble interpolateZeroPointArgumentFromThreePoints(double arguments[], double values[])
    {
        OptionalDouble factor = interpolateZeroPointFactorFromThreePoints(arguments, values);
        if (!factor.isPresent())
            return factor;
        double interval = Math.abs(arguments[1] - arguments[0]);
        double argumentDiff =  Double.compare(interval, 1.0) == 0 ? factor.getAsDouble() : factor.getAsDouble() * interval;
        return OptionalDouble.of(arguments[1] + argumentDiff);
    }

    /**
     * Seeks the zero point's interpolating factor by linear interpolation based on three tabular values.
     *
     * Unlike the Meeus's method, solves a quadratic equation (instead of
     * starting with an approximate interpolating factor and correcting it in iterations).
     *
     * @param arguments     array of three arguments of a function
     * @param values        array of three values corresponding to the arguments
     * @return              the interpolating factor for which the function's value should be zero,
     *                      or an empty optional object if the function seems to have
     *                      no zero point between the given arguments
     */
    public static OptionalDouble interpolateZeroPointFactorFromThreePoints(double arguments[], double values[])
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

        double factor = qDSgn == 0 ? -qB / (2 * qA)
            : (-2 * qC) / (qB + Math.signum(qB)*Math.sqrt(qD));
        return OptionalDouble.of(factor);
    }

    private static void validateGeneralParameters(double arguments[], double[] values, double x) {
        boolean wrong = arguments.length < 3 || arguments.length != values.length
            || Double.compare(x, arguments[0]) < 0 || Double.compare(x, arguments[arguments.length - 1]) > 0;
        if (wrong)
            throw new IllegalArgumentException();
    }
}
