package com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.CyclicPhenomenonFinderAbstract.StageIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public final class SeparateCompositionApparentLongitudeCalculator extends SingleOutputComposition<Subject, TimelinePoint> implements StageIndicatingAngleCalculator {

    public SeparateCompositionApparentLongitudeCalculator() {
        super(Composer.get().compose(Subject.SUN_APPARENT_LONGITUDE));
    }

    @Override
    public double calculateAngle(double julianEphemerisDay) {
        return (Double) calculate(new TimelinePoint(julianEphemerisDay, TimeType.DYNAMICAL));
    }
}
