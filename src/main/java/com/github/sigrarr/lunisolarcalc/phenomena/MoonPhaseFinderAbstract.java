package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinders.StageIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.NoMoonPhaseResultAroundInScopeException;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianDayOutOfPeriodException;

abstract class MoonPhaseFinderAbstract extends CyclicPhenomenonFinderAbstract {

    protected final MoonPhaseApproximator approximator = new MoonPhaseApproximator();

    public MoonPhaseFinderAbstract(StageIndicatingAngleCalculator excessCalculator) {
        super(excessCalculator);
    }

    /**
     * Finds time of occurrence of the requested Moon's phase as close to the requested time as possible,
     * in Julian Ephemeris Day.
     * Note that the occurrence may be found before or after the time argument.
     *
     * @param tx        time argument to look around
     * @param phase     Moon's phase to look for
     * @return          time of the found instant, in Julian Ephemeris Day
     */
    public double findJulianEphemerisDayAround(TimelinePoint tx, MoonPhase phase) {
        return findJulianEphemerisDay(approximator.approximateJulianEphemerisDayAround(tx, phase), phase);
    }

    /**
     * Finds the occurrence of a principal Moon's phase as close to the requested time as possible.
     * Note that the occurrence may be found before or after the time argument.
     *
     * @param tx    time argument to look around
     * @return      found occurrence
     */
    public DynamicalOccurrence<MoonPhase> findAround(TimelinePoint tx) {
        return findAround(tx, EnumSet.allOf(MoonPhase.class));
    }

    /**
     * Finds the occurrence of the requested Moon's phase as close to the requested time as possible.
     * Note that the occurrence may be found before or after the time argument.
     *
     * @param tx        time argument to look around
     * @param phase     Moon's phase to look for
     * @return          found occurrence
     */
    public DynamicalOccurrence<MoonPhase> findAround(TimelinePoint tx, MoonPhase phase) {
        return findAround(tx, EnumSet.of(phase));
    }

    /**
     * Finds the occurrence of one of the specified Moon's phases as close to the requested time as possible.
     * Note that the occurrence may be found before or after the time argument.
     *
     * @param tx        time argument to look around
     * @param phases    Moon's phases to look for
     * @return          found occurrence
     */
    public DynamicalOccurrence<MoonPhase> findAround(TimelinePoint tx, EnumSet<MoonPhase> phases) {
        double baseJde = tx.toDynamicalTime().julianDay;
        Optional<DynamicalOccurrence<MoonPhase>> uncertainCloseApproximate = phases.stream()
            .map(ph -> {
                try {
                    return new DynamicalOccurrence<>(approximator.approximateJulianEphemerisDayAround(tx, ph), ph);
                } catch (JulianDayOutOfPeriodException outException) {
                    return null;
                }
            })
            .filter(ph -> ph != null)
            .min((f1, f2) -> Double.compare(Math.abs(f1.timelinePoint.julianDay - baseJde), Math.abs(f2.timelinePoint.julianDay - baseJde)));

        if (!uncertainCloseApproximate.isPresent()) {
            throw new NoMoonPhaseResultAroundInScopeException(tx, phases);
        }
        DynamicalOccurrence<MoonPhase> closeApproximate = uncertainCloseApproximate.get();

        return new DynamicalOccurrence<>(
            findJulianEphemerisDay(closeApproximate.timelinePoint.julianDay, closeApproximate.type),
            closeApproximate.type
        );
    }

    /**
     * Finds and streams subsequent occurrences of principal Moon's phases
     * starting from the closest possible to the requested time.
     * Note that the initial occurrence may be found before or after the time argument.
     *
     * @param startAroundPoint  time argument to start around
     * @return                  unterminated {@link Stream} of found occurrences
     */
    public Stream<DynamicalOccurrence<MoonPhase>> findMany(TimelinePoint startAroundPoint) {
        return findMany(startAroundPoint, EnumSet.allOf(MoonPhase.class));
    }

    /**
     * Finds and streams subsequent occurrences of the requested Moon's phase
     * starting from the closest possible to the requested time.
     * Note that the initial occurrence may be found before or after the time argument.
     *
     * @param startAroundPoint  time argument to start around
     * @param phase             Moon's phase to look for
     * @return                  unterminated {@link Stream} of found occurrences
     */
    public Stream<DynamicalOccurrence<MoonPhase>> findMany(TimelinePoint startAroundPoint, MoonPhase phase) {
        return findMany(startAroundPoint, EnumSet.of(phase));
    }

    /**
     * Finds and streams subsequent occurrences of requested Moon's phases
     * starting from the closest possible to the requested time.
     * Note that the initial occurrence may be found before or after the time argument.
     *
     * @param startAroundPoint  time argument to start around
     * @param phases            Moon's phases to look for
     * @return                  unterminated {@link Stream} of found occurrences
     */
    public Stream<DynamicalOccurrence<MoonPhase>> findMany(TimelinePoint startAroundPoint, EnumSet<MoonPhase> phases) {
        return Stream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases));
    }

    /**
     * Finds and streams times of subsequent occurrences of requested Moon's phases
     * starting from the closest possible to the requested time, in Julian Ephemeris Days.
     * Note that the initial occurrence may be found before or after the time argument.
     *
     * @param startAroundPoint  time argument to start around
     * @param phase             Moon's phase to look for
     * @return                  unterminated {@linkplain DoubleStream stream} of times of found occurrences,
     *                          in Julian Ephemeris Days
     */
    public DoubleStream findManyJulianEphemerisDays(TimelinePoint startAroundPoint, MoonPhase phase) {
        return DoubleStream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, EnumSet.of(phase)));
    }

    protected abstract double findJulianEphemerisDay(double closeJulianEphemerisDay, MoonPhase phase);

    @Override
    protected MeanCycle getMeanCycle() {
        return MeanCycle.LUNATION;
    }

    private ResultSupplier prepareResultSupplierWithInitialResult(TimelinePoint startAroundPoint, EnumSet<MoonPhase> phases) {
        DynamicalOccurrence<MoonPhase> initial = findAround(startAroundPoint, phases);
        List<MoonPhase> orderedPhases = orderPhasesByCyclingToStartAtInitial(phases, initial.type);
        return new ResultSupplier(initial, orderedPhases);
    }

    private List<MoonPhase> orderPhasesByCyclingToStartAtInitial(EnumSet<MoonPhase> phases, MoonPhase initialPhase) {
        return phases.stream()
            .map(ph -> new SimpleEntry<MoonPhase, Double>(ph, ph.lunationFraction + (ph.lunationFraction < initialPhase.lunationFraction ? 1.0 : 0.0)))
            .sorted(Entry.comparingByValue())
            .map(e -> e.getKey())
            .collect(Collectors.toCollection(() -> new ArrayList<>(phases.size())));
    }

    private class ResultSupplier extends ResultSupplierAbstract<MoonPhase> {

        final DynamicalOccurrence<MoonPhase> initialResult;
        final Map<MoonPhase, Double> stageToJde;
        MoonPhase previousStage;
        boolean initialPending = true;

        ResultSupplier(DynamicalOccurrence<MoonPhase> initialResult, List<MoonPhase> orderedStagesInScope) {
            super(orderedStagesInScope);
            this.initialResult = initialResult;
            this.stageToJde = new EnumMap<>(MoonPhase.class);
            stageToJde.put(initialResult.type, initialResult.timelinePoint.julianDay);
            currentStage = stageIterator.next();
        }

        @Override
        public double getAsDouble() {
            if (pullInitialPendingFlag())
                return initialResult.timelinePoint.julianDay;
            previousStage = currentStage;
            forward();
            double newValue = findJulianEphemerisDay(approximateJde(), currentStage);
            stageToJde.put(currentStage, newValue);
            return newValue;
        }

        @Override
        public DynamicalOccurrence<MoonPhase> get() {
            return pullInitialPendingFlag() ? initialResult : super.get();
        }

        private double approximateJde() {
            if (stageToJde.containsKey(currentStage)) {
                return stageToJde.get(currentStage) + MeanCycle.LUNATION.epochalLengthDays;
            }
            return stageToJde.get(previousStage)
                + (MeanCycle.LUNATION.epochalLengthDays * Math.abs(currentStage.lunationFraction - previousStage.lunationFraction));
        }

        boolean pullInitialPendingFlag() {
            boolean flag = initialPending;
            initialPending = false;
            return flag;
        }
    }
}
