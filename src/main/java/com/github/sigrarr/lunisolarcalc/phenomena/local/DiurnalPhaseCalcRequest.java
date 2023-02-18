package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;

class DiurnalPhaseCalcRequest {

    final CalendarPoint baseMidnight;
    final double latitude;
    final double longitude;
    final Set<DiurnalPhase> phases;

    DiurnalPhaseCalcRequest(CalendarPoint baseDate, GeoCoords geoCoords, Set<DiurnalPhase> phases) {
        this.baseMidnight = reduceToMidnight(baseDate);
        this.latitude = geoCoords.latitude;
        this.longitude = geoCoords.longitude;
        this.phases = phases;
    }

    private CalendarPoint reduceToMidnight(CalendarPoint baseDate) {
        return Double.compare(baseDate.getTime(), 0.0) == 0 ? baseDate : new CalendarPoint(baseDate.y, baseDate.m, baseDate.getDay());
    }
}
