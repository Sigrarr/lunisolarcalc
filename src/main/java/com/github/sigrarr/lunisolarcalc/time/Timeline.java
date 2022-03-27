package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.julianform.*;
import com.github.sigrarr.lunisolarcalc.time.julianform.JulianformCalendarPoint.Rules;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class Timeline {

    protected static final double JULIAN_YEAR_DAYS = 365.25;
    protected static final double JULIAN_CENTURY_DAYS = 100 * JULIAN_YEAR_DAYS;
    protected static final double JULIAN_MILLENIUM_DAYS = 1000.0 * JULIAN_YEAR_DAYS;

    public static double JULIAN_PERIOD_START_JD = 0.0;
    public static double JULIAN_PERIOD_END_JD = JULIAN_PERIOD_START_JD + (7980 * JULIAN_YEAR_DAYS);

    private static final double JD_MONTH_FACTOR = 30.6 + Calcs.EPSILON;
    protected static final double EPOCH_2000_JD = 2451545.0;

    /**
     * Meeus 1998, 7.1, p. 61
     */
    public static double julianformCalendarToJulianDay(JulianformCalendarPoint calendarPoint) {
        int y = calendarPoint.y;
        int m = calendarPoint.m;
        if (m <= 2) {
            m += 12;
            y--;
        }

        int b = 0;
        if (calendarPoint.getRules() == Rules.GREGORIAN) {
            int a = y / 100;
            b = 2 - a + (a / 4);
        }

        return Math.floor(JULIAN_YEAR_DAYS * (y + 4716))
            + Math.floor(JD_MONTH_FACTOR * (m + 1))
            + calendarPoint.dt
            + b
            - 1524.5;
    }

    /**
     * Meeus 1998, Ch. 7, p. 63
     */
    public static GregorianCalendarPoint julianDayToGregorianCalendar(double jd) {
        GregorianCalendarPoint calendarPoint = new GregorianCalendarPoint(0, 0, 0);
        setCalendarPointByJulianDay(calendarPoint, jd, null);
        return calendarPoint;
    }

    /**
     * Based on: Meeus 1998, Ch. 7, p. 63
     */
    public static ProlepticGregorianCalendarPoint julianDayToProlepticGregorianCalendar(double jd) {
        ProlepticGregorianCalendarPoint calendarPoint = new ProlepticGregorianCalendarPoint(0, 0, 0);
        setFixedRulesCalendarPointByJulianDay(calendarPoint, jd);
        return calendarPoint;
    }

    /**
     * Based on: Meeus 1998, Ch. 7, p. 63
     */
    public static ProlepticJulianCalendarPoint julianDayToProlepticJulianCalendar(double jd) {
        ProlepticJulianCalendarPoint calendarPoint = new ProlepticJulianCalendarPoint(0, 0, 0);
        setFixedRulesCalendarPointByJulianDay(calendarPoint, jd);
        return calendarPoint;
    }

    protected static void setFixedRulesCalendarPointByJulianDay(JulianformCalendarPoint fixedRulescalendarPoint, double jd) {
        setCalendarPointByJulianDay(fixedRulescalendarPoint, jd, fixedRulescalendarPoint.getRules());
    }

    protected static void setCalendarPointByJulianDay(JulianformCalendarPoint calendarPoint, double jd, Rules nullableForcedRules) {
        double jdMidnight = jd + 0.5;
        double z = Math.floor(jdMidnight);
        double f = jdMidnight - z;
        double a;
        boolean julianCalendarRules = nullableForcedRules == null ? z < 2299161.0 : nullableForcedRules == Rules.JULIAN;
        if (julianCalendarRules) {
            a = z;
        } else {
            double alpha = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1.0 + alpha - Math.floor(alpha / 4);
        }
        double b = a + 1524.0;
        double c = Math.floor((b - 122.1) / JULIAN_YEAR_DAYS);
        double d = Math.floor(JULIAN_YEAR_DAYS * c);
        double e = Math.floor((b - d) / JD_MONTH_FACTOR);

        calendarPoint.dt = b - d - Math.floor(JD_MONTH_FACTOR * e) + f;
        calendarPoint.m = (int) (e - (e < 14 ? 1.0 : 13.0));
        calendarPoint.y = (int) (c - (calendarPoint.m > 2 ? 4716 : 4715));        
    }

    /**
     * Meeus 1998, 32.1, p. 218
     */
    public static double julianDayToMillenialTau(double jd) {
        return (jd - EPOCH_2000_JD) / JULIAN_MILLENIUM_DAYS;
    }

    public static double millenialTauToJulianDay(double millenialTau) {
        // TODO test
        return millenialTau * JULIAN_MILLENIUM_DAYS + EPOCH_2000_JD;
    }

    /**
     * Meeus 1998, 22.1, p. 143
     */
    public static double julianDayToCenturialT(double jd) {
        return (jd - EPOCH_2000_JD) / JULIAN_CENTURY_DAYS;
    }

    public static double centurialTToJulianDay(double centurialT) {
        // TODO test
        return centurialT * JULIAN_CENTURY_DAYS + EPOCH_2000_JD;
    }

    public static double millenialTauToCenturialT(double tau) {
        return 10.0 * tau;
    }

    public static double centurialTToMillenialTau(double cT) {
        return 0.1 * cT;
    }
}
