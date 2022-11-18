package com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.CyclicPhenomenonFinderAbstract;

public final class CalculationLimitExceededException extends RuntimeException {

    private final CyclicPhenomenonFinderAbstract finder;

    public CalculationLimitExceededException(CyclicPhenomenonFinderAbstract finder) {
        super(
            "Calculation limit exceeded (" + finder.getCoreCalculationsLimit() + ")."
            + " Set higher limit or lowen your accuracy expectation (set greater epsilon). "
            + finder.getClass().getName() + " should call " + CyclicPhenomenonFinderAbstract.class.getSimpleName() + ".resetFinding()"
            + " before the first calculation of a single result."
        );
        this.finder = finder;
    }

    public int getLimit() {
        return finder.getCoreCalculationsLimit();
    }
}
