package com.github.sigrarr.lunisolarcalc.spacebytime;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.normalizeLongitudinally;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Group of intermediate arguments used in periodic terms for the Moon's coordinates (L', D, M, M', F, A1, A2, A3).
 * Rather quick calculation. An instance is stateful, contains the last calculation's results.
 * {@link CalculationComposer Composable}, pre-registered in {@link SpaceByTimeCalcComposition}.
 *
 * @see " Meeus 1998: Ch. 47 (p. 337...)
 */
public class MoonCoordinateElements implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.MOON_COORDINATE_ELEMENTS;
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
     * Evaluates the group of intermediate arguments used in periodic terms for the Moon's coordinates (L', D, M, M', F, A1, A2, A3).
     * Rather quick. Calculated values will be available through getters of this object.
     * Overwrites previous results, if there are any.
     *
     * @param tx    time argument
     */
    public void calculate(TimelinePoint tx) {
        double cT = tx.toDynamicalTime().toCenturialT();
        double cT2 = cT * cT;
        double cT3 = cT2 * cT;
        double cT4 = cT3 * cT;
        values[INDEX_MEAN_LONGITUDE] = normalizeLongitudinally(Math.toRadians(
            218.3164477 + (481267.88123421 * cT) - (0.0015786 * cT2) + (cT3 / 538841.0) - (cT4 / 65194000.0)
        ));
        values[INDEX_MEAN_ELONGATION] = normalizeLongitudinally(Math.toRadians(
            297.8501921 + (445267.1114034 * cT) - (0.0018819 * cT2) + (cT3 / 545868.0) - (cT4 / 113065000.0)
        ));
        values[INDEX_MEAN_ANOMALY_OF_SUN] = normalizeLongitudinally(Math.toRadians(
            357.5291092 + (35999.0502909 * cT) - (0.0001536 * cT2) + (cT3 / 24490000.0)
        ));
        values[INDEX_MEAN_ANOMALY] = normalizeLongitudinally(Math.toRadians(
            134.9633964 + (477198.8675055 * cT) + (0.0087414 * cT2) + (cT3 / 69699.0) - (cT4 / 14712000.0)
        ));
        values[INDEX_MEAN_DISTANCE_FROM_ASCENDING_NODE] = normalizeLongitudinally(Math.toRadians(
            93.2720950 + (483202.0175233 * cT) - (0.0036539 * cT2) - (cT3 / 3526000.0) + (cT4 / 863310000.0)
        ));
        values[INDEX_ADDITIONAL_ARGUMENT_VENUS] = normalizeLongitudinally(Math.toRadians(
            119.75 + (131.849 * cT)
        ));
        values[INDEX_ADDITIONAL_ARGUMENT_JUPITER] = normalizeLongitudinally(Math.toRadians(
            53.09 + (479264.290 * cT)
        ));
        values[INDEX_ADDITIONAL_ARGUMENT_3] = normalizeLongitudinally(Math.toRadians(
            313.45 + (481266.484 * cT)
        ));
    }

    /**
     * Creates an instance and evaluates it immediately (calculates values).
     *
     * @param tx    time argument
     * @see         #calculate(TimelinePoint)
     */
    public MoonCoordinateElements(TimelinePoint tx) {
        calculate(tx);
    }

    /**
     * Creates an instance without values.
     * Getters of such instance must not be used before evaluation.
     *
     * @return  a new instance, without values
     * @see     #calculate(TimelinePoint)
     */
    public static MoonCoordinateElements makeUnevaluatedInstance() {
        return new MoonCoordinateElements();
    }

    private MoonCoordinateElements() {}

    /**
     * Gets the value: one of intermediate arguments used in periodic terms for the Moon's coordinates (L', D, M, M', F, A1, A2, A3): [0, 2π).
     *
     * @param index     from 0, lesser than {@value #ELEMENTS_N}
     * @return          value: one of intermediate arguments used in periodic terms for the Moon's coordinates (L', D, M, M', F, A1, A2, A3): [0, 2π)
     */
    public double getValue(int index) {
        return values[index];
    }

    /**
     * Gets the value: the Moon's mean longitude (L'): [0, 2π)
     * @return  value: the Moon's mean longitude (L'): [0, 2π)
     */
    public double getLPrim() {
        return values[INDEX_MEAN_LONGITUDE];
    }

    /**
     * Gets the value: the Moon's mean elongation (D): [0, 2π)
     * @return  value: the Moon's mean elongation (D): [0, 2π)
     */
    public double getD() {
        return values[INDEX_MEAN_ELONGATION];
    }

    /**
     * Gets the value: the Sun's mean anomaly (M): [0, 2π)
     * @return  value: the Sun's mean anomaly (M): [0, 2π)
     */
    public double getM() {
        return values[INDEX_MEAN_ANOMALY_OF_SUN];
    }

    /**
     * Gets the value: the Moon's mean anomaly (M'): [0, 2π)
     * @return  value: the Moon's mean anomaly (M'): [0, 2π)
     */
    public double getMPrim() {
        return values[INDEX_MEAN_ANOMALY];
    }

    /**
     * Gets the value: Moon's argument of latitude (F): [0, 2π)
     * @return  value: Moon's argument of latitude (F): [0, 2π)
     */
    public double getF() {
        return values[INDEX_MEAN_DISTANCE_FROM_ASCENDING_NODE];
    }

    /**
     * Gets the value: additional argument due to the action of Venus (A1): [0, 2π)
     * @return  value: additional argument due to the action of Venus (A1): [0, 2π)
     */
    public double getA1() {
        return values[INDEX_ADDITIONAL_ARGUMENT_VENUS];
    }

    /**
     * Gets the value: additional argument due to the action of Jupiter (A2): [0, 2π)
     * @return  value: additional argument due to the action of Jupiter (A2): [0, 2π)
     */
    public double getA2() {
        return values[INDEX_ADDITIONAL_ARGUMENT_JUPITER];
    }

    /**
     * Gets the value: additional argument (A3): [0, 2π)
     * @return  value: additional argument (A3): [0, 2π)
     */
    public double getA3() {
        return values[INDEX_ADDITIONAL_ARGUMENT_3];
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
    public MoonCoordinateElements calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        calculate(tx);
        return this;
    }

    @Override
    public Provider<Subject, TimelinePoint> getInstanceForNewComposition() {
        return makeUnevaluatedInstance();
    }
}
