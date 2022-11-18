package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.function.*;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public abstract class CyclicPhenomenonFinderAbstract {

    public static interface StageIndicatingAngleCalculator {
        public double calculateAngle(double julianEphemerisDay);
    }

    public static final double ANGULAR_EPSILON_MIN_RADIANS = Calcs.EPSILON;
    public static final int ANGULAR_EPSILON_TIME_SECONDS_DEFAULT = 1;
    public static final int CORE_CALCULATIONS_LIMIT_DEFAULT = 10;

    public final CycleTemporalApproximate cycleTemporalApproximate;
    private final StageIndicatingAngleCalculator coreCalculator;
    private double epsilonRadians;
    private int coreCalculationsLimit = CORE_CALCULATIONS_LIMIT_DEFAULT;
    private int coreCalculationsCount = 0;

    public CyclicPhenomenonFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
        this.coreCalculator = coreCalculator;
        cycleTemporalApproximate = getCycleTemporalApproximate();
        setAngularEpsilonTime(ANGULAR_EPSILON_TIME_SECONDS_DEFAULT);
    }

    public void setAngularEpsilon(double radians) {
        validateEpsilonRadians(radians);
        epsilonRadians = radians;
    }

    public void setAngularEpsilonTime(int seconds) {
        validateEpsilonTimeSeconds(seconds);
        epsilonRadians = cycleTemporalApproximate.radiansPerTimeSeconds(seconds);
    }

    public double getAngularEpsilon() {
        return epsilonRadians;
    }

    public double getAngularEpsilonTimeSeconds() {
        return cycleTemporalApproximate.secondsPerRadians(epsilonRadians);
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

    abstract protected CycleTemporalApproximate getCycleTemporalApproximate();

    protected void validateEpsilonRadians(double radians) {
        if (radians < ANGULAR_EPSILON_MIN_RADIANS) {
            throw new EpsilonAngleSettingTooLowException(radians);
        }
    }

    protected void validateEpsilonTimeSeconds(int seconds) {
        if (seconds < 1) {
            throw new EpsilonTimeSettingTooLowException(seconds);
        }
    }

    abstract protected class ResultSupplierAbstract<PhT extends Enum<PhT>> implements DoubleSupplier, Supplier<ResultCyclicPhenomenon<PhT>> {
        final List<PhT> orderedStagesInScope;
        Iterator<PhT> phIterator;
        PhT currentStage;

        protected ResultSupplierAbstract(List<PhT> orderedStagesInScope) {
            this.orderedStagesInScope = orderedStagesInScope;
            this.phIterator = orderedStagesInScope.listIterator();
        }

        @Override
        public ResultCyclicPhenomenon<PhT> get() {
            return new ResultCyclicPhenomenon<>(getAsDouble(), currentStage);
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
