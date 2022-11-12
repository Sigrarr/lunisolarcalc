package com.github.sigrarr.lunisolarcalc.time.julianform;

import java.time.*;

public class ProlepticGregorianCalendarPoint extends JulianformCalendarPoint implements Comparable<ProlepticGregorianCalendarPoint> {

    public static final Rules RULES = Rules.GREGORIAN;

    public ProlepticGregorianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    public ProlepticGregorianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    public ProlepticGregorianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    public static ProlepticGregorianCalendarPoint ofLocalDateTime(LocalDateTime localDateTime) {
        return new ProlepticGregorianCalendarPoint(
            localDateTime.getYear(),
            localDateTime.getMonthValue(),
            localDateTime.getDayOfMonth(),
            localDateTime.getHour(),
            localDateTime.getMinute(),
            localDateTime.getSecond()
        );
    }

    public static ProlepticGregorianCalendarPoint ofLocalDate(LocalDate localDate) {
        return new ProlepticGregorianCalendarPoint(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    @Override
    public Rules getRules() {
        return RULES;
    }

    @Override
    public int compareTo(ProlepticGregorianCalendarPoint pgcp) {
        return compareNominally(this, pgcp);
    }
}
