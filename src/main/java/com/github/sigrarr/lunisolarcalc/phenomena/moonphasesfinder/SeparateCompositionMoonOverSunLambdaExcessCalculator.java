package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.CyclicPhenomenonFinderAbstract.StageIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionMoonOverSunLambdaExcessCalculator extends SingleOutputComposition<Subject, TimelinePoint> implements StageIndicatingAngleCalculator {

    public SeparateCompositionMoonOverSunLambdaExcessCalculator() {
        super(Composer.get().compose(Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS));
    }

    @Override
    public double calculateAngle(double julianEphemerisDay) {
        return (Double) calculate(new TimelinePoint(julianEphemerisDay, TimeType.DYNAMICAL));
    }
}
