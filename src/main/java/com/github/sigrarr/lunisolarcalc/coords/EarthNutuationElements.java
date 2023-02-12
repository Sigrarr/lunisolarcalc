package com.github.sigrarr.lunisolarcalc.coords;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.normalizeLongitudinally;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.DoubleRow;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Group of intermediate arguments used in periodic terms for the Earth's nutuation (D, M, M', F, Ω).
 * Rather quick calculation. An instance is stateful, contains the last calculation's results.
 * {@linkplain CalculationComposer Composable}, pre-registered in {@link CalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (p. 143...)"
 */
public class EarthNutuationElements implements Provider<Subject, TimelinePoint>, DoubleRow {

    public static final Subject SUBJECT = Subject.EARTH_NUTUATION_ELEMENTS;

    public static final int INDEX_MEAN_ELONGATION_OF_MOON_FROM_SUN = 0;
    public static final int INDEX_MEAN_ANOMALY_OF_SUN = 1;
    public static final int INDEX_MEAN_ANOMALY_OF_MOON = 2;
    public static final int INDEX_ARGUMENT_OF_LATITUDE_OF_MOON = 3;
    public static final int INDEX_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON = 4;
    public static final int ELEMENTS_N = 5;

    private double[] values = new double[ELEMENTS_N];

    /**
     * Evaluates the group of intermediate arguments used in periodic terms for the Earth's nutuation (D, M, M', F, Ω).
     * Rather quick. Calculated values will be available through getters of this object.
     * Overwrites previous results, if there are any.
     *
     * @param tx    time argument
     */
    public void calculate(TimelinePoint tx) {
        double cT = tx.toDynamicalTime().toCenturialT();
        double cT2 = cT * cT;
        double cT3 = cT2 * cT;
        values[INDEX_MEAN_ELONGATION_OF_MOON_FROM_SUN] = normalizeLongitudinally(Math.toRadians(
            297.85036 + (445267.11148 * cT) - (0.0019142 * cT2) + (cT3 / 189474.0)
        ));
        values[INDEX_MEAN_ANOMALY_OF_SUN] = normalizeLongitudinally(Math.toRadians(
            357.52772 + (35999.05034 * cT) - (0.0001603 * cT2) - (cT3 / 300000.0)
        ));
        values[INDEX_MEAN_ANOMALY_OF_MOON] = normalizeLongitudinally(Math.toRadians(
            134.96298 + (477198.867398 * cT) + (0.0086972 * cT2) + (cT3 / 56250.0)
        ));
        values[INDEX_ARGUMENT_OF_LATITUDE_OF_MOON] = normalizeLongitudinally(Math.toRadians(
            93.27191 + (483202.017538 * cT) - (0.0036825 * cT2) + (cT3 / 327270.0)
        ));
        values[INDEX_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON] = normalizeLongitudinally(Math.toRadians(
            125.04452 - (1934.136261 * cT) + (0.0020708 * cT2) + (cT3 / 450000.0)
        ));
    }

    /**
     * Creates an instance and evaluates it immediately (calculates values).
     *
     * @param tx    time argument
     * @see         #calculate(TimelinePoint)
     */
    public EarthNutuationElements(TimelinePoint tx) {
        calculate(tx);
    }

    /**
     * Creates an instance without values.
     * Getters of such instance must not be used before evaluation.
     *
     * @return  a new instance, without values
     * @see     #calculate(TimelinePoint)
     */
    public static EarthNutuationElements makeUnevaluatedInstance() {
        return new EarthNutuationElements();
    }

    private EarthNutuationElements() {}

    /**
     * Gets the value: mean elongation of the Moon from the Sun (D): [0, 2π)
     * @return  value: mean elongation of the Moon from the Sun (D): [0, 2π)
     */
    public double getD() {
        return values[INDEX_MEAN_ELONGATION_OF_MOON_FROM_SUN];
    }

    /**
     * Gets the value: mean anomaly of the Sun (M): [0, 2π)
     * @return  value: mean anomaly of the Sun (M): [0, 2π)
     */
    public double getM() {
        return values[INDEX_MEAN_ANOMALY_OF_SUN];
    }

    /**
     * Gets the value: mean anomaly of the Moon (M'): [0, 2π)
     * @return  value: mean anomaly of the Moon (M'): [0, 2π)
     */
    public double getMPrim() {
        return values[INDEX_MEAN_ANOMALY_OF_MOON];
    }

    /**
     * Gets the value: Moon's argument of latitude (F): [0, 2π)
     * @return  value: Moon's argument of latitude (F): [0, 2π)
     */
    public double getF() {
        return values[INDEX_ARGUMENT_OF_LATITUDE_OF_MOON];
    }

    /**
     * Gets the value: longitude of the ascending node of the Moon's mean orbit on the ecliptic (Ω): [0, 2π)
     * @return  value: longitude of the ascending node of the Moon's mean orbit on the ecliptic (Ω): [0, 2π)
     */
    public double getOmega() {
        return values[INDEX_LONGITUDE_OF_ASCENDING_NODE_OF_MEAN_ORBIT_OF_MOON];
    }

    /**
     * Gets the value: one of intermediate arguments used in periodic terms for the Earth's nutuation, by index: [0, 2π).
     *
     * @param index     from 0, lesser than {@value #ELEMENTS_N}
     * @return          value: one of intermediate arguments used in periodic terms for the Earth's nutuation: [0, 2π)
     */
    @Override
    public double getValue(int index) {
        return values[index];
    }

    @Override
    public int getSize() {
        return ELEMENTS_N;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.noneOf(Subject.class);
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EarthNutuationElements calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        calculate(tx);
        return this;
    }

    @Override
    public Provider<Subject, TimelinePoint> getInstanceForNewComposition() {
        return makeUnevaluatedInstance();
    }
}
