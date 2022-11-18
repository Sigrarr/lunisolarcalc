package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public abstract class MoonCoordinateCalculator implements Provider<Subject, TimelinePoint> {

    protected MoonCoordinatePeriodicTerms periodicTerms;

    public MoonCoordinateCalculator(MoonCoordinatePeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    public double calculateCoordinate(TimelinePoint tx, MoonCoordinateElements elements) {
        return periodicTerms.evaluate(tx.toDynamicalTime(), elements);
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_COORDINATE_ELEMENTS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateCoordinate(tx, (MoonCoordinateElements) calculatedValues.get(Subject.MOON_COORDINATE_ELEMENTS));
    }
}
