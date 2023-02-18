package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Stream;

import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.PrecisionTimeNotPositiveException;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

abstract class DiurnalPhaseFinderAbstract {

    public static final double DEFAULT_PRECISION_DAY_FRACTION = Calcs.SECOND_TO_DAY;

    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseFinderAbstract(DiurnalPhaseCalcCore core) {
        this.core = core;
        core.precision = DEFAULT_PRECISION_DAY_FRACTION;
    }

    public double getPrecisionDayFraction() {
        return core.precision;
    }

    public void setPrecisionSeconds(int seconds) {
        validatePrecisionSeconds(seconds);
        core.precision = seconds * Calcs.SECOND_TO_DAY;
    }

    public void setApproximationMode(boolean approximationMode) {
        core.approximationMode = approximationMode;
    }

    public void setOnApproximationMode() {
        setApproximationMode(true);
    }

    public boolean isApproximationModeOn() {
        return core.approximationMode;
    }

    public Optional<UniversalOccurrence<BodyDiurnalPhase>> find(CalendarPoint date, GeoCoords geoCoords, DiurnalPhase phase) {
        core.reset(new DiurnalPhaseCalcRequest(date, geoCoords, EnumSet.of(phase)));
        return core.occurrenceSupplier.get();
    }

    public OptionalDouble findTime(CalendarPoint date, GeoCoords geoCoords, DiurnalPhase phase) {
        core.reset(new DiurnalPhaseCalcRequest(date, geoCoords, EnumSet.of(phase)));
        return core.timeSupplier.get();
    }

    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords) {
        return findMany(baseDate, geoCoords, EnumSet.allOf(DiurnalPhase.class));
    }

    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords, DiurnalPhase phase) {
        return findMany(baseDate, geoCoords, EnumSet.of(phase));
    }

    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords, Set<DiurnalPhase> phases) {
        core.reset(new DiurnalPhaseCalcRequest(baseDate, geoCoords, phases));
        return Stream.generate(core.occurrenceSupplier);
    }

    public Stream<OptionalDouble> findManyTimes(CalendarPoint baseDate, GeoCoords geoCoords, DiurnalPhase phase) {
        core.reset(new DiurnalPhaseCalcRequest(baseDate, geoCoords, EnumSet.of(phase)));
        return Stream.generate(core.timeSupplier);
    }

    private void validatePrecisionSeconds(int seconds) {
        if (seconds <= 0)
            throw new PrecisionTimeNotPositiveException(seconds);
    }
}
