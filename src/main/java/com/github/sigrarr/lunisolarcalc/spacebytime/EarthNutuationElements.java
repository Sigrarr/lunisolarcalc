package com.github.sigrarr.lunisolarcalc.spacebytime;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.toLongitudinallyNormalRadians;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public class EarthNutuationElements implements Provider<Subject, Double> {

    public static final int INDEX_MEAN_ELONGATION_OF_MOON_FROM_SUN = 0;
    public static final int INDEX_MEAN_ANOMALY_OF_SUN = 1;
    public static final int INDEX_MEAN_ANOMALY_OF_MOON = 2;
    public static final int INDEX_ARGUMENT_OF_LATITUDE_OF_MOON = 3;
    public static final int INDEX_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON = 4;
    public static final int ELEMENTS_N = 5;

    private double[] values = new double[ELEMENTS_N];

    /**
     * Meeus 1998, Ch. 22, p. 144
     */
    public void calculate(double centurialT) {
        double cT2 = centurialT * centurialT;
        double cT3 = cT2 * centurialT;
        values[INDEX_MEAN_ELONGATION_OF_MOON_FROM_SUN] = toLongitudinallyNormalRadians(
            297.85036 + (445267.11148 * centurialT) - (0.0019142 * cT2) + (cT3 / 189474.0)
        );
        values[INDEX_MEAN_ANOMALY_OF_SUN] = toLongitudinallyNormalRadians(
            357.52772 + (35999.05034 * centurialT) - (0.0001603 * cT2) - (cT3 / 300000.0)
        );
        values[INDEX_MEAN_ANOMALY_OF_MOON] = toLongitudinallyNormalRadians(
            134.96298 + (477198.867398 * centurialT) + (0.0086972 * cT2) + (cT3 / 56250.0)
        );
        values[INDEX_ARGUMENT_OF_LATITUDE_OF_MOON] = toLongitudinallyNormalRadians(
            93.27191 + (483202.017538 * centurialT) - (0.0036825 * cT2) + (cT3 / 327270.0)
        );
        values[INDEX_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON] = toLongitudinallyNormalRadians(
            125.04452 - (1934.136261 * centurialT) + (0.0020708 * cT2) + (cT3 / 450000.0)
        );
    }

    public EarthNutuationElements() {}

    public EarthNutuationElements(double centurialT) {
        calculate(centurialT);
    }

    public double getValue(int index) {
        return values[index];
    }

    public double getD() {
        return values[INDEX_MEAN_ELONGATION_OF_MOON_FROM_SUN];
    }

    public double getM() {
        return values[INDEX_MEAN_ANOMALY_OF_SUN];
    }

    public double getMPrim() {
        return values[INDEX_MEAN_ANOMALY_OF_MOON];
    }

    public double getF() {
        return values[INDEX_ARGUMENT_OF_LATITUDE_OF_MOON];
    }

    public double getOmega() {
        return values[INDEX_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON];
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.noneOf(Subject.class);
    }

    @Override
    public Subject provides() {
        return Subject.EARTH_NUTUATION_ELEMENTS;
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> requiredArguments) {
        calculate(centurialT);
        return this;
    }

    @Override
    public Provider<Subject, Double> getInstanceForNewComposition() {
        return new EarthNutuationElements();
    }
}
