package com.github.sigrarr.lunisolarcalc.time;

import java.time.*;
import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

/**
 * A point of {@link Timeline the timeline}, in a certain {@link TimeScale scale of time}.
 *
 * Represented primary as Julian Day - a number of days (with fractions) from the beginning
 * of the current Julian Period. Several secondary representations are supported;
 * see methods prefixed with "to-" and static methods prefixed with "of-".
 *
 * The Julian Day number and the time scale of an instance are immutable.
 * A representation in the {@link CalendarPoint main calendar}
 * and a corresponding point in the other time scale are cached
 * - a reference is stored by an instance, when known.
 *
 * The {@link #compareTo(TimelinePoint) natural ordering} is chronological (ascending); note
 * that it may be inconsistent with {@link #equals(Object) equivalence} for instances of different
 * time scales (a {@link #equivalenceConsistentComparator() consistent comparator} is also available).
 */
public class TimelinePoint implements Comparable<TimelinePoint> {
    /**
     * The Julian Day number (number of days from the beginning of the current Julian Period).
     */
    public final double julianDay;
    public final TimeScale timeScale;

    private TimelinePoint inOtherTimeScale = null;
    private CalendarPoint calendarPoint = null;

    /**
     * Constructs an instance with a given {@link #julianDay Julian Day} number,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param julianDay {@link #julianDay Julian Day}
     */
    public TimelinePoint(double julianDay) {
        this(julianDay, TimeScale.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given {@link #julianDay Julian Day} number,
     * in a selected time scale.
     *
     * @param julianDay {@link #julianDay Julian Day}
     * @param timeScale  the selected time scale
     */
    public TimelinePoint(double julianDay, TimeScale timeScale) {
        this.julianDay = julianDay;
        this.timeScale = timeScale;
        Timeline.validateJulianDayVersusSupportedScope(julianDay);
    }

    /**
     * Constructs an instance with a given Julian Ephemeris Day, ie.
     * {@link #julianDay Julian Day} in {@link TimeScale#DYNAMICAL Dynamical Time}.
     *
     * @param julianEphemerisDay    Julian Ephemeris Day ({@link #julianDay Julian Day}
     *                              in {@link TimeScale#DYNAMICAL Dynamical Time})
     * @return                      new instance (in {@link TimeScale#DYNAMICAL Dynamical Time})
     */
    public static TimelinePoint ofJulianEphemerisDay(double julianEphemerisDay) {
        return new TimelinePoint(julianEphemerisDay, TimeScale.DYNAMICAL);
    }

    /**
     * Constructs an instance with a given (Julian/Gregorian) calendar point,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param calendarPoint     Julian/Gregorian calendar point
     * @return                  new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofCalendarPoint(CalendarPoint calendarPoint) {
        return ofCalendarPoint(calendarPoint, TimeScale.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given (Julian/Gregorian) calendar point,
     * in a selected time scale.
     *
     * @param calendarPoint     Julian/Gregorian calendar point
     * @param timeScale          the selected time scale
     * @return                  new instance
     */
    public static TimelinePoint ofCalendarPoint(CalendarPoint calendarPoint, TimeScale timeScale) {
        TimelinePoint point = new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeScale);
        point.calendarPoint = calendarPoint;
        return point;
    }

    /**
     * Constructs an instance with a given proleptic Gregorian calendar point,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param calendarPoint     proleptic Gregorian calendar point
     * @return                  new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint calendarPoint) {
        return ofProlepticGregorianCalendarPoint(calendarPoint, TimeScale.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given proleptic Gregorian calendar point,
     * in a selected time scale.
     *
     * @param calendarPoint     proleptic Gregorian calendar point
     * @param timeScale          the selected time scale
     * @return                  new instance
     */
    public static TimelinePoint ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint calendarPoint, TimeScale timeScale) {
        return new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeScale);
    }

    /**
     * Constructs an instance with a given proleptic Julian calendar point,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param calendarPoint     proleptic Julian calendar point
     * @return                  new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofProlepticJulianCalendarPoint(ProlepticJulianCalendarPoint calendarPoint) {
        return ofProlepticJulianCalendarPoint(calendarPoint, TimeScale.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given proleptic Julian calendar point,
     * in a selected time scale.
     *
     * @param calendarPoint     proleptic Julian calendar point
     * @param timeScale          the selected time scale
     * @return                  new instance
     */
    public static TimelinePoint ofProlepticJulianCalendarPoint(ProlepticJulianCalendarPoint calendarPoint, TimeScale timeScale) {
        return new TimelinePoint(Timeline.calendarToJulianDay(calendarPoint), timeScale);
    }

    /**
     * Constructs an instance with a given value of {@link #toCenturialT() centurial T},
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param centurialT    {@link #toCenturialT() centurial T}
     * @return              new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofCenturialT(double centurialT) {
        return ofCenturialT(centurialT, TimeScale.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given value of {@link #toCenturialT() centurial T},
     * in a selected time scale.
     *
     * @param centurialT    {@link #toCenturialT() centurial T}
     * @param timeScale      the selected time scale
     * @return              new instance
     */
    public static TimelinePoint ofCenturialT(double centurialT, TimeScale timeScale) {
        return new TimelinePoint(Timeline.centurialTToJulianDay(centurialT), timeScale);
    }

    /**
     * Constructs an instance with a given value of {@link #toMillenialTau() millenial τ (tau)},
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param millenialTau  {@link #toMillenialTau() millenial τ (tau)}
     * @return              new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofMillenialTau(double millenialTau) {
        return ofMillenialTau(millenialTau, TimeScale.UNIVERSAL);
    }

    /**
     * Constructs an instance with a given value of {@link #toMillenialTau() millenial τ (tau)},
     * in a selected time scale..
     *
     * @param millenialTau  {@link #toMillenialTau() millenial τ (tau)}
     * @param timeScale      the selected time scale
     * @return              new instance
     */
    public static TimelinePoint ofMillenialTau(double millenialTau, TimeScale timeScale) {
        return new TimelinePoint(Timeline.millenialTauToCenturialT(millenialTau), timeScale);
    }

    /**
     * Construct an instance representing the current moment in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @return  new instance, representing the current moment in {@link TimeScale#UNIVERSAL Universal Time}
     */
    public static TimelinePoint ofNow() {
        return ofCalendarPoint(CalendarPoint.ofNow());
    }

    /**
     * Constructs an instance with a given {@link LocalDateTime} object,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param localDateTime     {@link LocalDateTime} object
     * @return                  new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofLocalDateTime(LocalDateTime localDateTime) {
        return ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint.ofLocalDateTime(localDateTime));
    }

    /**
     * Constructs an instance with a given {@link LocalDate} object,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * @param localDate     {@link LocalDate} object
     * @return              new instance (in {@link TimeScale#UNIVERSAL Universal Time})
     */
    public static TimelinePoint ofLocalDate(LocalDate localDate) {
        return ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint.ofLocalDate(localDate));
    }

    /**
     * Constructs an instance with a given {@link java.util.GregorianCalendar} object,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * Note that if the passed GregorianCalendar object is set to a specific time zone,
     * the new TimelinePoint won't keep this time zone's offset anymore.
     *
     * @param gregorianCalendar     {@link java.util.GregorianCalendar} object
     * @return                      new instance (in {@link TimeScale#UNIVERSAL Universal Time})
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
     * number of {@link Timeline#JULIAN_CENTURY_DAYS Julian centuries}
     * from {@link Timeline#EPOCH_2000_JD Epoch 2000}.
     *
     * @return  centurial T, the number of
     *          {@link Timeline#JULIAN_CENTURY_DAYS Julian centuries}
     *          from {@link Timeline#EPOCH_2000_JD Epoch 2000}
     */
    public double toCenturialT() {
        return Timeline.julianDayToCenturialT(julianDay);
    }

    /**
     * Expresses this timeline point as millenial τ (tau):
     * number of {@link Timeline#JULIAN_MILLENIUM_DAYS Julian millenia}
     * from {@link Timeline#EPOCH_2000_JD Epoch 2000}.
     *
     * @return  millenial τ (tau), the number of
     *          {@link Timeline#JULIAN_MILLENIUM_DAYS Julian millenia}
     *          from {@link Timeline#EPOCH_2000_JD Epoch 2000}
     */
    public double toMillenialTau() {
        return Timeline.julianDayToMillenialTau(julianDay);
    }

    /**
     * Obtains an instance in {@link TimeScale#DYNAMICAL Dynamical Time}.
     *
     * If the time scale of this instance is the Dynamical Time, returns this instance,
     * otherwise prepares a corresponding instance in Dynamical Time.
     *
     * @return  instance in {@link TimeScale#DYNAMICAL Dynamical Time}:
     *          this instance, if its time scale is the Dynamical Time,
     *          or another instance otherwise
     */
    public TimelinePoint toDynamicalTime() {
        return toTimeScale(TimeScale.DYNAMICAL);
    }

    /**
     * Obtains an instance in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * If the time scale of this instance is the Universal Time, returns this instance,
     * otherwise prepares a corresponding instance in Universal Time.
     *
     * @return  instance in {@link TimeScale#UNIVERSAL Universal Time}:
     *          this instance, if its time scale is the Universal Time,
     *          or another instance otherwise
     */
    public TimelinePoint toUniversalTime() {
        return toTimeScale(TimeScale.UNIVERSAL);
    }

    /**
     * Obtains an instance in a selected time scale.
     *
     * If the time scale of this instance is the same as the selected, returns this instance,
     * otherwise prepares a corresponding instance in the other time scale.
     *
     * @return  instance in the selected time scale:
     *          this instance, if its time scale is the selected one,
     *          or another instance otherwise
     */
    public TimelinePoint toTimeScale(TimeScale targetTimeScale) {
        if (timeScale == targetTimeScale)
            return this;
        if (inOtherTimeScale == null)
            switch (targetTimeScale) {
                case DYNAMICAL:
                    inOtherTimeScale = TimelinePoint.ofJulianEphemerisDay(TimeScaleDelta.convertJulianDayToDynamicalTime(julianDay));
                    break;
                case UNIVERSAL:
                    inOtherTimeScale = new TimelinePoint(TimeScaleDelta.convertJulianEphemerisDayToUniversalTime(julianDay));
                    break;
            }
        return inOtherTimeScale;
    }

    /**
     * Creates a new instance with the same nominal {@link #julianDay Julian Day} value,
     * in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * Note that if the time scale of this instance is not the Universal Time,
     * the result will represent a different point of time.
     *
     * @return  new instance with the same nominal {@link #julianDay Julian Day} value,
     *          in {@link TimeScale#UNIVERSAL Universal Time}
     */
    public TimelinePoint toUniversalTimeNominalEquivalent() {
        return new TimelinePoint(this.julianDay, TimeScale.UNIVERSAL);
    }

    /**
     * Creates a new instance with the same nominal {@link #julianDay Julian Day} value,
     * in {@link TimeScale#DYNAMICAL Dynamical Time}.
     *
     * Note that if the time scale of this instance is not the Dynamical Time,
     * the result will represent a different point of time.
     *
     * @return  new instance with the same nominal {@link #julianDay Julian Day} value,
     *          in {@link TimeScale#DYNAMICAL Dynamical Time}
     */
    public TimelinePoint toDynamicalTimeNominalEquivalent() {
        return new TimelinePoint(this.julianDay, TimeScale.DYNAMICAL);
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
     * Preserves the time scale.
     *
     * @param addendDays    time value to add, in days
     * @return              new timeline point - shifted by the given value,
     *                      in the same time scale
     */
    public TimelinePoint add(double addendDays) {
        return new TimelinePoint(julianDay + addendDays, timeScale);
    }

    /**
     * Calculates the difference between another timeline point
     * and this timeline point, in days. Result will be positive
     * if the other point lies after this one, or negative - if before
     * (or 0 - if they represent the same point of time).
     *
     * Note that if the other point's time scale is different than this one's,
     * the operation will include time scale conversion.
     *
     * @param o     the other timeline point
     * @return      the difference between the other point and this, in days
     *              (positive - if the other lies after this, negative - if before,
     *              0 - if both are equal)
     */
    public double diff(TimelinePoint o) {
        return timeScale == TimeScale.UNIVERSAL && o.timeScale == TimeScale.UNIVERSAL ?
            o.julianDay - julianDay
            : o.toDynamicalTime().julianDay - toDynamicalTime().julianDay;
    }

    /**
     * Equivalence check applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}:
     * checks whether the other object (timeline point) is an equivalent of this, i.e. whether
     * they represent the same moment (equivalence unit) of time and have the same time scale.
     *
     * @param o     other object (timeline point), to check its equivalence with this
     * @return      {@code true} - if the other point has the same time scale as this
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
        return timeScale == point.timeScale
            && Timeline.equal(julianDay, point.julianDay);
    }

    /**
     * Equivalence check applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}:
     * generates a {@link Object#hashCode() hash code} identifying the combination
     * of this timeline point's time scale and of the equivalence unit its {@link #julianDay Julian Day}
     * belongs to.
     *
     * @return  {@link Object#hashCode() hash code} identifying the combination
     *          of this timeline point's time scale and of the equivalence unit
     *          its {@link #julianDay Julian Day} belongs to
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            Timeline.roundToEquivUnit(julianDay),
            timeScale
        );
    }

    /**
     * Compares this timeline point to the other - chronologically,
     * applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}.
     *
     * Note that if the other point's time scale is different than this one's,
     * the operation will include time scale conversion,
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
        return timeScale == TimeScale.UNIVERSAL && o.timeScale == TimeScale.UNIVERSAL ?
            Timeline.compare(julianDay, o.julianDay)
            : Timeline.compare(
                toDynamicalTime().julianDay,
                o.toDynamicalTime().julianDay
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
     * in different time scales - but compares them {@link Enum#compareTo(Enum) by time scale}.
     *
     * @return  {@link Comparable equivalence-consistent} chronological comparator of timeline points,
     *          applying {@link Timeline#getEquivUnitDays() the timeline's equivalence unit}
     * @see     #equals(Object)
     */
    public static Comparator<TimelinePoint> equivalenceConsistentComparator() {
        return (a, b) -> {
            int cmp = a.compareTo(b);
            return cmp != 0 ? cmp : a.timeScale.compareTo(b.timeScale);
        };
    }

    /**
     * Expresses this timeline point textually in
     * a {@link NormalCalendarPoint#formatDateTimeToMinutes() calendaric format},
     * adding the {@link TimeScale#mainAbbreviation time scale determiner}.
     *
     * @return  textual expression in a
     *          {@link NormalCalendarPoint#formatDateTimeToMinutes() calendaric format},
     *          with the {@link TimeScale#mainAbbreviation time scale determiner} added
     */
    public String formatCalendrically() {
        return toCalendarPoint().formatDateTimeToMinutes() + " " + timeScale.mainAbbreviation;
    }

    @Override
    public String toString() {
        return String.format("(%s) %f %s",
            getClass().getSimpleName(),
            julianDay,
            timeScale.julianDayAbbreviation
        );
    }
}
