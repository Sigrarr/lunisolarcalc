package com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.toLongitudinallyNormalRadians;

public class EarthNutuationElementsVector {

    public static final int DIMENSION_MEAN_ELONGATION_OF_MOON_FROM_SUN = 0;
    public static final int DIMENSION_MEAN_ANOMALY_OF_SUN = 1;
    public static final int DIMENSION_MEAN_ANOMALY_OF_MOON = 2;
    public static final int DIMENSION_ARGUMENT_OF_LATITUDE_OF_MOON = 3;
    public static final int DIMENSION_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON = 4;
    public static final int DIMENSIONS_N = 5;

    private double[] values = new double[DIMENSIONS_N];

    /**
     * Meeus 1998, Ch. 22, p. 144
     */
    public void calculate(double centurialT) {
        double cT2 = centurialT * centurialT;
        double cT3 = cT2 * centurialT;
        values[DIMENSION_MEAN_ELONGATION_OF_MOON_FROM_SUN] = toLongitudinallyNormalRadians(
            297.85036 + (445267.11148 * centurialT) - (0.0019142 * cT2) + (cT3 / 189474.0)
        );
        values[DIMENSION_MEAN_ANOMALY_OF_SUN] = toLongitudinallyNormalRadians(
            357.52772 + (35999.05034 * centurialT) - (0.0001603 * cT2) - (cT3 / 300000.0)
        );
        values[DIMENSION_MEAN_ANOMALY_OF_MOON] = toLongitudinallyNormalRadians(
            134.96298 + (477198.867398 * centurialT) + (0.0086972 * cT2) + (cT3 / 56250.0)
        );
        values[DIMENSION_ARGUMENT_OF_LATITUDE_OF_MOON] = toLongitudinallyNormalRadians(
            93.27191 + (483202.017538 * centurialT) - (0.0036825 * cT2) + (cT3 / 327270.0)
        );
        values[DIMENSION_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON] = toLongitudinallyNormalRadians(
            125.04452 - (1934.136261 * centurialT) + (0.0020708 * cT2) + (cT3 / 450000.0)
        );
    }

    public EarthNutuationElementsVector() {}

    public EarthNutuationElementsVector(double centurialT) {
        calculate(centurialT);
    }

    public double getValue(int dimension) {
        return values[dimension];
    }

    public double getD() {
        return values[DIMENSION_MEAN_ELONGATION_OF_MOON_FROM_SUN];
    }

    public double getM() {
        return values[DIMENSION_MEAN_ANOMALY_OF_SUN];
    }

    public double getMPrim() {
        return values[DIMENSION_MEAN_ANOMALY_OF_MOON];
    }

    public double getF() {
        return values[DIMENSION_ARGUMENT_OF_LATITUDE_OF_MOON];
    }

    public double getOmega() {
        return values[DIMENSION_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON];
    }
}
