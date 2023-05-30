package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class UniversalTimelinePointTest {

    @Test
    public void shouldSupportLocalTimeConversion() {
        UniversalTimelinePoint ut = UniversalTimelinePoint.ofCalendaricParameters(2000, 1, 1.5);
        CalendarPoint lt = new CalendarPoint(2000, 1, 1, 13, 0, 0);
        double offset = +1.0 / 24.0;
        GeoCoords geoCoords = GeoCoords.ofConventional(0, Math.toRadians(15.0));
        assertAgreement(ut, lt, offset, geoCoords);

        ut = UniversalTimelinePoint.ofCalendaricParameters(1001, 1, 1, 0, 30, 0);
        lt = new CalendarPoint(1000, 12, 31, 12, 31, 0);
        offset = -0.5 + Calcs.MINUTE_TO_DAY;
        geoCoords = GeoCoords.ofConventional(0, Math.toRadians(Calcs.Angle.toSingleDegreesValue(-179, 45, 0)));
        assertAgreement(ut, lt, offset, geoCoords);
    }

    private void assertAgreement(UniversalTimelinePoint ut, CalendarPoint lt, double offset, GeoCoords geoCoords) {
        assertEquals(ut, UniversalTimelinePoint.ofLocalTimeCalendarPoint(lt, offset));
        assertEquals(ut, UniversalTimelinePoint.ofLocalTimeCalendarPoint(lt, geoCoords));
        assertEquals(lt, ut.toLocalTimeCalendarPoint(offset));
        assertEquals(lt, ut.toLocalTimeCalendarPoint(geoCoords));
    }
}
