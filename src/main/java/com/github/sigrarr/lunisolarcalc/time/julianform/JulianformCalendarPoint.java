package com.github.sigrarr.lunisolarcalc.time.julianform;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.time.Time;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public abstract class JulianformCalendarPoint {

    public static enum Rules {
        JULIAN("Julian rules"),
        GREGORIAN("Gregorian rules");

        public final String label;

        private Rules(String label) {
            this.label = label;
        }
    }

    protected static final double DEFAULT_COMPARISON_DELTA_DAYS = Calcs.SECOND_TO_DAY;
    protected static double comparisonDeltaDays = DEFAULT_COMPARISON_DELTA_DAYS;

    public final int y;
    public final int m;
    public final double dt;

    public JulianformCalendarPoint(int y, int m, double dt) {
        this.y = y;
        this.m = m;
        this.dt = dt;
    }

    public JulianformCalendarPoint(int y, int m, int d) {
        this(y, m, (double) d);
    }

    public JulianformCalendarPoint(int y, int m, int d, int h, int min, int s) {
        this(y, m, (double) d + Time.timeToDays(h, min, s));
    }

    public static double getComparisonDeltaDays() {
        return comparisonDeltaDays;
    }

    public static void setComparisonDeltaDays(double delta) {
        comparisonDeltaDays = delta;
    }

    public static int compareNominally(JulianformCalendarPoint a, JulianformCalendarPoint b) {
        int cmpY = Integer.compare(a.y, b.y);
        if (cmpY != 0)
            return cmpY;
        int cmpM = Integer.compare(a.m, b.m);
        if (cmpM != 0)
            return cmpM;
        return Calcs.compare(a.dt, b.dt, comparisonDeltaDays);
    }

    public abstract Rules getRules();

    public boolean isYearLeap() {
        if (y % 4 != 0) {
            return false;
        }
        if (getRules() == Rules.JULIAN) {
            return true;
        }
        return !(y % 100 == 0 && y % 400 != 0);
    }

    public int getNumberOfDaysInYear() {
        return isYearLeap() ? 366 : 365;
    }

    /**
     * Meeus 1998, Ch. 7, p. 65
     */
    public int getDayOfYear() {
        int k = isYearLeap() ? 1 : 2;
        return (275 * m / 9) - (k * ((m + 9) / 12)) + (int) dt - 30;
    }

    public double toYearWithFraction() {
        return y + ((getDayOfYear() - 1.0 + getTime()) / getNumberOfDaysInYear());
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
    public String toString() {
        return this.getClass().getSimpleName() + " (" + getRules().label + ") [" + y + ", " + m + ", " + dt + "]";
    }

    public String formatYMD() {
        return "" + y + "/" + String.format("%02d", m) + "/" + String.format("%02d", getDay());
    }

    public String formatYMDHMin() {
        return formatYMD() + " " + String.format("%02d", getHours()) + ":" + String.format("%02d", getMinutes());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JulianformCalendarPoint)) {
            return false;
        }
        JulianformCalendarPoint jfcp = (JulianformCalendarPoint) o;
        return this == jfcp || (
            getRules() == jfcp.getRules() && y == jfcp.y && m == jfcp.m && Calcs.equal(dt, jfcp.dt, comparisonDeltaDays)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRules(), y, m, Calcs.roundToDelta(dt, 2.0 * comparisonDeltaDays));
    }
}
