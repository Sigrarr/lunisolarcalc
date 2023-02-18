package com.github.sigrarr.lunisolarcalc.phenomena.global;

import java.util.*;
import java.util.function.*;

import com.github.sigrarr.lunisolarcalc.phenomena.DynamicalOccurrence;
import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinders.*;
import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.*;
import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

abstract class CyclicPhenomenonFinderAbstract {
    /**
     * Minimal allowed value of {@linkplain #getPrecision() angular precision}, in radians.
     */
    public static final double MIN_PRECISION_RADIANS = Calcs.EPSILON;
    /**
     * {@linkplain #getPrecisionTimeSeconds() Time equivalent} of the default {@linkplain #getPrecision() angular precision}, in seconds.
     */
    public static final int DEFAULT_PRECISION_TIME_SECONDS = 1;
    /**
     * The default {@linkplain #getCoreCalculationsLimit() limit} for number of core calculations of a stage-indicating angle per result.
     */
    public static final int DEFAULT_CORE_CALCULATIONS_LIMIT = 10;

    private final StageIndicatingAngleCalculator coreCalculator;
    private double precisionRadians;
    private int coreCalculationsLimit = DEFAULT_CORE_CALCULATIONS_LIMIT;
    private int coreCalculationsInCurrentFindingCount = 0;
    private int totalCoreCalculationsCount = 0;
    private int totalFindingsCount = 0;

    public CyclicPhenomenonFinderAbstract(StageIndicatingAngleCalculator coreCalculator) {
        this.coreCalculator = coreCalculator;
        setPrecisionTime(DEFAULT_PRECISION_TIME_SECONDS);
    }

    /**
     * Gets the angular precision, in radians.
     *
     * This setting determines the results' proximity
     * to the best values achievable by this object's core calculator.
     * While searching, a time argument of a newly calculated value of stage-indicating angle
     * is accepted as a result iff it differs by less than 'angular precision'
     * from the value indicating the stage which is currently under search;
     * otherwise a time argument is corrected, then stage-indicating angle recalculated.
     *
     * A smaller value implies better precision,
     * but also a higher mean number of core calculations needed to obtain a result.
     *
     * @return  the angular precision value, in radians
     * @see     #DEFAULT_PRECISION_TIME_SECONDS
     */
    public double getPrecision() {
        return precisionRadians;
    }

    /**
     * Gets the time equivalent of the {@linkplain #getPrecision() angular precision}, in seconds.
     * This is a mean (epochal) time corresponding to the vlaue of stage-indicating angle equal to
     * the precision setting in the cycle which is measured with this angle,
     * whose phenomena under search are stages of.
     *
     * @return  time equivalent of the {@linkplain #getPrecision() angular precision}, in seconds
     * @see     #DEFAULT_PRECISION_TIME_SECONDS
     * @see     MeanCycle
     */
    public double getPrecisionTimeSeconds() {
        return getMeanCycle().secondsPerRadians(precisionRadians);
    }

    /**
     * Sets the {@linkplain #getPrecision() angular precision}.
     *
     * @param radians   new value of angular precision, in radians, not lesser than {@value #MIN_PRECISION_RADIANS}
     * @see             #DEFAULT_PRECISION_TIME_SECONDS
     */
    public void setPrecision(double radians) {
        validatePrecisionRadians(radians);
        precisionRadians = radians;
    }

    /**
     * Sets the {@linkplain #getPrecision() angular precision} with its {@linkplain #getPrecisionTimeSeconds() time equivalent},
     * a positive number of seconds.
     *
     * @param seconds   {@linkplain #getPrecisionTimeSeconds() time equivalent} of the new {@linkplain #getPrecision() precision delta},
     *                  in seconds, a positive number
     * @see             #DEFAULT_PRECISION_TIME_SECONDS
     */
    public void setPrecisionTime(int seconds) {
        validatePrecisionTimeSeconds(seconds);
        precisionRadians = getMeanCycle().radiansPerTimeSeconds(seconds);
    }

    /**
     * Gets the limit for number of core calculations of stage-indicating angle
     * to perform in order to find a single result.
     * If too low (not adequate to a small {@linkplain #getPrecision() angular delta}),
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

    protected final void validatePrecisionRadians(double radians) {
        if (radians < MIN_PRECISION_RADIANS)
            throw new PrecisionAngleTooSmallException(radians, MIN_PRECISION_RADIANS);
    }

    protected final void validatePrecisionTimeSeconds(int seconds) {
        if (seconds < 1)
            throw new PrecisionTimeNotPositiveException(seconds);
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
            composedCalculator = CoordsCalcCompositions.compose(angleSubject);
        }

        @Override
        public double calculateAngle(double julianEphemerisDay) {
            return (Double) composedCalculator.calculate(new DynamicalTimelinePoint(julianEphemerisDay));
        }
    }
}
