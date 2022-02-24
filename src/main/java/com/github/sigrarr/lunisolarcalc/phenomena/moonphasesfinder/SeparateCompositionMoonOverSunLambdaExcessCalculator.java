package com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.MoonPhaseFinder.MoonOverSunLambdaExcessCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionMoonOverSunLambdaExcessCalculator extends SingleOutputComposition<Subject, Double> implements MoonOverSunLambdaExcessCalculator {

    public SeparateCompositionMoonOverSunLambdaExcessCalculator() {
        super(Composer.get().compose(Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS));
    }

    @Override
    public double calculateExcess(double julianEphemerisDay) {
        return (Double) calculate(Timeline.julianDayToCenturialT(julianEphemerisDay));
    }
}
