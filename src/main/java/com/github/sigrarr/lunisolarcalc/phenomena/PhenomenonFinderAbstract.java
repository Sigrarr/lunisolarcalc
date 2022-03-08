package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.function.*;

import com.github.sigrarr.lunisolarcalc.phenomena.phenomenonfinder.CalculationLimitExceededException;

public abstract class PhenomenonFinderAbstract {

    public static interface InstantIndicatingAngleCalculator {
        public double calculateAngle(double julianEphemerisDay);
    }

    public static final int DEFAULT_MEAN_PRECISION_SECONDS = 15;
    public static final int DEFAULT_CORE_CALCULATIONS_LIMIT = 15;

    private final InstantIndicatingAngleCalculator coreCalculator;
    private int coreCalculationsLimit = DEFAULT_CORE_CALCULATIONS_LIMIT;
    private int coreCalculationsCount = 0;

    public PhenomenonFinderAbstract(InstantIndicatingAngleCalculator coreCalculator) {
        this.coreCalculator = coreCalculator;
    }

    public void setCoreCalculationsLimit(int limit) {
        this.coreCalculationsLimit = limit;
    }

    public int getCoreCalculationsLimit() {
        return coreCalculationsLimit;
    }

    protected void resetFinding() {
        coreCalculationsCount = 0;
    }

    protected final boolean canCalculateFurther() {
        return coreCalculationsCount < coreCalculationsLimit;
    }

    protected final double calculateInstantIndicatingAngle(double julianEphemerisDay) {
        if (!canCalculateFurther()) {
            throw new CalculationLimitExceededException(this);
        }
        coreCalculationsCount++;
        return coreCalculator.calculateAngle(julianEphemerisDay);
    }

    abstract protected double getMeanPrecisionRadians(int meanPrecisionSeconds);

    abstract protected class ResultSupplierAbstract<PhT extends Enum<PhT>> implements DoubleSupplier, Supplier<FoundPhenomenon<PhT>> {
        final double meanPrecisionRadians;
        final List<PhT> instants;
        Iterator<PhT> phIterator;
        PhT currentInstant;

        protected ResultSupplierAbstract(List<PhT> orderedInstants, int meanPrecisionSeconds) {
            this.instants = orderedInstants;
            this.phIterator = orderedInstants.listIterator();
            meanPrecisionRadians = getMeanPrecisionRadians(meanPrecisionSeconds);
        }

        @Override
        public FoundPhenomenon<PhT> get() {
            return new FoundPhenomenon<>(getAsDouble(), currentInstant);
        }

        void forward() {
            if (!phIterator.hasNext()) {
                rewindInstant();
            }
            currentInstant = phIterator.next();
        }

        void rewindInstant() {
            phIterator = instants.listIterator();
        }
    }
}
