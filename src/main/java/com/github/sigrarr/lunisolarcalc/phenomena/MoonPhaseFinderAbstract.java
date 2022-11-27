package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder.MoonPhaseApproximator;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

abstract class MoonPhaseFinderAbstract extends CyclicPhenomenonFinderAbstract {

    public final MoonPhaseApproximator approximator = new MoonPhaseApproximator();

    public MoonPhaseFinderAbstract(StageIndicatingAngleCalculator excessCalculator) {
        super(excessCalculator);
    }

    public double findJulianEphemerisDayAround(TimelinePoint tx, MoonPhase phase) {
        return findJulianEphemerisDay(approximator.approximateJulianEphemerisDayAround(tx, phase), phase);
    }

    public ResultCyclicPhenomenon<MoonPhase> findAround(TimelinePoint tx) {
        return findAround(tx, EnumSet.allOf(MoonPhase.class));
    }

    public ResultCyclicPhenomenon<MoonPhase> findAround(TimelinePoint tx, MoonPhase phase) {
        return new ResultCyclicPhenomenon<>(findJulianEphemerisDayAround(tx, phase), phase);
    }

    public ResultCyclicPhenomenon<MoonPhase> findAround(TimelinePoint tx, EnumSet<MoonPhase> phases) {
        double baseJde = tx.toDynamicalTime().julianDay;
        ResultCyclicPhenomenon<MoonPhase> closeApproximate = phases.stream()
            .map(ph -> new ResultCyclicPhenomenon<>(approximator.approximateJulianEphemerisDayAround(tx, ph), ph))
            .min((f1, f2) -> Double.compare(Math.abs(f1.ephemerisTimelinePoint.julianDay - baseJde), Math.abs(f2.ephemerisTimelinePoint.julianDay - baseJde)))
            .get();
        return new ResultCyclicPhenomenon<>(
            findJulianEphemerisDay(closeApproximate.ephemerisTimelinePoint.julianDay, closeApproximate.stage),
            closeApproximate.stage
        );
    }

    public Stream<ResultCyclicPhenomenon<MoonPhase>> findMany(TimelinePoint startAroundPoint) {
        return findMany(startAroundPoint, EnumSet.allOf(MoonPhase.class));
    }

    public Stream<ResultCyclicPhenomenon<MoonPhase>> findMany(TimelinePoint startAroundPoint, EnumSet<MoonPhase> phases) {
        return Stream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases));
    }

    public DoubleStream findManyJulianEphemerisDays(TimelinePoint startAroundPoint) {
        return findManyJulianEphemerisDays(startAroundPoint, EnumSet.allOf(MoonPhase.class));
    }

    public DoubleStream findManyJulianEphemerisDays(TimelinePoint startAroundPoint, EnumSet<MoonPhase> phases) {
        return DoubleStream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases));
    }

    protected abstract double findJulianEphemerisDay(double closeJulianEphemerisDay, MoonPhase phase);

    @Override
    protected CycleTemporalApproximate getCycleTemporalApproximate() {
        return MeanMotionApproximate.SYNODIC_MONTH;
    }

    private ResultSupplier prepareResultSupplierWithInitialResult(TimelinePoint startAroundPoint, EnumSet<MoonPhase> phases) {
        ResultCyclicPhenomenon<MoonPhase> initial = findAround(startAroundPoint, phases);
        List<MoonPhase> orderedPhases = orderPhasesByCyclingToStartAtInitial(phases, initial.stage);
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

        final ResultCyclicPhenomenon<MoonPhase> initialResult;
        final Map<MoonPhase, Double> phaseToJde;
        MoonPhase previousStage;
        boolean initialPending = true;

        ResultSupplier(ResultCyclicPhenomenon<MoonPhase> initialResult, List<MoonPhase> orderedPhasesInScope) {
            super(orderedPhasesInScope);
            this.initialResult = initialResult;
            this.phaseToJde = new EnumMap<>(MoonPhase.class);
            phaseToJde.put(initialResult.stage, initialResult.ephemerisTimelinePoint.julianDay);
            currentStage = phIterator.next();
        }

        @Override
        public double getAsDouble() {
            if (pullInitialPending())
                return initialResult.ephemerisTimelinePoint.julianDay;
            previousStage = currentStage;
            forward();
            double newValue = findJulianEphemerisDay(approximateJde(), currentStage);
            phaseToJde.put(currentStage, newValue);
            return newValue;
        }

        @Override
        public ResultCyclicPhenomenon<MoonPhase> get() {
            return pullInitialPending() ? initialResult : super.get();
        }

        private double approximateJde() {
            if (phaseToJde.containsKey(currentStage)) {
                return phaseToJde.get(currentStage) + MeanMotionApproximate.SYNODIC_MONTH.lengthDays;
            }
            return phaseToJde.get(previousStage)
                + (MeanMotionApproximate.SYNODIC_MONTH.lengthDays * Math.abs(currentStage.lunationFraction - previousStage.lunationFraction));
        }

        boolean pullInitialPending() {
            boolean answer = initialPending;
            initialPending = false;
            return answer;
        }
    }
}
