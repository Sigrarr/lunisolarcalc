package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Stream;

import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;

abstract class DiurnalPhaseFinderAbstract {

    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseFinderAbstract(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    public void setApproximationMode(boolean approximationMode) {
        core.approximationMode = approximationMode;
    }

    public void setApproximationModeOn() {
        setApproximationMode(true);
    }

    public boolean isApproximationModeOn() {
        return core.approximationMode;
    }

    public Optional<UniversalOccurrence<BodyDiurnalPhase>> find(CalendarPoint date, GeoCoords geoCoords, DiurnalPhase phase) {
        core.reset(new DiurnalPhaseCalcRequest(date, geoCoords, EnumSet.of(phase)));
        return core.occurrenceSupplier.get();
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
}
