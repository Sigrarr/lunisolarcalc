package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.function.*;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder.CalculationLimitExceededException;
import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder.MeanPrecisionSettingTooLowException;

public abstract class CyclicPhenomenonFinderAbstract {

    public static interface StageIndicatingAngleCalculator {
        public double calculateAngle(double julianEphemerisDay);
    }

    public static final int DEFAULT_MEAN_PRECISION_SECONDS = 15;
    public static final int DEFAULT_CORE_CALCULATIONS_LIMIT = 15;

    private final StageIndicatingAngleCalculator coreCalculator;
    private int coreCalculationsLimit = DEFAULT_CORE_CALCULATIONS_LIMIT;
    private int coreCalculationsCount = 0;

    public CyclicPhenomenonFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
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

    protected final double calculateStageIndicatingAngle(double julianEphemerisDay) {
        if (!canCalculateFurther()) {
            throw new CalculationLimitExceededException(this);
        }
        coreCalculationsCount++;
        return coreCalculator.calculateAngle(julianEphemerisDay);
    }

    protected void validateMeanPrecisionSeconds(int meanPrecisionSeconds) {
        if (meanPrecisionSeconds < 1) {
            throw new MeanPrecisionSettingTooLowException(meanPrecisionSeconds);
        }
    }

    abstract protected double getMeanPrecisionRadians(int meanPrecisionSeconds);

    abstract protected class ResultSupplierAbstract<PhT extends Enum<PhT>> implements DoubleSupplier, Supplier<FoundCyclicPhenomenon<PhT>> {
        final double meanPrecisionRadians;
        final List<PhT> orderedStagesInScope;
        Iterator<PhT> phIterator;
        PhT currentStage;

        protected ResultSupplierAbstract(List<PhT> orderedStagesInScope, int meanPrecisionSeconds) {
            this.orderedStagesInScope = orderedStagesInScope;
            this.phIterator = orderedStagesInScope.listIterator();
            meanPrecisionRadians = getMeanPrecisionRadians(meanPrecisionSeconds);
        }

        @Override
        public FoundCyclicPhenomenon<PhT> get() {
            return new FoundCyclicPhenomenon<>(getAsDouble(), currentStage);
        }

        void forward() {
            if (!phIterator.hasNext()) {
                rewindStage();
            }
            currentStage = phIterator.next();
        }

        void rewindStage() {
            phIterator = orderedStagesInScope.listIterator();
        }
    }
}
