package com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.PhenomenonFinderAbstract.InstantIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionApparentLongitudeCalculator extends SingleOutputComposition<Subject, Double> implements InstantIndicatingAngleCalculator {

    public SeparateCompositionApparentLongitudeCalculator() {
        super(Composer.get().compose(Subject.SUN_APPARENT_LONGITUDE));
    }

    @Override
    public double calculateAngle(double julianEphemerisDay) {
        return (Double) calculate(Timeline.julianDayToCenturialT(julianEphemerisDay));
    }
}
