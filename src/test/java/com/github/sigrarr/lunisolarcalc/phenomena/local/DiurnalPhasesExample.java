package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.calendar.*;

class DiurnalPhasesExample {

    final GeoCoords geoCoords;
    final double timeOffset;
    final TreeMap<DiurnalPhase, NormalCalendarPoint> localDateTimesByPhase;
    Iterator<Entry<DiurnalPhase, NormalCalendarPoint>> it;

    DiurnalPhasesExample(GeoCoords geoCoords, double timeOffset, TreeMap<DiurnalPhase, NormalCalendarPoint> localDateTimesByPhase) {
        this.geoCoords = geoCoords;
        this.timeOffset = timeOffset;
        this.localDateTimesByPhase = localDateTimesByPhase;
        it = localDateTimesByPhase.entrySet().iterator();
    }

    CalendarPoint getCenterDateUT() {
        double jdWithOffset = localDateTimesByPhase.containsKey(DiurnalPhase.TRANSIT) ?
            Timeline.normalCalendarToJulianDay(localDateTimesByPhase.get(DiurnalPhase.TRANSIT))
            : (
                Timeline.normalCalendarToJulianDay(localDateTimesByPhase.get(DiurnalPhase.RISE))
                + Timeline.normalCalendarToJulianDay(localDateTimesByPhase.get(DiurnalPhase.SET))
            ) / 2;
        return Timeline.julianDayToCalendar(jdWithOffset - timeOffset);
    }
}
