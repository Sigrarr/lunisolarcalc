package com.github.sigrarr.lunisolarcalc.time.timeline;

import com.github.sigrarr.lunisolarcalc.time.Timeline.CalendarType;
import com.github.sigrarr.lunisolarcalc.time.julianform.*;

public class JulianformCalendarPointFactory {

    public JulianformCalendarPoint make(int y, int m, double dt, CalendarType calendarType) {
        switch (calendarType) {
            case PROLEPTIC_JULIAN:
                return new ProlepticJulianCalendarPoint(y, m, dt);
            case PROLEPTIC_GREGORIAN:
                return new ProlepticGregorianCalendarPoint(y, m, dt);
            case GREGORIAN:
            default:
                return new GregorianCalendarPoint(y, m, dt);
        }
    }

}
