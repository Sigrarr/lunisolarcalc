package com.github.sigrarr.lunisolarcalc.time;

import java.time.*;
import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

/**
 * A point of {@link Timeline the timeline}, in a certain {@link TimeType type/scale of time}.
 *
 * Represented primary as Julian Day - a number of days (with fractions) from the beginning
 * of the current Julian Period. Several secondary representations are supported;
 * see methods prefixed with "to-" and static methods prefixed with "of-".
 *
 * The Julian Day number and the time type of an instance are immutable.
 * A representation in the {@link CalendarPoint main calendar}
 * and a corresponding point in the other time type are cached
 * - a reference is stored by an instance, when known.
 *
 * The {@link #compareTo(TimelinePoint) natural ordering} is chronological (ascending); note
 * that it may be inconsistent with {@link #equals(Object) equivalence} for instances of different
 * time types (a {@link #equivalenceConsistentComparator() consistent comparator} is also available).
 */
public class TimelinePoint implements Comparable<TimelinePoint> {
    /**
     * The Julian Day number (number of days from the beginning of the current Julian Period).
     */
    public final double julianDay;
    public final TimeType timeType;

    private TimelinePoint inOtherTimeType = null;
    private CalendarPoint calendarPoint = null;

    /**
     * Constructs an instance with a given {@link #julianDay Julian Day} number,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param julianDay {@link #julianDay Julian Day}
     */
    public TimelinePoint(double julianDay) {
        this(julianDay, TimeType.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given {@link #julianDay Julian Day} number,
     * in a selected time type/scale.
     *
     * @param julianDay {@link #julianDay Julian Day}
     * @param timeType  the selected time type/scale
     */
    public TimelinePoint(double julianDay, TimeType timeType) {
        this.julianDay = julianDay;
        this.timeType = timeType;
        Timeline.validateJulianDayVersusSupportedScope(julianDay);
    }

    /**
     * Constructs an instance with a given Julian Ephemeris Day, ie.
     * {@link #julianDay Julian Day} in {@link TimeType#DYNAMICAL Dynamical Time}.
     *
     * @param julianEphemerisDay    Julian Ephemeris Day ({@link #julianDay Julian Day}
     *                              in {@link TimeType#DYNAMICAL Dynamical Time})
     * @return                      new instance (in {@link TimeType#DYNAMICAL Dynamical Time})
     */
    public static TimelinePoint ofJulianEphemerisDay(double julianEphemerisDay) {
        return new TimelinePoint(julianEphemerisDay, TimeType.DYNAMICAL);
    }

    /**
     * Constructs an instance with a given (Julian/Gregorian) calendar point,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param calendarPoint     Julian/Gregorian calendar point
     * @return                  new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofCalendarPoint(CalendarPoint calendarPoint) {
        return ofCalendarPoint(calendarPoint, TimeType.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given (Julian/Gregorian) calendar point,
     * in a selected time type/scale.
     *
     * @param calendarPoint     Julian/Gregorian calendar point
     * @param timeType          the selected time type/scale
     * @return                  new instance
     */
    public static TimelinePoint ofCalendarPoint(CalendarPoint calendarPoint, TimeType timeType) {
        TimelinePoint point = new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeType);
        point.calendarPoint = calendarPoint;
        return point;
    }

    /**
     * Constructs an instance with a given proleptic Gregorian calendar point,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param calendarPoint     proleptic Gregorian calendar point
     * @return                  new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint calendarPoint) {
        return ofProlepticGregorianCalendarPoint(calendarPoint, TimeType.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given proleptic Gregorian calendar point,
     * in a selected time type/scale.
     *
     * @param calendarPoint     proleptic Gregorian calendar point
     * @param timeType          the selected time type/scale
     * @return                  new instance
     */
    public static TimelinePoint ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint calendarPoint, TimeType timeType) {
        return new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeType);
    }

    /**
     * Constructs an instance with a given proleptic Julian calendar point,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param calendarPoint     proleptic Julian calendar point
     * @return                  new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofProlepticJulianCalendarPoint(ProlepticJulianCalendarPoint calendarPoint) {
        return ofProlepticJulianCalendarPoint(calendarPoint, TimeType.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given proleptic Julian calendar point,
     * in a selected time type/scale.
     *
     * @param calendarPoint     proleptic Julian calendar point
     * @param timeType          the selected time type/scale
     * @return                  new instance
     */
    public static TimelinePoint ofProlepticJulianCalendarPoint(ProlepticJulianCalendarPoint calendarPoint, TimeType timeType) {
        return new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeType);
    }

    /**
     * Constructs an instance with a given value of {@link #toCenturialT() centurial T},
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param centurialT    {@link #toCenturialT() centurial T}
     * @return              new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofCenturialT(double centurialT) {
        return ofCenturialT(centurialT, TimeType.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given value of {@link #toCenturialT() centurial T},
     * in a selected time type/scale.
     *
     * @param centurialT    {@link #toCenturialT() centurial T}
     * @param timeType      the selected time type/scale
     * @return              new instance
     */
    public static TimelinePoint ofCenturialT(double centurialT, TimeType timeType) {
        return new TimelinePoint(Timeline.centurialTToJulianDay(centurialT), timeType);
    }

    /**
     * Constructs an instance with a given value of {@link #toMillenialTau() millenial τ (tau)},
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param millenialTau  {@link #toMillenialTau() millenial τ (tau)}
     * @return              new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofMillenialTau(double millenialTau) {
        return ofMillenialTau(millenialTau, TimeType.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given value of {@link #toMillenialTau() millenial τ (tau)},
     * in a selected time type/scale..
     *
     * @param millenialTau  {@link #toMillenialTau() millenial τ (tau)}
     * @param timeType      the selected time type/scale
     * @return              new instance
     */
    public static TimelinePoint ofMillenialTau(double millenialTau, TimeType timeType) {
        return new TimelinePoint(Timeline.millenialTauToCenturialT(millenialTau), timeType);
    }

    /**
     * Construct an instance representing the current moment in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @return  new instance, representing the current moment in {@link TimeType#UNIVERSAL Universal Time}
     */
    public static TimelinePoint ofNow() {
        return ofCalendarPoint(CalendarPoint.ofNow());
    }

    /**
     * Constructs an instance with a given {@link LocalDateTime} object,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param localDateTime     {@link LocalDateTime} object
     * @return                  new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofLocalDateTime(LocalDateTime localDateTime) {
        return ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint.ofLocalDateTime(localDateTime));
    }

    /**
     * Constructs an instance with a given {@link LocalDate} object,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * @param localDate     {@link LocalDate} object
     * @return              new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofLocalDate(LocalDate localDate) {
        return ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint.ofLocalDate(localDate));
    }

    /**
     * Constructs an instance with a given {@link java.util.GregorianCalendar} object,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * Note that if the passed GregorianCalendar object is set to a specific time zone,
     * the new TimelinePoint won't keep this time zone's offset anymore.
     *
     * @param gregorianCalendar     {@link java.util.GregorianCalendar} object
     * @return                      new instance (in {@link TimeType#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofLegacyGregorianCalendar(GregorianCalendar gregorianCalendar) {
        return ofCalendarPoint(CalendarPoint.ofLegacyGregorianCalendar(gregorianCalendar));
    }

    /**
     * Expresses this timeline point in the Julian/Gregorian calendar.
     *
     * @return  Julian/Gregorian calendar point
     */
    public CalendarPoint toCalendarPoint() {
        if (calendarPoint == null)
            calendarPoint = Timeline.julianDayToCalendar(julianDay);
        return calendarPoint;
    }

    /**
     * Expresses this timeline point in the proleptic Gregorian calendar.
     *
     * @return  calendar point of the proleptic Gregorian calendar
     */
    public ProlepticGregorianCalendarPoint toProlepticGregorianCalendarPoint() {
        return Timeline.julianDayToProlepticGregorianCalendar(julianDay);
    }

    /**
     * Expresses this timeline point in the proleptic Julian calendar.
     *
     * @return  calendar point of the proleptic Julian calendar
     */
    public ProlepticJulianCalendarPoint toProlepticJulianCalendarPoint() {
        return Timeline.julianDayToProlepticJulianCalendar(julianDay);
    }

    /**
     * Expresses this timeline point as centurial T:
     * number of Julian centuries (hundreds of {@link Timeline#JULIAN_YEAR_DAYS Julian years})
     * from {@link Timeline#EPOCH_2000_JD Epoch 2000}.
     *
     * @return  centurial T, the number of Julian centuries
     *          (hundreds of {@link Timeline#JULIAN_YEAR_DAYS Julian years})
     *          from {@link Timeline#EPOCH_2000_JD Epoch 2000}
     */
    public double toCenturialT() {
        return Timeline.julianDayToCenturialT(julianDay);
    }

    /**
     * Expresses this timeline point as millenial τ (tau):
     * number of Julian millenia (thousands of {@link Timeline#JULIAN_YEAR_DAYS Julian years})
     * from {@link Timeline#EPOCH_2000_JD Epoch 2000}.
     *
     * @return  millenial τ (tau), the number of Julian millenia
     *          (thousands of {@link Timeline#JULIAN_YEAR_DAYS Julian years})
     *          from {@link Timeline#EPOCH_2000_JD Epoch 2000}
     */
    public double toMillenialTau() {
        return Timeline.julianDayToMillenialTau(julianDay);
    }

    /**
     * Obtains an instance in {@link TimeType#DYNAMICAL Dynamical Time}.
     *
     * If the time type of this instance is the Dynamical Time, returns this instance,
     * otherwise prepares a corresponding instance in Dynamical Time.
     *
     * @return  instance in {@link TimeType#DYNAMICAL Dynamical Time}:
     *          this instance, if its time type is the Dynamical Time,
     *          or another instance otherwise
     */
    public TimelinePoint toDynamicalTime() {
        return toTimeType(TimeType.DYNAMICAL);
    }

    /**
     * Obtains an instance in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * If the time type of this instance is the Universal Time, returns this instance,
     * otherwise prepares a corresponding instance in Universal Time.
     *
     * @return  instance in {@link TimeType#UNIVERSAL Universal Time}:
     *          this instance, if its time type is the Universal Time,
     *          or another instance otherwise
     */
    public TimelinePoint toUniversalTime() {
        return toTimeType(TimeType.UNIVERSAL);
    }

    /**
     * Obtains an instance in a selected time type/scale.
     *
     * If the time type of this instance is the same as the selected, returns this instance,
     * otherwise prepares a corresponding instance in the other time type.
     *
     * @return  instance in the selected time type/scale:
     *          this instance, if its time type is the selected one,
     *          or another instance otherwise
     */
    public TimelinePoint toTimeType(TimeType targetTimeType) {
        if (timeType == targetTimeType)
            return this;
        if (inOtherTimeType == null) {
            inOtherTimeType = new TimelinePoint(convertJulianDayToTimeType(targetTimeType), targetTimeType);
            inOtherTimeType.inOtherTimeType = this;
        }
        return inOtherTimeType;
    }

    /**
     * Creates a new instance with the same nominal {@link #julianDay Julian Day} value,
     * in {@link TimeType#UNIVERSAL Universal Time}.
     *
     * Note that if the time type of this instance is not the Universal Time,
     * the result will represent a different point of time.
     *
     * @return  new instance with the same nominal {@link #julianDay Julian Day} value,
     *          in {@link TimeType#UNIVERSAL Universal Time}
     */
    public TimelinePoint toUniversalTimeNominalEquivalent() {
        return new TimelinePoint(this.julianDay, TimeType.UNIVERSAL);
    }

    /**
     * Creates a new instance with the same nominal {@link #julianDay Julian Day} value,
     * in {@link TimeType#DYNAMICAL Dynamical Time}.
     *
     * Note that if the time type of this instance is not the Dynamical Time,
     * the result will represent a different point of time.
     *
     * @return  new instance with the same nominal {@link #julianDay Julian Day} value,
     *          in {@link TimeType#DYNAMICAL Dynamical Time}
     */
    public TimelinePoint toDynamicalTimeNominalEquivalent() {
        return new TimelinePoint(this.julianDay, TimeType.DYNAMICAL);
    }

    /**
     * Expresses this timeline point as a {@link LocalDateTime} object.
     *
     * @return {@link LocalDateTime} object
     */
    public LocalDateTime toLocalDateTime() {
        return toProlepticGregorianCalendarPoint().toLocalDateTime();
    }

    /**
     * Expresses this timeline point as a {@link LocalDate} object.
     *
     * @return {@link LocalDate} object
     */
    public LocalDate toLocalDate() {
        return toProlepticGregorianCalendarPoint().toLocalDate();
    }

    /**
     * Expresses this timeline point as a {@link java.util.GregorianCalendar} object.
     * The target object's time zone will be set to "Universal".
     *
     * @return  {@link java.util.GregorianCalendar} object
     */
    public GregorianCalendar toLegacyGregorianCalendar() {
        return toCalendarPoint().toLegacyGregorianCalendar();
    }

    /**
     * Adds a given time value to this timeline point,
     * resulting in a new timeline point, shifted forward or backward
     * for a positive or negative value of the addend respectively.
     * Preserves the time type.
     *
     * @param addendDays    time value to add, in days
     * @return              new timeline point - shifted by the given value,
     *                      in the same time type
     */
    public TimelinePoint add(double addendDays) {
        return new TimelinePoint(julianDay + addendDays, timeType);
    }

    /**
     * Calculates the difference between another timeline point
     * and this timeline point, in days. Result will be positive
     * if the other point lies after this one, or negative - if before
     * (or 0 - if they represent the same point of time).
     *
     * Note that if the other point's time type is different than this one's,
     * the operation will include on-the-fly, non-remembered time type conversion.
     *
     * @param o     the other timeline point
     * @return      the difference between the other point and this, in days
     *              (positive - if the other lies after this, negative - if before,
     *              0 - if both are equal)
     */
    public double diff(TimelinePoint o) {
        return timeType == TimeType.UNIVERSAL && o.timeType == TimeType.UNIVERSAL ?
            o.julianDay - julianDay
            : o.convertJulianDayToTimeType(TimeType.DYNAMICAL) - convertJulianDayToTimeType(TimeType.DYNAMICAL);
    }

    private double convertJulianDayToTimeType(TimeType timeType) {
        return this.timeType == timeType ? julianDay : Time.shiftDaysToTimeType(julianDay, timeType, toCalendarPoint().y);
    }

    /**
     * Equivalence check applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}:
     * checks whether the other object (timeline point) is an equivalent of this, i.e. whether
     * they represent the same moment (equivalence unit) of time and have the same time type.
     *
     * @param o     other object (timeline point), to check its equivalence with this
     * @return      {@code true} - if the other point has the same time type as this
     *              and their {@link #julianDay Julian Days} are close enough to be considered equal
     *              (belong to the same {@link Timeline#getEquivUnitDays() equivalence unit});
     *              {@code false } - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimelinePoint)) {
            return false;
        }
        TimelinePoint point = (TimelinePoint) o;
        return timeType == point.timeType
            && Timeline.equal(julianDay, point.julianDay);
    }

    /**
     * Equivalence check applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}:
     * generates a {@link Object#hashCode() hash code} identifying the combination
     * of this timeline point's time type and of the equivalence unit its {@link #julianDay Julian Day}
     * belongs to.
     *
     * @return  {@link Object#hashCode() hash code} identifying the combination
     *          of this timeline point's time type and of the equivalence unit
     *          its {@link #julianDay Julian Day} belongs to
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            Timeline.roundToEquivUnit(julianDay),
            timeType
        );
    }

    /**
     * Compares this timeline point to the other - chronologically,
     * applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}.
     *
     * Note that if the other point's time type is different than this one's,
     * the operation will include on-the-fly, non-remembered time type conversion,
     * and in the case when both points represent the same moment (equivalence unit) of time,
     * the result will be {@link Comparable inconsistent with equivalence-check}.
     *
     * @param o     the timeline point to compare to
     * @return      result of chronological comparison
     *              applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit},
     *              in the {@link Comparable#compareTo(Object) parent interface's} format
     * @see         #equivalenceConsistentComparator()
     */
    @Override
    public int compareTo(TimelinePoint o) {
        return timeType == TimeType.UNIVERSAL && o.timeType == TimeType.UNIVERSAL ?
            Timeline.compare(julianDay, o.julianDay)
            : Timeline.compare(
                convertJulianDayToTimeType(TimeType.DYNAMICAL),
                o.convertJulianDayToTimeType(TimeType.DYNAMICAL)
            );
    }

    /**
     * Obtains the {@link Comparable equivalence-consistent} chronological comparator
     * of timeline points applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}.
     *
     * It is intented for cases when such consistency is important,
     * e.g. when TimelinePoint instances are stored in a {@link java.util.TreeMap TreeMap}
     * or a {@link java.util.TreeSet TreeSet}.
     *
     * Unlike the {@link #compareTo(TimelinePoint) TimelinePoint's natural ordering},
     * it doesn't equate points which represent the same moment (equivalence unit) of time
     * in different time types - but compares them {@link Enum#compareTo(Enum) by time type}.
     *
     * @return  {@link Comparable equivalence-consistent} chronological comparator of timeline points,
     *          applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}
     * @see     #equals(Object)
     */
    public static Comparator<TimelinePoint> equivalenceConsistentComparator() {
        return (a, b) -> {
            int cmp = a.compareTo(b);
            return cmp != 0 ? cmp : a.timeType.compareTo(b.timeType);
        };
    }

    /**
     * Expresses this timeline point textually in
     * a {@link NormalCalendarPoint#formatDateTimeToMinutes() calendaric format},
     * adding the {@link TimeType#mainAbbreviation time type determiner}.
     *
     * @return  textual expression in a
     *          {@link NormalCalendarPoint#formatDateTimeToMinutes() calendaric format},
     *          with the {@link TimeType#mainAbbreviation time type determiner} added
     */
    public String formatCalendrically() {
        return toCalendarPoint().formatDateTimeToMinutes() + " " + timeType.mainAbbreviation;
    }

    @Override
    public String toString() {
        return String.format("(%s) %f %s",
            getClass().getSimpleName(),
            julianDay,
            timeType.julianDayAbbreviation
        );
    }
}
