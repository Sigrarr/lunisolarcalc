package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public abstract class MoonCoordinateCalculator implements Provider<Subject, Double> {

    protected MoonCoordinatePeriodicTerms periodicTerms;

    public MoonCoordinateCalculator(MoonCoordinatePeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    public double calculateCoordinate(double centurialT, MoonCoordinateElements elements) {
        return periodicTerms.evaluate(centurialT, elements);
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.MOON_COORDINATE_ELEMENTS);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> requiredArguments) {
        return calculateCoordinate(centurialT, (MoonCoordinateElements) requiredArguments.get(Subject.MOON_COORDINATE_ELEMENTS));
    }
}
