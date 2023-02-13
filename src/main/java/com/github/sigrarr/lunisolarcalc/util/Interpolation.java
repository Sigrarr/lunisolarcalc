package com.github.sigrarr.lunisolarcalc.util;

import java.util.Arrays;

// TODO test, description and validation
public abstract class Interpolation {

    public static double interpolate(double[] arguments, double[] values, double x) {
        int centerIndex = -1;
        double interpolatingFactor = Double.MAX_VALUE;

        for (int i = 1; i < arguments.length - 1; i++) {
            double argumentDiff = x - values[i];
            if (Math.abs(argumentDiff) < interpolatingFactor) {
                centerIndex = i;
                interpolatingFactor = argumentDiff;
            }
        }

        return interpolateFromThreeValuesAndFactor(
            Arrays.copyOfRange(values, centerIndex - 1, centerIndex + 1),
            interpolatingFactor
        );
    }

    public static double interpolateFromThreeValuesAndFactor(double[] values, double interpolatingFactor) {
        double a = values[1] - values[0];
        double b = values[2] - values[1];
        double c = b - a;

        return values[1] + 0.5 * interpolatingFactor * (a + b + interpolatingFactor * c);
    }
}
