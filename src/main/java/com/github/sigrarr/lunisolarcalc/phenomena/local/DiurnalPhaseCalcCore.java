package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcDayValues.*;

import java.util.*;
import java.util.function.Supplier;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.phenomena.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.MultiOutputComposition;

abstract class DiurnalPhaseCalcCore implements Supplier<Optional<UniversalOccurrence<BodyDiurnalPhase>>> {

    final Body body = prepareBody();
    final FlexPentadBuffer<DiurnalPhaseCalcDayValues> dayValues = new FlexPentadBuffer<>();
    final MultiOutputComposition<Subject, TimelinePoint> coordsCalc = CoordsCalcCompositions.compose(EnumSet.of(
        body.declinationSubject, body.rightAscensionSubject, body.hourAngleSubject,
        Subject.EARTH_NUTUATION_IN_LONGITUDE, Subject.ECLIPTIC_TRUE_OBLIQUITY
    ));
    protected final DiurnalPhaseCalcCoordsCombiner coordsCombiner = new DiurnalPhaseCalcCoordsCombiner(this);
    protected final DiurnalPhaseCalcTransitResolver transitResolver = prepareTransitResolver();
    protected final DiurnalPhaseCalcExtremeApproximator extremeApproximator = prepareExtremeApproximator();
    protected final DiurnalPhaseCalcExtremeFinder extremeFinder = prepareExtremeFinder();
    private final DiurnalPhaseCalcProgressController progress = new DiurnalPhaseCalcProgressController(this);
    private DiurnalPhaseCalcRequest request;

    public void reset(DiurnalPhaseCalcRequest request) {
        this.request = request;
        progress.reset();
    }

    @Override
    public Optional<UniversalOccurrence<BodyDiurnalPhase>> get() {
        DiurnalPhase phase = progress.getCurrentPhase();
        Optional<UniversalTimelinePoint> transit = resolveTransit();
        Optional<UniversalTimelinePoint> result = transit.isPresent() ?
            (phase.isExtreme() ? resolveExtremePhase(phase) : transit)
            : Optional.empty();

        progress.phaseForward();

        return result.isPresent() ?
            Optional.of(new UniversalOccurrence<>(result.get(), BodyDiurnalPhase.of(body, phase)))
            : Optional.empty();
    }

    private Optional<UniversalTimelinePoint> resolveTransit() {
        DiurnalPhaseCalcDayValues dayValues = getDay(0);
        if (!dayValues.hasFinalizedTransit())
            dayValues.setFinalTransitByVectorFromNoon(transitResolver.findCentralNoonToTransitVector());
        return dayValues.getFinalTransit();
    }

    private Optional<UniversalTimelinePoint> resolveExtremePhase(DiurnalPhase phase) {
        OptionalDouble transitToApproximateVector = extremeApproximator.approximateVectorFromTransitToExtremePhase(phase.direction);
        if (!transitToApproximateVector.isPresent())
            return Optional.empty();
        OptionalDouble noonToPhaseVector = extremeFinder.findNoonToExtremePhaseVector(phase, transitToApproximateVector.getAsDouble());
        if (!noonToPhaseVector.isPresent())
            return Optional.empty();
        return Optional.of(getDay(0).noon.add(noonToPhaseVector.getAsDouble()));
    }

    DiurnalPhaseCalcRequest getRequest() {
        return request;
    }

    abstract protected Body prepareBody();

    protected DiurnalPhaseCalcTransitResolver prepareTransitResolver() {
        return new DiurnalPhaseCalcTransitResolver(this);
    }

    protected DiurnalPhaseCalcExtremeApproximator prepareExtremeApproximator() {
        return new DiurnalPhaseCalcExtremeApproximator(this);
    }

    abstract protected DiurnalPhaseCalcExtremeFinder prepareExtremeFinder();

    protected DiurnalPhaseCalcDayValues prepareDayValues(UniversalTimelinePoint noon) {
        return new DiurnalPhaseCalcDayValues(this, noon);
    }

    abstract protected double getNoonStandardAltitude(int dayPosition);

    double getCloseNoonToTransitVector(int dayPosition) {
        if (dayPosition > 1 || dayPosition < -1)
            throw new IllegalArgumentException();
        return getDay(dayPosition).get(NOON_TO_TRANSIT_VECTOR, () -> transitResolver.findCloseNoonToTransitVector(dayPosition));
    }

    DiurnalPhaseCalcDayValues getDay(int dayPosition) {
        return dayValues.get(dayPosition + 2);
    }
}
