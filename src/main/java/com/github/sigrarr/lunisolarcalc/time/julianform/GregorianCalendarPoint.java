package com.github.sigrarr.lunisolarcalc.time.julianform;

import java.util.GregorianCalendar;

public class GregorianCalendarPoint extends JulianformCalendarPoint implements Comparable<GregorianCalendarPoint> {

    public static final GregorianCalendarPoint EPOCH_GREGORIAN_RULES = new GregorianCalendarPoint(1582, 10, 15);

    public GregorianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    public GregorianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    public GregorianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    public static GregorianCalendarPoint ofLegacyGregorianCalendar(GregorianCalendar gregorianCalendar) {
        int y = gregorianCalendar.get(GregorianCalendar.YEAR);
        return new GregorianCalendarPoint(
            gregorianCalendar.get(GregorianCalendar.ERA) == GregorianCalendar.AD ? y : 1 - y,
            gregorianCalendar.get(GregorianCalendar.MONTH) + 1,
            gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH),
            gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY),
            gregorianCalendar.get(GregorianCalendar.MINUTE),
            gregorianCalendar.get(GregorianCalendar.SECOND)
        );
    }

    @Override
    public Rules getRules() {
        return this.compareTo(EPOCH_GREGORIAN_RULES) < 0 ? Rules.JULIAN : Rules.GREGORIAN;
    }

    @Override
    public int compareTo(GregorianCalendarPoint gcp) {
        return compareNominally(this, gcp);
    }
}
