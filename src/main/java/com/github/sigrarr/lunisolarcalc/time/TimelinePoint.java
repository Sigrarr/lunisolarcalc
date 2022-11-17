package com.github.sigrarr.lunisolarcalc.time;

import java.time.*;
import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.julianform.*;
import com.github.sigrarr.lunisolarcalc.time.timeline.JulianDayOutOfPeriodException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TimelinePoint implements Comparable<TimelinePoint> {

    public static final double MINIMAL_JULIAN_DAY = Timeline.JULIAN_PERIOD_START_JD;
    public static final double MAXIMAL_JULIAN_DAY = Timeline.JULIAN_PERIOD_END_JD;
    private static final double ILLEGAL_CENTURIAL_T = Timeline.julianDayToCenturialT(MINIMAL_JULIAN_DAY) - 1.0;
    private static final double ILLEGAL_MILLENIAL_TAU = Timeline.julianDayToMillenialTau(MINIMAL_JULIAN_DAY) - 1.0;

    protected static double comparisonDeltaDays = Calcs.SECOND_TO_DAY;

    public final double julianDay;
    public final TimeType timeType;

    private GregorianCalendarPoint gregorianCalendarPoint = null;
    private ProlepticGregorianCalendarPoint prolepticGregorianCalendarPoint = null;
    private ProlepticJulianCalendarPoint prolepticJulianCalendarPoint = null;
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
        TimelinePoint timelinePoint = new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeType);
        if (calendarPoint instanceof GregorianCalendarPoint)
            timelinePoint.gregorianCalendarPoint = (GregorianCalendarPoint) calendarPoint;
        else if (calendarPoint instanceof ProlepticGregorianCalendarPoint)
            timelinePoint.prolepticGregorianCalendarPoint = (ProlepticGregorianCalendarPoint) calendarPoint;
        else if (calendarPoint instanceof ProlepticJulianCalendarPoint)
            timelinePoint.prolepticJulianCalendarPoint = (ProlepticJulianCalendarPoint) calendarPoint;
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

    public static TimelinePoint ofLegacyGregorianCalendar(GregorianCalendar gregorianCalendar) {
        return ofCalendarPoint(GregorianCalendarPoint.ofLegacyGregorianCalendar(gregorianCalendar));
    }

    public static TimelinePoint ofLocalDateTime(LocalDateTime localDateTime) {
        return ofCalendarPoint(ProlepticGregorianCalendarPoint.ofLocalDateTime(localDateTime));
    }

    public static TimelinePoint ofLocalDate(LocalDate localDate) {
        return ofCalendarPoint(ProlepticGregorianCalendarPoint.ofLocalDate(localDate));
    }

    public GregorianCalendarPoint toGregorianCalendarPoint() {
        if (gregorianCalendarPoint == null)
            gregorianCalendarPoint = Timeline.julianDayToGregorianCalendar(julianDay);
        return gregorianCalendarPoint;
    }

    public ProlepticGregorianCalendarPoint toProlepticGregorianCalendarPoint() {
        if (prolepticGregorianCalendarPoint == null)
            prolepticGregorianCalendarPoint = Timeline.julianDayToProlepticGregorianCalendar(julianDay);
        return prolepticGregorianCalendarPoint;
    }

    public ProlepticJulianCalendarPoint toProlepticJulianCalendarPoint() {
        if (prolepticJulianCalendarPoint == null)
            prolepticJulianCalendarPoint = Timeline.julianDayToProlepticJulianCalendar(julianDay);
        return prolepticJulianCalendarPoint;
    }

    public double toCenturialT() {
        if (centurialT <= ILLEGAL_CENTURIAL_T)
            centurialT = Timeline.julianDayToCenturialT(julianDay);
        return centurialT;
    }

    public double toMillenialTau() {
        if (millenialTau <= ILLEGAL_MILLENIAL_TAU)
            millenialTau = Timeline.julianDayToMillenialTau(julianDay);
        return millenialTau;
    }

    public double calculateCenturialTPower(int power) {
        return Math.pow(toCenturialT(), power);
    }

    public double calculateMillenialTauPower(int power) {
        return Math.pow(toMillenialTau(), power);
    }

    public TimelinePoint relabelToUniversalTime() {
        return new TimelinePoint(this.julianDay, TimeType.UNIVERSAL);
    }

    public TimelinePoint relabelToDynamicalTime() {
        return new TimelinePoint(this.julianDay, TimeType.DYNAMICAL);
    }

    public TimelinePoint toDynamicalTime() {
        return toTimeType(TimeType.DYNAMICAL);
    }

    public TimelinePoint toUniversalTime() {
        return toTimeType(TimeType.UNIVERSAL);
    }

    public TimelinePoint toTimeType(TimeType targetTimeType) {
        return timeType == targetTimeType ? this : new TimelinePoint(convertJulianDayToOtherTimeType(), targetTimeType);
    }

    private double convertJulianDayToOtherTimeType() {
        return Time.shiftDaysToTimeType(julianDay, timeType.getOther(), toGregorianCalendarPoint().y);
    }

    private void validate() {
        if (julianDay < MINIMAL_JULIAN_DAY || julianDay > MAXIMAL_JULIAN_DAY) {
            throw new JulianDayOutOfPeriodException(julianDay);
        }
    }

    @Override
    public int compareTo(TimelinePoint o) {
        return timeType == o.timeType ?
            Calcs.compare(julianDay, o.julianDay, comparisonDeltaDays)
            : Calcs.compare(julianDay, o.convertJulianDayToOtherTimeType(), comparisonDeltaDays);
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
