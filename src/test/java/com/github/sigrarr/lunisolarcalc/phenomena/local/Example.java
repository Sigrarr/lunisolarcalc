package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;

import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Titled;

class Example implements Titled {

    // https://www.timeanddate.com/worldclock/@6620709
    final static GeoCoords ADELAIDE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -67, 13, 0)),
        Math.toRadians(toSingleDegreesValue( -68, 23, 0))
    );
    // https://www.timeanddate.com/worldclock/norway/bodo
    final static GeoCoords BODOE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  67, 17, 0)),
        Math.toRadians(toSingleDegreesValue(  14, 23, 0))
    );
    // https://www.timeanddate.com/worldclock/usa/honolulu
    final static GeoCoords HONOLULU = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  21, 19, 0)),
        Math.toRadians(toSingleDegreesValue(-157, 51, 0))
    );
    // https://www.timeanddate.com/worldclock/australia/sydney
    final static GeoCoords SYDNEY = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -33, 52, 0)),
        Math.toRadians(toSingleDegreesValue( 151, 12, 0))
    );
    // https://www.timeanddate.com/worldclock/poland/wroclaw
    final static GeoCoords WROCLAW = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  51, 07, 0)),
        Math.toRadians(toSingleDegreesValue(  17, 02, 0))
    );

    final String label;
    final GeoCoords geoCoords;
    final double officialOffsetDayFraction;
    final CalendarPoint baseLocalNoon;
    final CalendarPoint[] expectedLocalDateTimes;

    Example(
        String label,
        GeoCoords geoCoords,
        double officialOffsetH,
        CalendarPoint baseLocalNoon,
        CalendarPoint[] expectedLocalDateTimes
    ) {
        this.label = label;
        this.geoCoords = geoCoords;
        this.officialOffsetDayFraction = officialOffsetH / 24.0;
        this.baseLocalNoon = baseLocalNoon;
        this.expectedLocalDateTimes = expectedLocalDateTimes;
    }

    CalendarPoint toLocalDateTime(UniversalTimelinePoint timelinePoint) {
        return timelinePoint.toLocalTimeCalendarPoint(officialOffsetDayFraction);
    }

    @Override
    public String getTitle() {
        return label;
    }
}
