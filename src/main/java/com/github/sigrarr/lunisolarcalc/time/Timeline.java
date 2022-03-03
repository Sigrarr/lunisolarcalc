package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.RomanCalendarPoint.Calendar;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class Timeline {

    private static final double JULIAN_MEAN_YEAR_DAYS = 365.25;
    private static final double JD_MONTH_FACTOR = 30.6 + Calcs.EPSILON;
    private static final double JULIAN_CENTURY_DAYS = 100 * JULIAN_MEAN_YEAR_DAYS;
    private static final double JULIAN_MILLENIUM_DAYS = 1000.0 * JULIAN_MEAN_YEAR_DAYS;
    private static final double EPOCH_2000_JD = 2451545.0;

    /**
     * Meeus 1998, 7.1, p. 61
     */
    public static double romanCalendarToJulianDay(RomanCalendarPoint romanCalendarPoint) {
        int y = romanCalendarPoint.y;
        int m = romanCalendarPoint.m;
        if (m <= 2) {
            m += 12;
            y--;
        }

        int b = 0;
        if (romanCalendarPoint.getCalendar() == Calendar.GREGORIAN) {
            int a = y / 100;
            b = 2 - a + (a / 4);
        }

        return Math.floor(JULIAN_MEAN_YEAR_DAYS * (y + 4716))
            + Math.floor(JD_MONTH_FACTOR * (m + 1))
            + romanCalendarPoint.dt
            + b
            - 1524.5;
    }

    /**
     * Meeus 1998, Ch. 7, p. 63
     */
    public static void setByJulianDay(RomanCalendarPoint romanCalendarPoint, double jd) {
        double jdMidnight = jd + 0.5;
        double z = Math.floor(jdMidnight);
        double f = jdMidnight - z;
        double a;
        if (z < 2299161.0) {
            a = z;
        } else {
            double alpha = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1.0 + alpha - Math.floor(alpha / 4);
        }
        double b = a + 1524.0;
        double c = Math.floor((b - 122.1) / JULIAN_MEAN_YEAR_DAYS);
        double d = Math.floor(JULIAN_MEAN_YEAR_DAYS * c);
        double e = Math.floor((b - d) / JD_MONTH_FACTOR);

        romanCalendarPoint.dt = b - d - Math.floor(JD_MONTH_FACTOR * e) + f;
        romanCalendarPoint.m = (int) (e - (e < 14 ? 1.0 : 13.0));
        romanCalendarPoint.y = (int) (c - (romanCalendarPoint.m > 2 ? 4716 : 4715));        
    }

    public static RomanCalendarPoint julianDayToRomanCalendar(double jd) {
        RomanCalendarPoint point = new RomanCalendarPoint();
        setByJulianDay(point, jd);
        return point;
    }

    /**
     * Meeus 1998, 32.1, p. 218
     */
    public static double julianDayToMillenialTau(double jd) {
        return (jd - EPOCH_2000_JD) / JULIAN_MILLENIUM_DAYS;
    }

    /**
     * Meeus 1998, 22.1, p. 143
     */
    public static double julianDayToCenturialT(double jd) {
        return (jd - EPOCH_2000_JD) / JULIAN_CENTURY_DAYS;
    }

    public static double millenialTauToCenturialT(double tau) {
        return 10.0 * tau;
    }

    public static double centurialTToMillenialTau(double cT) {
        return 0.1 * cT;
    }

    public static double julianDayToEphemeris(double jd, int romanYear) {
        return jd + Time.timeToDays(0, 0, Time.getDeltaTSeconds(romanYear));
    }

    public static double julianEphemerisDaysToUniversal(double jde, int romanYear) {
        return jde - Time.timeToDays(0, 0, Time.getDeltaTSeconds(romanYear));
    }

    public static double romanCalendarToJulianEphemerisDay(RomanCalendarPoint romanCalendarPoint) {
        return julianDayToEphemeris(romanCalendarToJulianDay(romanCalendarPoint), romanCalendarPoint.y);
    }

    public static RomanCalendarPoint julianEphemerisDayToRomanCalendar(double jde) {
        RomanCalendarPoint rcp = julianDayToRomanCalendar(jde);
        int romanYear = rcp.y;
        double jd = julianEphemerisDaysToUniversal(jde, romanYear);
        setByJulianDay(rcp, jd);
        if (romanYear != rcp.y) {
            setByJulianDay(rcp, julianEphemerisDaysToUniversal(jde, rcp.y));
        }
        return rcp;
    }
}
