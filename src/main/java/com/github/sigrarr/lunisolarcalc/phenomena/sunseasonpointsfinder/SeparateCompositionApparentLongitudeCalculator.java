package com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointsfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.SunSeasonPointsFinder.ApparentLongitudeCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionApparentLongitudeCalculator extends SingleOutputComposition<Subject, Double, Double> implements ApparentLongitudeCalculator {
    
    public SeparateCompositionApparentLongitudeCalculator() {
        super(Composer.get().compose(Subject.SUN_APPARENT_LONGITUDE));
    }

    @Override
    public double calculateLambda(double julianEphemerisDay) {
        return calculate(Timeline.julianDayToCenturialT(julianEphemerisDay));
    }
}
