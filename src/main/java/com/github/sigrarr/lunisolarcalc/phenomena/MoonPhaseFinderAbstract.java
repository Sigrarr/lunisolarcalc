package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.*;

import com.github.sigrarr.lunisolarcalc.phenomena.moonphasesfinder.MeanMoonPhaseApproximator;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.Lunation;

abstract class MoonPhaseFinderAbstract extends PhenomenonFinderAbstract {

    public final MeanMoonPhaseApproximator approximator = new MeanMoonPhaseApproximator();

    public MoonPhaseFinderAbstract(InstantIndicatingAngleCalculator excessCalculator) {
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

    public FoundPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman) {
        return findAround(roman, EnumSet.allOf(MoonPhase.class));
    }

    public FoundPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman, EnumSet<MoonPhase> phases) {
        return findAround(roman, phases, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public FoundPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman, int meanPrecisionSeconds) {
        return findAround(roman, EnumSet.allOf(MoonPhase.class), meanPrecisionSeconds);
    }

    public FoundPhenomenon<MoonPhase> findAround(RomanCalendarPoint roman, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        double baseJde = Time.julianDayToEphemeris(Timeline.romanCalendarToJulianDay(roman));
        FoundPhenomenon<MoonPhase> closeApproximate = phases.stream()
            .map(ph -> new FoundPhenomenon<>(approximator.approximateJulianEphemerisDayAround(roman, ph), ph))
            .min((f1, f2) -> Double.compare(Math.abs(f1.julianEphemerisDay - baseJde), Math.abs(f2.julianEphemerisDay - baseJde)))
            .get();
        return new FoundPhenomenon<>(
            findJulianEphemerisDay(closeApproximate.julianEphemerisDay, closeApproximate.instant, meanPrecisionSeconds),
            closeApproximate.instant
        );
    }

    public Stream<FoundPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int instantsLimit) {
        return findMany(startAroundPoint, instantsLimit, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int instantsLimit, EnumSet<MoonPhase> phases) {
        return findMany(startAroundPoint, instantsLimit, phases, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public Stream<FoundPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int instantsLimit, int meanPrecisionSeconds) {
        return findMany(startAroundPoint, instantsLimit, EnumSet.allOf(MoonPhase.class), meanPrecisionSeconds);
    }

    public Stream<FoundPhenomenon<MoonPhase>> findMany(RomanCalendarPoint startAroundPoint, int instantsLimit, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        return Stream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases, meanPrecisionSeconds))
            .limit(instantsLimit);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int instantsLimit) {
        return findManyJulianEphemerisDays(startAroundPoint, instantsLimit, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int instantsLimit, EnumSet<MoonPhase> phases) {
        return findManyJulianEphemerisDays(startAroundPoint, instantsLimit, phases, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int instantsLimit, int meanPrecisionSeconds) {
        return findManyJulianEphemerisDays(startAroundPoint, instantsLimit, EnumSet.allOf(MoonPhase.class), meanPrecisionSeconds);
    }

    public DoubleStream findManyJulianEphemerisDays(RomanCalendarPoint startAroundPoint, int instantsLimit, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        return DoubleStream.generate(prepareResultSupplierWithInitialResult(startAroundPoint, phases, meanPrecisionSeconds))
            .limit(instantsLimit);
    }

    protected double findJulianEphemerisDay(double approximateJde, MoonPhase phase, int meanPrecisionSeconds) {
        return findJulianEphemerisDay(approximateJde, phase, getMeanPrecisionRadians(meanPrecisionSeconds));
    }

    protected abstract double findJulianEphemerisDay(double approximateJde, MoonPhase phase, double meanPrecisionRadians);

    @Override
    protected double getMeanPrecisionRadians(int seconds) {
        return Math.toRadians(Lunation.degreesPerTimeMiliseconds(1000 * seconds));
    }

    private ResultSupplier prepareResultSupplierWithInitialResult(RomanCalendarPoint startAroundPoint, EnumSet<MoonPhase> phases, int meanPrecisionSeconds) {
        FoundPhenomenon<MoonPhase> initial = findAround(startAroundPoint, phases, meanPrecisionSeconds);
        List<MoonPhase> orderedPhases = orderPhasesByCyclingToStartAtInitial(phases, initial.instant);
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

        final FoundPhenomenon<MoonPhase> initialResult;
        final Map<MoonPhase, Double> phaseToJde;
        MoonPhase previousInstant;
        boolean initialPending = true;

        ResultSupplier(FoundPhenomenon<MoonPhase> initialResult, List<MoonPhase> orderedPhases, int meanPrecisionSeconds) {
            super(orderedPhases, meanPrecisionSeconds);
            this.initialResult = initialResult;
            this.phaseToJde = new EnumMap<>(MoonPhase.class);
            phaseToJde.put(initialResult.instant, initialResult.julianEphemerisDay);
            currentInstant = phIterator.next();
        }

        @Override
        public double getAsDouble() {
            if (pullInitialPending())
                return initialResult.julianEphemerisDay;
            previousInstant = currentInstant;
            forward();
            double newValue = findJulianEphemerisDay(approximateJde(), currentInstant, meanPrecisionRadians);
            phaseToJde.put(currentInstant, newValue);
            return newValue;
        }

        @Override
        public FoundPhenomenon<MoonPhase> get() {
            return pullInitialPending() ? initialResult : super.get();
        }

        private double approximateJde() {
            if (phaseToJde.containsKey(currentInstant)) {
                return phaseToJde.get(currentInstant) + MeanValueApproximations.LUNATION_MEAN_DAYS;
            }
            return phaseToJde.get(previousInstant)
                + (MeanValueApproximations.LUNATION_MEAN_DAYS * Math.abs(currentInstant.lunationFraction - previousInstant.lunationFraction));
        }

        boolean pullInitialPending() {
            boolean answer = initialPending;
            initialPending = false;
            return answer;
        }
    }
}
