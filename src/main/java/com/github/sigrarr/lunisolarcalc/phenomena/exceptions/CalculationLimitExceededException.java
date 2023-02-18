package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

public final class CalculationLimitExceededException extends IllegalStateException {

    private final int limit;

    public CalculationLimitExceededException(int limit) {
        super(
            "Calculation limit exceeded (" + limit + ")."
            + " Set higher limit (if you need to keep your accuracy expectation), or loosen the precision setting."
        );
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
