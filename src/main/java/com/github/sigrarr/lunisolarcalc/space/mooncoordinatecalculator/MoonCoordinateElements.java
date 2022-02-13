package com.github.sigrarr.lunisolarcalc.space.mooncoordinatecalculator;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.toLongitudinallyNormalRadians;

public class MoonCoordinateElements {

    public static final int INDEX_MEAN_LONGITUDE = 0;
    public static final int INDEX_MEAN_ELONGATION = 1;
    public static final int INDEX_MEAN_ANOMALY_OF_SUN = 2;
    public static final int INDEX_MEAN_ANOMALY = 3;
    public static final int INDEX_MEAN_DISTANCE_FROM_ASCENDING_NODE = 4;
    public static final int INDEX_ADDITIONAL_ARGUMENT_VENUS = 5;
    public static final int INDEX_ADDITIONAL_ARGUMENT_JUPITER = 6;
    public static final int INDEX_ADDITIONAL_ARGUMENT_3 = 7;
    public static final int ELEMENTS_N = 8;

    private double[] values = new double[ELEMENTS_N];

    /**
     * Meeus 1998, pp. 337-338
     */
    public void calculate(double centurialT) {
        double cT2 = centurialT * centurialT;
        double cT3 = cT2 * centurialT;
        double cT4 = cT3 * centurialT;
        values[INDEX_MEAN_LONGITUDE] = toLongitudinallyNormalRadians(
            218.3164477 + (481267.88123421 * centurialT) - (0.0015786 * cT2) + (cT3 / 538841.0) - (cT4 / 65194000.0)
        );
        values[INDEX_MEAN_ELONGATION] = toLongitudinallyNormalRadians(
            297.8501921 + (445267.1114034 * centurialT) - (0.0018819 * cT2) + (cT3 / 545868.0) - (cT4 / 113065000.0)
        );
        values[INDEX_MEAN_ANOMALY_OF_SUN] = toLongitudinallyNormalRadians(
            357.5291092 + (35999.0502909 * centurialT) - (0.0001536 * cT2) + (cT3 / 24490000.0)
        );
        values[INDEX_MEAN_ANOMALY] = toLongitudinallyNormalRadians(
            134.9633964 + (477198.8675055 * centurialT) + (0.0087414 * cT2) + (cT3 / 69699.0) - (cT4 / 14712000.0)
        );
        values[INDEX_MEAN_DISTANCE_FROM_ASCENDING_NODE] = toLongitudinallyNormalRadians(
            93.2720950 + (483202.0175233 * centurialT) - (0.0036539 * cT2) - (cT3 / 3526000.0) + (cT4 / 863310000.0)
        );
        values[INDEX_ADDITIONAL_ARGUMENT_VENUS] = toLongitudinallyNormalRadians(
            119.75 + (131.849 * centurialT)
        );
        values[INDEX_ADDITIONAL_ARGUMENT_JUPITER] = toLongitudinallyNormalRadians(
            53.09 + (479264.290 * centurialT)
        );
        values[INDEX_ADDITIONAL_ARGUMENT_3] = toLongitudinallyNormalRadians(
            313.45 + (481266.484 * centurialT)
        );
    }

    public MoonCoordinateElements() {}

    public MoonCoordinateElements(double centurialT) {
        calculate(centurialT);
    }

    public double getValue(int index) {
        return values[index];
    }

    public double getLPrim() {
        return values[INDEX_MEAN_LONGITUDE];
    }

    public double getD() {
        return values[INDEX_MEAN_ELONGATION];
    }

    public double getM() {
        return values[INDEX_MEAN_ANOMALY_OF_SUN];
    }

    public double getMPrim() {
        return values[INDEX_MEAN_ANOMALY];
    }

    public double getF() {
        return values[INDEX_MEAN_DISTANCE_FROM_ASCENDING_NODE];
    }

    public double getA1() {
        return values[INDEX_ADDITIONAL_ARGUMENT_VENUS];
    }

    public double getA2() {
        return values[INDEX_ADDITIONAL_ARGUMENT_JUPITER];
    }

    public double getA3() {
        return values[INDEX_ADDITIONAL_ARGUMENT_3];
    }
}