package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.ROUND;

import com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public class MoonPhaseFinder extends MoonPhaseFinderAbstract {

    private DoubleStepPair excess = new DoubleStepPair();
    private DoubleStepPair jde = new DoubleStepPair();
    private double diff;

    public MoonPhaseFinder() {
        this(new SeparateCompositionMoonOverSunLambdaExcessCalculator());
    }

    public MoonPhaseFinder(StageIndicatingAngleCalculator moonOverSunLambdaExcessCalculator) {
        super(moonOverSunLambdaExcessCalculator);
    }

    @Override
    protected double findJulianEphemerisDay(double approximateJde, MoonPhase phase, double meanPrecisionRadians) {
        resetFinding();
        jde.push(approximateJde);
        excess.push(calculateMoonOverSunLambdaExcess());
        setDiffAndExcessProjectingOnContinuousLine(phase);

        while (Math.abs(diff) > meanPrecisionRadians) {
            jde.push(jde.getCurrent() + calculateJdeCorrection());
            excess.push(calculateMoonOverSunLambdaExcess());
            setDiffAndExcessProjectingOnContinuousLine(phase);
        }

        return jde.getCurrent();
    }

    protected double calculateMoonOverSunLambdaExcess() {
        return calculateStageIndicatingAngle(jde.getCurrent());
    }

    private double calculateJdeCorrection() {
        return diff * (excess.isComplete() ? estimateSlopeInverseFromRecentEvaluations() : cycleTemporalApproximate.getLengthDays() / ROUND);
    }

    private double estimateSlopeInverseFromRecentEvaluations() {
        return (jde.getCurrent() - jde.getPrevious()) / (excess.getCurrent() - excess.getPrevious());
    }

    private void setDiffAndExcessProjectingOnContinuousLine(MoonPhase phase) {
        diff = phase.moonOverSunApparentLongitudeExcess - excess.getCurrent();
        if (phase.moonOverSunApparentLongitudeExcess == 0.0 && diff < -0.75 * ROUND) {
            excess.setCurrent(excess.getCurrent() - ROUND);
            diff += ROUND;
        }
    }

    @Override
    protected void resetFinding() {
        super.resetFinding();
        jde.clear();
        excess.clear();
    }
}
