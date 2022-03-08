package com.github.sigrarr.lunisolarcalc.phenomena.phenomenonfinder;

import com.github.sigrarr.lunisolarcalc.phenomena.PhenomenonFinderAbstract;

public final class CalculationLimitExceededException extends RuntimeException {

    private final PhenomenonFinderAbstract finder;

    public CalculationLimitExceededException(PhenomenonFinderAbstract finder) {
        super(
            "Calculation limit exceeded (" + finder.getCoreCalculationsLimit() + ")."
            + " Lowen your accuracy expectation or set higher limit, and make sure that "
            + finder.getClass().getName() + " calls " + PhenomenonFinderAbstract.class.getSimpleName() + ".resetFinding()"
            + " before the first calculation for an instant."
        );
        this.finder = finder;
    }

    public int getLimit() {
        return finder.getCoreCalculationsLimit();
    }
}
