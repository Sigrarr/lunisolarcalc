package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator.*;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;

public abstract class EarthNutuationCalculator {

    protected EarthNutuationPeriodicTerms periodicTerms;

    public EarthNutuationCalculator(EarthNutuationPeriodicTerms periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, Ch. 22, p. 143-144
     */
    public double calculateNutuation(double centurialT) {
        return periodicTerms.evaluate(centurialT, new EarthNutuationElementsVector(centurialT));
    }
}
