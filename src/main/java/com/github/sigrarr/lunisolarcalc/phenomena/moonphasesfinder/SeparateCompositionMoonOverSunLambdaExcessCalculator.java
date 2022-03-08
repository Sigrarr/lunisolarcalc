package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.PhenomenonFinderAbstract.InstantIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionMoonOverSunLambdaExcessCalculator extends SingleOutputComposition<Subject, Double> implements InstantIndicatingAngleCalculator {

    public SeparateCompositionMoonOverSunLambdaExcessCalculator() {
        super(Composer.get().compose(Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS));
    }

    @Override
    public double calculateAngle(double julianEphemerisDay) {
        return (Double) calculate(Timeline.julianDayToCenturialT(julianEphemerisDay));
    }
}