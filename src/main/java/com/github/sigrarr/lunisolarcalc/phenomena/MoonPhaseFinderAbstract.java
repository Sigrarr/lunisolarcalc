package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder.MeanMoonPhaseApproximator;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.Lunation;

abstract class MoonPhaseFinderAbstract extends CyclicPhenomenonFinderAbstract {

    public final MeanMoonPhaseApproximator approximator = new MeanMoonPhaseApproximator();

    public MoonPhaseFinderAbstract(StageIndicatingAngleCalculator excessCalculator) {
        super(excessCalculator);
    }

    public double findJulianEphemerisDayAround(RomanCalendarPoint roman, MoonPhase phase) {
        return findJulianEphemerisDayAround(roman, phase, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public double findJulianEphemerisDayAround(double closeJulianEphemerisDay, MoonPhase phase) {
        return findJulianEphemerisDayAround(closeJulianEphemerisDay, phase, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public double findJulianEphemerisDayAround(RomanCalendarPoint roman, MoonPhase phase, int meanPresisionSeconds) {
        return findJulianEphemerisDayAround(approximator.approximateJulianEphemerisDayAround(roman, phase), phase, meanPresisionSeconds);
    }

    public double findJulianEphemerisDayAround(double closeJulianEphemerisDay, MoonPhase phase, int meanPresisionSeconds) {
        return findJulianEphemerisDay(closeJulianEphemerisDay, phase, meanPresisionSeconds);
    }

    public FoundCyclicPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman) {
        return findAround(roman, EnumSet.allOf(MoonPhase.class));
    }

    public FoundCyclicPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman, EnumSet<MoonPhase> phases) {
        return findAround(roman, phases, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public FoundCyclicPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman, int meanPrecisionSeconds) {
        return findAround(roman, EnumSet.allOf(MoonPhase.class), meanPrecisionSeconds);
    }

    public FoundCyclicPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        double baseJde = Timeline.romanCalendarToJulianEphemerisDay(roman);
        FoundCyclicPhenomenon<MoonPhase> closeApproximate = phases.stream()
            .map(ph -> new FoundCyclicPhenomenon<>(approximator.approximateJulianEphemerisDayAround(roman, ph), ph))
            .min((f1, f2) -> Double.compare(Math.abs(f1.julianEphemerisDay - baseJde), Math.abs(f2.julianEphemerisDay - baseJde)))
            .get();
        return new FoundCyclicPhenomenon<>(
            findJulianEphemerisDay(closeApproximate.julianEphemerisDay, closeApproximate.stage, meanPrecisionSeconds),
            closeApproximate.stage
        );
    }

    public Stream<FoundCyclicPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int resultLimit) {
        return findMany(startAroundPoint, resultLimit, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundCyclicPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int resultLimit, EnumSet<MoonPhase> phases) {
        return findMany(startAroundPoint, resultLimit, phases, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundCyclicPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int resultLimit, int meanPrecisionSeconds) {
        return findMany(startAroundPoint, resultLimit, EnumSet.allOf(MoonPhase.class), meanPrecisionSeconds);
    }

    public Stream<FoundCyclicPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int resultLimit, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        return Stream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases, meanPrecisionSeconds))
            .limit(resultLimit);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int resultLimit) {
        return findManyJulianEphemerisDays(startAroundPoint, resultLimit, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int resultLimit, EnumSet<MoonPhase> phases) {
        return findManyJulianEphemerisDays(startAroundPoint, resultLimit, phases, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int resultLimit, int meanPrecisionSeconds) {
        return findManyJulianEphemerisDays(startAroundPoint, resultLimit, EnumSet.allOf(MoonPhase.class), meanPrecisionSeconds);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int resultLimit, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        return DoubleStream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases, meanPrecisionSeconds))
            .limit(resultLimit);
    }

    protected double findJulianEphemerisDay(double approximateJde, MoonPhase phase, int meanPrecisionSeconds) {
        return findJulianEphemerisDay(approximateJde, phase, getMeanPrecisionRadians(meanPrecisionSeconds));
    }

    protected abstract double findJulianEphemerisDay(double approximateJde, MoonPhase phase, double meanPrecisionRadians);

    @Override
    protected double getMeanPrecisionRadians(int seconds) {
        validateMeanPrecisionSeconds(seconds);
        return Math.toRadians(Lunation.degreesPerTimeMiliseconds(1000 * seconds));
    }

    private ResultSupplier prepareResultSupplierWithInitialResult(RomanCalendarPoint startAroundPoint, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        FoundCyclicPhenomenon<MoonPhase> initial = findAround(startAroundPoint, phases, meanPrecisionSeconds);
        List<MoonPhase> orderedPhases = orderPhasesByCyclingToStartAtInitial(phases, initial.stage);
        return new ResultSupplier(initial, orderedPhases, meanPrecisionSeconds);
    }

    private List<MoonPhase> orderPhasesByCyclingToStartAtInitial(EnumSet<MoonPhase> phases, MoonPhase initialPhase) {
        return phases.stream()
            .map(ph -> new SimpleEntry<MoonPhase, Double>(ph, ph.lunationFraction + (ph.lunationFraction < initialPhase.lunationFraction ? 1.0 : 0.0)))
            .sorted(Entry.comparingByValue())
            .map(e -> e.getKey())
            .collect(Collectors.toCollection(() -> new ArrayList<>(phases.size())));
    }

    private class ResultSupplier extends ResultSupplierAbstract<MoonPhase> {

        final FoundCyclicPhenomenon<MoonPhase> initialResult;
        final Map<MoonPhase, Double> phaseToJde;
        MoonPhase previousStage;
        boolean initialPending = true;

        ResultSupplier(FoundCyclicPhenomenon<MoonPhase> initialResult, List<MoonPhase> orderedPhasesInScope, int meanPrecisionSeconds) {
            super(orderedPhasesInScope, meanPrecisionSeconds);
            this.initialResult = initialResult;
            this.phaseToJde = new EnumMap<>(MoonPhase.class);
            phaseToJde.put(initialResult.stage, initialResult.julianEphemerisDay);
            currentStage = phIterator.next();
        }

        @Override
        public double getAsDouble() {
            if (pullInitialPending())
                return initialResult.julianEphemerisDay;
            previousStage = currentStage;
            forward();
            double newValue = findJulianEphemerisDay(approximateJde(), currentStage, meanPrecisionRadians);
            phaseToJde.put(currentStage, newValue);
            return newValue;
        }

        @Override
        public FoundCyclicPhenomenon<MoonPhase> get() {
            return pullInitialPending() ? initialResult : super.get();
        }

        private double approximateJde() {
            if (phaseToJde.containsKey(currentStage)) {
                return phaseToJde.get(currentStage) + MeanValueApproximations.LUNATION_MEAN_DAYS;
            }
            return phaseToJde.get(previousStage)
                + (MeanValueApproximations.LUNATION_MEAN_DAYS * Math.abs(currentStage.lunationFraction - previousStage.lunationFraction));
        }

        boolean pullInitialPending() {
            boolean answer = initialPending;
            initialPending = false;
            return answer;
        }
    }
}
