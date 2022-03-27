package com.github.sigrarr.lunisolarcalc.time;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.julianform.*;
import com.github.sigrarr.lunisolarcalc.time.timeline.JulianDayOutOfPeriodException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelinePoint implements Comparable<TimelinePoint> {

    private static final double ILLEGAL_CENTURIAL_T = Timeline.julianDayToCenturialT(Timeline.JULIAN_PERIOD_START_JD) - 1.0;
    private static final double ILLEGAL_MILLENIAL_TAU = Timeline.julianDayToMillenialTau(Timeline.JULIAN_PERIOD_START_JD) - 1.0;

    protected static double comparisonDeltaDays = Calcs.SECOND_TO_DAY;

    public final double julianDay;
    public final TimeType timeType;

    private GregorianCalendarPoint gregorianCalendarPoint = null;
    private double centurialT = ILLEGAL_CENTURIAL_T;
    private double millenialTau = ILLEGAL_MILLENIAL_TAU;

    public TimelinePoint(double julianDay) {
        this(julianDay, TimeType.UNIVERSAL);
    }

    public TimelinePoint(double julianDay, TimeType timeType) {
        this.julianDay = julianDay;
        this.timeType = timeType;
        validate();
    }

    public TimelinePoint(TimelinePoint timelinePoint) {
        this.julianDay = timelinePoint.julianDay;
        this.timeType = timelinePoint.timeType;
    }

    public static double getComparisonDeltaDays() {
        return comparisonDeltaDays;
    }

    public static void setComparisonDeltaDays(double delta) {
        comparisonDeltaDays = delta;
    }

    public static TimelinePoint ofCalendarPoint(JulianformCalendarPoint calendarPoint) {
        return ofCalendarPoint(calendarPoint, TimeType.UNIVERSAL);
    }

    public static TimelinePoint ofCalendarPoint(JulianformCalendarPoint calendarPoint, TimeType timeType) {
        TimelinePoint timelinePoint = new TimelinePoint(Timeline.julianformCalendarToJulianDay(calendarPoint), timeType);
        if (calendarPoint instanceof GregorianCalendarPoint) {
            timelinePoint.gregorianCalendarPoint = (GregorianCalendarPoint) calendarPoint;
        }
        return timelinePoint;
    }

    public static TimelinePoint ofCenturialT(double centurialT) {
        return ofCenturialT(centurialT, TimeType.UNIVERSAL);
    }

    public static TimelinePoint ofCenturialT(double centurialT, TimeType timeType) {
        TimelinePoint timelinePoint = new TimelinePoint(Timeline.centurialTToJulianDay(centurialT), timeType);
        timelinePoint.centurialT = centurialT;
        return timelinePoint;
    }

    public static TimelinePoint ofMillenialTau(double millenialTau) {
        return ofMillenialTau(millenialTau, TimeType.UNIVERSAL);
    }

    public static TimelinePoint ofMillenialTau(double millenialTau, TimeType timeType) {
        TimelinePoint timelinePoint = new TimelinePoint(Timeline.millenialTauToCenturialT(millenialTau), timeType);
        timelinePoint.millenialTau = millenialTau;
        return timelinePoint;
    }

    public GregorianCalendarPoint getGregorianCalendarPoint() {
        if (gregorianCalendarPoint == null)
            gregorianCalendarPoint = Timeline.julianDayToGregorianCalendar(julianDay);
        return gregorianCalendarPoint;
    }

    public ProlepticGregorianCalendarPoint getProlepticGregorianCalendarPoint() {
        return Timeline.julianDayToProlepticGregorianCalendar(julianDay);
    }

    public ProlepticJulianCalendarPoint getProlepticJulianCalendarPoint() {
        return Timeline.julianDayToProlepticJulianCalendar(julianDay);
    }

    public double getCenturialT() {
        if (centurialT <= ILLEGAL_CENTURIAL_T)
            centurialT = Timeline.julianDayToCenturialT(julianDay);
        return centurialT;
    }

    public double getMillenialTau() {
        if (millenialTau <= ILLEGAL_MILLENIAL_TAU)
            millenialTau = Timeline.julianDayToMillenialTau(julianDay);
        return millenialTau;
    }

    public double getCenturialTPower(int power) {
        return Math.pow(getCenturialT(), power);
    }

    public double getMillenialTauPower(int power) {
        return Math.pow(getMillenialTau(), power);
    }

    public TimelinePoint relabelToUniversalTime() {
        return new TimelinePoint(this.julianDay, TimeType.UNIVERSAL);
    }

    public TimelinePoint relabelToDynamicalTime() {
        return new TimelinePoint(this.julianDay, TimeType.DYNAMICAL);
    }

    public TimelinePoint convertToDynamicalTime() {
        return convertToTimeType(TimeType.DYNAMICAL);
    }

    public TimelinePoint convertToUniversalTime() {
        return convertToTimeType(TimeType.UNIVERSAL);
    }

    public TimelinePoint convertToTimeType(TimeType targetTimeType) {
        return new TimelinePoint(convertJulianDayToTimeType(targetTimeType), targetTimeType);
    }

    private double convertJulianDayToTimeType(TimeType targetTimeType) {
        double targetJd = julianDay;
        if (timeType != targetTimeType) {
            targetJd += targetTimeType.deltaTSignumForConversionTo * Time.getDeltaTDays(getGregorianCalendarPoint().y);
        }
        return targetJd;
    }

    private void validate() {
        if (julianDay < Timeline.JULIAN_PERIOD_START_JD || julianDay >= Timeline.JULIAN_PERIOD_END_JD) {
            throw new JulianDayOutOfPeriodException(julianDay);
        }
    }

    @Override
    public int compareTo(TimelinePoint o) {
        return timeType == o.timeType ?
            Calcs.compare(julianDay, o.julianDay, comparisonDeltaDays)
            : Calcs.compare(julianDay, o.convertJulianDayToTimeType(timeType), comparisonDeltaDays);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimelinePoint)) {
            return false;
        }
        TimelinePoint tp = (TimelinePoint) o;
        return timeType == tp.timeType && Calcs.equal(julianDay, tp.julianDay, comparisonDeltaDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(julianDay, timeType);
    }

    @Override
    public String toString() {
        return "" + julianDay + " " + (timeType == TimeType.UNIVERSAL ? "JD" : "JDE");
    }
}
