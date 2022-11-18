package com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.CyclicPhenomenonFinderAbstract.StageIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionStageIndicatingAngleCalculator implements StageIndicatingAngleCalculator {

    private final SingleOutputComposition<Subject, TimelinePoint> composition;

    public SeparateCompositionStageIndicatingAngleCalculator(Subject angleSubject) {
        composition = SpaceByTimeCalcComposition.compose(angleSubject);
    }

    @Override
    public double calculateAngle(double julianEphemerisDay) {
        return (Double) composition.calculate(TimelinePoint.ofJulianEphemerisDay(julianEphemerisDay));
    }

}
