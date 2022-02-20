package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public abstract class EarthNutuationCalculator implements Provider<Subject, Double, Double> {

    protected EarthNutuationPeriodicTerms periodicTerms;

    public EarthNutuationCalculator(EarthNutuationPeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, Ch. 22, p. 143-144
     */
    public double calculateNutuation(double centurialT, EarthNutuationElements elements) {
        return periodicTerms.evaluate(centurialT, elements);
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(Subject.EARTH_NUTUATION_ELEMENTS);
    }

    @Override
    public Object calculate(Double centurialT, Map<Subject, Object> arguments) {
        return calculateNutuation(centurialT, (EarthNutuationElements) arguments.get(Subject.EARTH_NUTUATION_ELEMENTS));
    }
}
