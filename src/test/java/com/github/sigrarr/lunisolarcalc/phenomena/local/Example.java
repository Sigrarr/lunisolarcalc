package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;

import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.*;

class Example implements Titled {

    // https://www.timeanddate.com/worldclock/@6620709
    final static GeoCoords ADELAIDE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -67, 13, 0)),
        Math.toRadians(toSingleDegreesValue( -68, 23, 0))
    );
    // https://www.timeanddate.com/worldclock/antarctica/belgrano-ii-base
    final static GeoCoords BELGRANO2 = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -77, 52, 0)),
        Math.toRadians(toSingleDegreesValue( -34, 38, 0))
    );
    // https://www.timeanddate.com/worldclock/norway/bodo
    final static GeoCoords BODOE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  67, 17, 0)),
        Math.toRadians(toSingleDegreesValue(  14, 23, 0))
    );
    // https://www.timeanddate.com/worldclock/@7670547
    final static GeoCoords GREENWICH_PARK = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( 51, 29, 0)),
        0.0
    );
    // https://www.timeanddate.com/worldclock/usa/honolulu
    final static GeoCoords HONOLULU = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  21, 19, 0)),
        Math.toRadians(toSingleDegreesValue(-157, 51, 0))
    );
    // https://www.timeanddate.com/worldclock/@2617832
    final static GeoCoords LEJRE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  55, 36, 0)),
        Math.toRadians(toSingleDegreesValue(  11, 58, 0))
    );
    // https://www.timeanddate.com/worldclock/norway/longyearbyen
    final static GeoCoords LONGYEARBYEN = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  78, 13, 0)),
        Math.toRadians(toSingleDegreesValue(  15, 38, 0))
    );
    // https://www.timeanddate.com/worldclock/australia/sydney
    final static GeoCoords SYDNEY = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -33, 52, 0)),
        Math.toRadians(toSingleDegreesValue( 151, 12, 0))
    );
    // https://www.timeanddate.com/worldclock/canada/vancouver
    final static GeoCoords VANCOUVER = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  49, 17, 0)),
        Math.toRadians(toSingleDegreesValue(-123,  7, 0))
    );
    // https://www.timeanddate.com/worldclock/poland/wroclaw
    final static GeoCoords WROCLAW = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  51, 07, 0)),
        Math.toRadians(toSingleDegreesValue(  17, 02, 0))
    );

    final String label;
    final GeoCoords geoCoords;
    final double officialOffsetDayFraction;
    final CalendarPoint[] expectedLocalDateTimes;
    final CalendarPoint baseLocalNoon;

    Example(
        String label,
        GeoCoords geoCoords,
        double officialOffsetH,
        CalendarPoint[] expectedLocalDateTimes
    ) {
        this.label = label;
        this.geoCoords = geoCoords;
        this.officialOffsetDayFraction = officialOffsetH / 24.0;
        this.expectedLocalDateTimes = expectedLocalDateTimes;
        this.baseLocalNoon = reduceToNoon(expectedLocalDateTimes[1]);
    }

    CalendarPoint toLocalDateTime(UniversalTimelinePoint timelinePoint) {
        return timelinePoint.toLocalTimeCalendarPoint(officialOffsetDayFraction);
    }

    UniversalTimelinePoint toUniversalPoint(CalendarPoint localDateTime) {
        return UniversalTimelinePoint.ofLocalTimeCalendarPoint(localDateTime, officialOffsetDayFraction);
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
