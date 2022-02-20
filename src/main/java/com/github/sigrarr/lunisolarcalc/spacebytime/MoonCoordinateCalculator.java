package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public abstract class MoonCoordinateCalculator implements Provider<Subject, Double, Double> {

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
    public Object calculate(Double centurialT, Map<Subject, Object> arguments) {
        return calculateCoordinate(centurialT, (MoonCoordinateElements) arguments.get(Subject.MOON_COORDINATE_ELEMENTS));
    }
}
