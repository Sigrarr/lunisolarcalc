package com.github.sigrarr.lunisolarcalc.time;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class RomanCalendarPoint implements Comparable<RomanCalendarPoint> {

    public static enum Calendar {
        JULIAN("Julian Calendar"), GREGORIAN("Gregorian Calendar");
        private String label;
        public String getLabel() {
            return label;
        }
        private Calendar(String label) {
            this.label = label;
        }
    }

    public static final double HOUR = 1.0 / 24;
    public static final double MINUTE = HOUR / 60;
    public static final double HALF_MINUTE = MINUTE / 2;
    public static final double SECOND = MINUTE / 60;
    protected static final RomanCalendarPoint FIRST_GREGORIAN_DATE = new RomanCalendarPoint(1582, 10, 15);

    public int y = 8;
    public int m = 1;
    public double dt = 1.0;

    public RomanCalendarPoint() {}

    public RomanCalendarPoint(int y, int m, double dt) {
        this.y = y;
        this.m = m;
        this.dt = dt;
    }

    public RomanCalendarPoint(int y, int m, int d) {
        this(y, m, (double) d);
    }

    public Calendar getCalendar() {
        return this.compareTo(FIRST_GREGORIAN_DATE) < 0 ? Calendar.JULIAN : Calendar.GREGORIAN;
    }

    @Override
    public int compareTo(RomanCalendarPoint rcp) {
        int cmpY = Integer.compare(y, rcp.y);
        if (cmpY != 0)
            return cmpY;
        int cmpM = Integer.compare(m, rcp.m);
        if (cmpM != 0)
            return cmpM;
        return Calcs.compare(dt, rcp.dt, HALF_MINUTE);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RomanCalendarPoint)) {
            return false;
        }
        RomanCalendarPoint rcp = (RomanCalendarPoint) o;
        return y == rcp.y && m == rcp.m && Calcs.equal(dt, rcp.dt, HALF_MINUTE);
    }

    public int getDay() {
        return (int) dt;
    }

    public double getTime() {
        return dt - getDay();
    }

    public int getHours() {
        return (int) (getTime() * 24);
    }

    public int getMinutes() {
        double hoursWithFraction = getTime() * 24;
        return (int) Math.round((hoursWithFraction - getHours()) * 60);
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, m, dt);
    }

    @Override
    public String toString() {
        return "(" + getCalendar().getLabel() + ") [" + y + ", " + m + ", " + dt + "]";
    }

    public String formatYMD() {
        return "" + y + "/" + String.format("%02d", m) + "/" + String.format("%02d", getDay());
    }

    public String formatYMDHI() {
        return formatYMD() + " " + String.format("%02d", getHours()) + ":" + String.format("%02d", getMinutes()); 
    }
}
