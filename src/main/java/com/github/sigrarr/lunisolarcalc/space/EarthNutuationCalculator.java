package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.earthnutuationcalculator.*;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;

public class EarthNutuationCalculator {

    private PeriodicTermsForNutuation periodicTerms;

    public EarthNutuationCalculator(PeriodicTermsForNutuation periodicTerms) {
        this.periodicTerms = periodicTerms;
    }

    /**
     * Meeus 1998, Ch. 22, p. 143-144
     */
    public double calculateNutuation(double centurialT) {
        return periodicTerms.evaluateSeries(centurialT, new EarthNutuationElementsVector(centurialT));
    }
}
