package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;

abstract class DiurnalPhaseFinderAbstract {

    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseFinderAbstract(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    public UniversalOccurrence<BodyDiurnalPhase> find(UniversalTimelinePoint date, DiurnalPhase phase, double longitude, double latitude) {
        core.reset(new DiurnalPhaseCalcRequest(date, EnumSet.of(phase), longitude, latitude));
        return core.occurrenceSupplier.get();
    }

    public UniversalTimelinePoint findTime(UniversalTimelinePoint date, DiurnalPhase phase, double longitude, double latitude) {
        core.reset(new DiurnalPhaseCalcRequest(date, EnumSet.of(phase), longitude, latitude));
        return core.timelinePointSupplier.get();
    }

    public Stream<UniversalOccurrence<BodyDiurnalPhase>> findMany(UniversalTimelinePoint startDate, Set<DiurnalPhase> phases, double longitude, double latitude) {
        core.reset(new DiurnalPhaseCalcRequest(startDate, phases, longitude, latitude));
        return Stream.generate(core.occurrenceSupplier);
    }
}
