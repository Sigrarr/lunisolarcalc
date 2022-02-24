package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.ROUND;

import com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder.*;
import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

public class MoonPhaseFinder {

    public static interface MoonOverSunLambdaExcessCalculator {
        public double calculateExcess(double julianEphemerisDay);
    }

    private static final double BASE_SLOPE_INVERSE = MeanValueApproximations.SYNODIC_MONTH_MEAN_DAYS / ROUND;   

    private final MeanMoonPhaseApproximator approximator = new MeanMoonPhaseApproximator();
    private final MoonOverSunLambdaExcessCalculator excessCalculator;

    private DoubleStepPair excess = new DoubleStepPair();
    private DoubleStepPair jde = new DoubleStepPair();
    private double diff;
    private int lastExcessCalculationCount = -1;

    public MoonPhaseFinder() {
        this(new SeparateCompositionMoonOverSunLambdaExcessCalculator());
    }

    public MoonPhaseFinder(MoonOverSunLambdaExcessCalculator excessCalculator) {
        this.excessCalculator = excessCalculator;
    }

    public double findJulianEphemerisDay(RomanCalendarPoint r, MoonPhase phase, double meanPrecisionRadians) {
        reset();
        jde.push(approximator.approximateJulianEphemerisDay(r, phase));
        excess.push(excessCalculator.calculateExcess(jde.getCurrent()));
        lastExcessCalculationCount++;
        setDiffAndExcessProjectingOnContinuousLine(phase);

        while (Math.abs(diff) > meanPrecisionRadians) {
            jde.push(jde.getCurrent() + calculateJDECorrection());
            excess.push(excessCalculator.calculateExcess(jde.getCurrent()));
            lastExcessCalculationCount++;
            setDiffAndExcessProjectingOnContinuousLine(phase);
        }

        return jde.getCurrent();
    }

    private double calculateJDECorrection() {
        return diff * (excess.isCompletePair() ? calculateSlopeInverseFromRecentEvaluations() : BASE_SLOPE_INVERSE);
    }

    private double calculateSlopeInverseFromRecentEvaluations() {
        return Math.abs((jde.getCurrent() - jde.getPrevious()) / (excess.getCurrent() - excess.getPrevious()));
    }

    private void setDiffAndExcessProjectingOnContinuousLine(MoonPhase phase) {
        diff = phase.moonOverSunApparentLongitudeExcess - excess.getCurrent();
        if (phase == MoonPhase.NEW_MOON && diff < -0.75 * ROUND) {
            excess.setCurrent(excess.getCurrent() - ROUND);
            diff += ROUND;
        }
    }

    private void reset() {
        jde.clear();
        excess.clear();
        lastExcessCalculationCount = 0;
    }

    public int getLastExcessCalculationsCount() {
        if (lastExcessCalculationCount < 0) {
            throw new IllegalStateException();
        }
        return lastExcessCalculationCount;
    }
}
