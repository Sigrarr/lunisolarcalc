package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

class DiurnalPhaseCalcRequest {

    final UniversalTimelinePoint baseNoon;
    final double latitude;
    final double longitude;
    final Set<DiurnalPhase> phases;
    final double precisionRadians;

    DiurnalPhaseCalcRequest(CalendarPoint baseLocalDate, GeoCoords geoCoords, Set<DiurnalPhase> phases, double precisionRadians) {
        this.baseNoon = getBaseNoon(baseLocalDate, geoCoords);
        this.latitude = geoCoords.getPlanetographicLatitude();
        this.longitude = geoCoords.getPlanetographicLongitude();
        this.phases = phases;
        this.precisionRadians = precisionRadians;
    }

    private UniversalTimelinePoint getBaseNoon(CalendarPoint baseLocalDate, GeoCoords geoCoords) {
        CalendarPoint baseLocalNoon = Double.compare(baseLocalDate.getTime(), 0.5) == 0 ?
            baseLocalDate : new CalendarPoint(baseLocalDate.y, baseLocalDate.m, baseLocalDate.getDay() + 0.5);
        return UniversalTimelinePoint.ofLocalTimeCalendarPoint(
            baseLocalNoon,
            geoCoords.getConventionalLongitude() / Calcs.TURN
        );
    }
}
