package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public abstract class HeliocentricCoordinateCalculator implements Provider<Subject, TimelinePoint> {

    protected HeliocentricCoordinatePeriodicTerms periodicTerms;

    public HeliocentricCoordinateCalculator(HeliocentricCoordinatePeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, 32.2, p. 218
     */
    public double calculateCoordinate(TimelinePoint tx) {
        return periodicTerms.evaluate(tx);
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.noneOf(Subject.class);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateCoordinate(tx);
    }
}
