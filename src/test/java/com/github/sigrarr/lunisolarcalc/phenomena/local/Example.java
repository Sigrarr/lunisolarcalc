package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.subjects.GeoCoords;
import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

public class Example implements Titled {

    final String label;
    final GeoCoords geoCoords;
    final double referenceOffsetDayFraction;
    final CalendarPoint[] expectedLocalDateTimes;
    final CalendarPoint baseLocalNoon;

    Example(
        String label,
        GeoCoords geoCoords,
        double referenceOffsetH,
        CalendarPoint[] expectedLocalDateTimes
    ) {
        this.label = label;
        this.geoCoords = geoCoords;
        this.referenceOffsetDayFraction = referenceOffsetH / 24.0;
        this.expectedLocalDateTimes = expectedLocalDateTimes;
        this.baseLocalNoon = reduceToNoon(expectedLocalDateTimes[1]);
    }

    CalendarPoint toLocalDateTime(UniversalTimelinePoint timelinePoint) {
        return timelinePoint.toLocalTimeCalendarPoint(referenceOffsetDayFraction);
    }

    UniversalTimelinePoint toUniversalPoint(CalendarPoint localDateTime) {
        return UniversalTimelinePoint.ofLocalTimeCalendarPoint(localDateTime, referenceOffsetDayFraction);
    }

    CalendarPoint reduceToNoon(CalendarPoint transitLocalDateTime) {
        return Double.compare(transitLocalDateTime.getTime(), 0.5) == 0 ? transitLocalDateTime
            : new CalendarPoint(transitLocalDateTime.y, transitLocalDateTime.m, transitLocalDateTime.getDay() + 0.5);
    }

    @Override
    public String getTitle() {
        return label;
    }
}
