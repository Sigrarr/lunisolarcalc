package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public abstract class EarthNutuationCalculator implements Provider<Subject, TimelinePoint> {

    protected EarthNutuationPeriodicTerms periodicTerms;

    public EarthNutuationCalculator(EarthNutuationPeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, Ch. 22, p. 143-144
     */
    public double calculateNutuation(TimelinePoint tx, EarthNutuationElements elements) {
        return periodicTerms.evaluate(tx, elements);
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_NUTUATION_ELEMENTS);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> calculatedValues) {
        return calculateNutuation(tx, (EarthNutuationElements) calculatedValues.get(Subject.EARTH_NUTUATION_ELEMENTS));
    }
}
