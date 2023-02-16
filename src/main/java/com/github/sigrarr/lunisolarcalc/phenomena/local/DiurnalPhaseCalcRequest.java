package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;

class DiurnalPhaseCalcRequest {

    public static enum Mode {
        PRECISION, APPROXIMATION
    }

    final double longitude;
    final double latitude;
    final UniversalTimelinePoint baseMidnight;
    final Set<DiurnalPhase> phases;
    final Mode mode;

    DiurnalPhaseCalcRequest(UniversalTimelinePoint baseMidnight, Set<DiurnalPhase> phases, double longitude, double latitude, Mode mode) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.baseMidnight = baseMidnight;
        this.phases = phases;
        this.mode = mode;
    }

    DiurnalPhaseCalcRequest(UniversalTimelinePoint baseMidnight, Set<DiurnalPhase> phases, double longitude, double latitude) {
        this(baseMidnight, phases, longitude, latitude, Mode.PRECISION);
    }
}
