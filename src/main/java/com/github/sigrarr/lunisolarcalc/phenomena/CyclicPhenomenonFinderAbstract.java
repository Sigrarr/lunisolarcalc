package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.function.*;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinders.*;
import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.*;
import com.github.sigrarr.lunisolarcalc.spacebytime.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

abstract class CyclicPhenomenonFinderAbstract {
    /**
     * Minimal allowed value of {@linkplain #getAngularDelta() angular delta}, in radians.
     */
    public static final double MIN_ANGULAR_DELTA_RADIANS = Calcs.EPSILON;
    /**
     * {@linkplain #getAngularDeltaTimeSeconds() Time equivalent} of the default {@linkplain #getAngularDelta() angular delta}, in seconds.
     */
    public static final int DEFAULT_ANGULAR_DELTA_TIME_SECONDS = 1;
    /**
     * The default {@linkplain #getCoreCalculationsLimit() limit} for number of core calculations of a stage-indicating angle per result.
     */
    public static final int DEFAULT_CORE_CALCULATIONS_LIMIT = 10;

    private final StageIndicatingAngleCalculator coreCalculator;
    private double deltaRadians;
    private int coreCalculationsLimit = DEFAULT_CORE_CALCULATIONS_LIMIT;
    private int coreCalculationsInCurrentFindingCount = 0;
    private int totalCoreCalculationsCount = 0;
    private int totalFindingsCount = 0;

    public CyclicPhenomenonFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
        this.coreCalculator = coreCalculator;
        setAngularDeltaTime(DEFAULT_ANGULAR_DELTA_TIME_SECONDS);
    }

    /**
     * Gets the angular delta, in radians.
     *
     * This settings determines the precision of searching:
     * proximity to the best values achievable by this object's core calculator.
     * While searching, a time argument of a newly calculated value of stage-indicating angle
     * is accepted as a result iff it differs by less than delta
     * from the value indicating the stage which is currently under search;
     * otherwise a time argument is corrected, then stage-indicating angle recalculated.
     *
     * The smaller the delta, the better the results' precision
     * but also the higher mean number of core calculations needed to get them.
     *
     * @return  value of angular delta, in radians
     * @see     #DEFAULT_ANGULAR_DELTA_TIME_SECONDS
     */
    public double getAngularDelta() {
        return deltaRadians;
    }

    /**
     * Gets the time equivalent of the {@linkplain #getAngularDelta() angular delta}, in seconds.
     * This is a mean (epochal) time corresponding to the vlaue of stage-indicating angle equal to delta
     * in the cycle which is measured with this angle, whose phenomena under search are stages of.
     *
     * @return  time equivalent of the {@linkplain #getAngularDelta() angular delta}, in seconds
     * @see     #DEFAULT_ANGULAR_DELTA_TIME_SECONDS
     * @see     MeanCycle
     */
    public double getAngularDeltaTimeSeconds() {
        return getMeanCycle().secondsPerRadians(deltaRadians);
    }

    /**
     * Sets the {@linkplain #getAngularDelta() angular delta}.
     *
     * @param radians   new value of angular delta, in radians, not lesser than {@value #MIN_ANGULAR_DELTA_RADIANS}
     * @see             #DEFAULT_ANGULAR_DELTA_TIME_SECONDS
     */
    public void setAngularDelta(double radians) {
        validateDeltaRadians(radians);
        deltaRadians = radians;
    }

    /**
     * Sets the {@linkplain #getAngularDelta() angular delta} by its {@linkplain #getAngularDeltaTimeSeconds() time equivalent},
     * a positive number of seconds.
     *
     * @param seconds   {@linkplain #getAngularDeltaTimeSeconds() time equivalent} of the new {@linkplain #getAngularDelta() angular delta},
     *                  in seconds, a positive number
     * @see             #DEFAULT_ANGULAR_DELTA_TIME_SECONDS
     */
    public void setAngularDeltaTime(int seconds) {
        validateEpsilonTimeSeconds(seconds);
        deltaRadians = getMeanCycle().radiansPerTimeSeconds(seconds);
    }

    /**
     * Gets the limit for number of core calculations of stage-indicating angle
     * to perform in order to find a single result.
     * If too low (not adequate to a small {@linkplain #getAngularDelta() angular delta}),
     * the limit may be exceeded, so a {@link CalculationLimitExceededException} will be thrown.
     *
     * @return  limit for number of core calculations of stage-indicating angle
     *          to perform in order to find a single result
     * @see     #DEFAULT_CORE_CALCULATIONS_LIMIT
     */
    public int getCoreCalculationsLimit() {
        return coreCalculationsLimit;
    }

    /**
     * Sets the {@linkplain #getCoreCalculationsLimit() limit} for number of core calculations per result.
     *
     * @param limit new {@linkplain #getCoreCalculationsLimit() limit} for number of core calculations per result,
     *              only a positive number makes sense
     * @see         #DEFAULT_CORE_CALCULATIONS_LIMIT
     */
    public void setCoreCalculationsLimit(int limit) {
        this.coreCalculationsLimit = limit;
    }

    /**
     * Gets the total number of core calculations of stage-indicating angle
     * performed by this object.
     *
     * @return  total number of core calculations of stage-indicating angle
     *          performed by this object
     */
    public int getTotalCoreCalculationsCount() {
        return totalCoreCalculationsCount;
    }

    /**
     * Gets the total number of findings that this object has proceeded.
     * Includes uncompleted findings, which haven't produced results
     * (broken by an exception, for example), but in a normal situation
     * this is the total number of found results.
     *
     * @return  total number of findings this object has proceeded
     *          (including uncompleted ones)
     */
    public int getTotalFindingsCount() {
        return totalFindingsCount;
    }

    protected final double calculateStageIndicatingAngle(double julianEphemerisDay) {
        if (!canCalculateFurther()) {
            throw new CalculationLimitExceededException(coreCalculationsLimit);
        }
        coreCalculationsInCurrentFindingCount++;
        totalCoreCalculationsCount++;
        return coreCalculator.calculateAngle(julianEphemerisDay);
    }

    protected void resetFinding() {
        coreCalculationsInCurrentFindingCount = 0;
        totalFindingsCount++;
    }

    protected final boolean canCalculateFurther() {
        return coreCalculationsInCurrentFindingCount < coreCalculationsLimit;
    }

    protected abstract MeanCycle getMeanCycle();

    protected final void validateDeltaRadians(double radians) {
        if (radians < MIN_ANGULAR_DELTA_RADIANS)
            throw new DeltaAngleTooSmallException(radians, MIN_ANGULAR_DELTA_RADIANS);
    }

    protected final void validateEpsilonTimeSeconds(int seconds) {
        if (seconds < 1)
            throw new DeltaTimeNotPositiveException(seconds);
    }

    abstract protected class ResultSupplierAbstract<PhT extends Enum<PhT>> implements DoubleSupplier, Supplier<DynamicalOccurrence<PhT>> {
        final List<PhT> orderedStagesInScope;
        Iterator<PhT> stageIterator;
        PhT currentStage;

        protected ResultSupplierAbstract(List<PhT> orderedStagesInScope) {
            this.orderedStagesInScope = orderedStagesInScope;
            this.stageIterator = orderedStagesInScope.listIterator();
        }

        @Override
        public DynamicalOccurrence<PhT> get() {
            return new DynamicalOccurrence<>(getAsDouble(), currentStage);
        }

        void forward() {
            if (!stageIterator.hasNext()) {
                rewindStage();
            }
            currentStage = stageIterator.next();
        }

        void rewindStage() {
            stageIterator = orderedStagesInScope.listIterator();
        }
    }

    protected static final class OwnCompositionStageIndicatingAngleCalculator implements StageIndicatingAngleCalculator {

        final SingleOutputComposition<Subject, TimelinePoint> composedCalculator;

        OwnCompositionStageIndicatingAngleCalculator(Subject angleSubject) {
            composedCalculator = SpaceByTimeCalcCompositions.compose(angleSubject);
        }

        @Override
        public double calculateAngle(double julianEphemerisDay) {
            return (Double) composedCalculator.calculate(new DynamicalTimelinePoint(julianEphemerisDay));
        }
    }
}
