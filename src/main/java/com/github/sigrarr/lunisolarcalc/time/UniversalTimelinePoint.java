package com.github.sigrarr.lunisolarcalc.time;

import java.time.*;
import java.util.GregorianCalendar;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

/**
 * A {@linkplain TimelinePoint timeline point} in the {@linkplain TimeScale#UNIVERSAL Universal Time}.
 *
 * This class introduces several additional conversion methods (named "to...")
 * and static construction methods (named "of..."), which reflects the support of
 * {@link LocalDateTime}, {@link LocalDate} and legacy {@link java.util.GregorianCalendar}.
 *
 * @see DynamicalTimelinePoint
 */
public final class UniversalTimelinePoint extends TimelinePoint implements Comparable<UniversalTimelinePoint> {

    public static final TimeScale TIME_SCALE = TimeScale.UNIVERSAL;

    /**
     * Constructs an instance with a given {@linkplain #julianDay Julian Day} number.
     *
     * @param julianDay {@linkplain #julianDay Julian Day}
     */
    public UniversalTimelinePoint(double julianDay) {
        super(julianDay);
    }

    /**
     * Constructs an instance with a given calendar point (of the main calendar).
     *
     * @param calendarPoint     calendar point (of the main calendar)
     */
    public UniversalTimelinePoint(CalendarPoint calendarPoint) {
        super(calendarPoint);
    }

    /**
     * Obtains an instance representing the current moment.
     *
     * @return  instance representing the current moment
     */
    public static UniversalTimelinePoint ofNow() {
        return ofCalendar(CalendarPoint.ofNow());
    }

    /**
     * Obtains an instance with a given calendar point (of the main calendar).
     *
     * @param calendarPoint     calendar point (of the main calendar)
     * @return                  instance representing the same moment
     */
    public static UniversalTimelinePoint ofCalendar(CalendarPoint calendarPoint) {
        return (UniversalTimelinePoint) TimelinePoint.ofCalendar(calendarPoint);
    }

    /**
     * Obtains an instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     * described by given parameters.
     *
     * @param y     {@linkplain NormalCalendaricExpression#y year number}
     * @param m     {@linkplain NormalCalendaricExpression#m month code number}
     * @param dt    {@linkplain NormalCalendaricExpression#dt day-with-time number}
     * @return      instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     *              described by given parameters
     */
    public static UniversalTimelinePoint ofCalendaricParameters(int y, int m, double dt) {
        return (UniversalTimelinePoint) TimelinePoint.ofCalendaricParameters(y, m, dt);
    }

    /**
     * Obtains an instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     * described by given parameters.
     *
     * @param y     {@linkplain NormalCalendaricExpression#y year number}
     * @param m     {@linkplain NormalCalendaricExpression#m month code number}
     * @param d     day of month number (between 1 and the month's maximum)
     * @param h     hour of day number (0-23)
     * @param min   minute of hour number (0-59)
     * @param s     second of minute number (0-59)
     * @return      instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     *              described by given parameters
     */
    public static UniversalTimelinePoint ofCalendaricParameters(int y, int m, int d, int h, int min, int s) {
        return (UniversalTimelinePoint) TimelinePoint.ofCalendaricParameters(y, m, d, h, min, s);
    }

    /**
     * Obtains an instance with a given point of the proleptic Gregorian calendar.
     *
     * @param prolepticGregorianCalendarPoint   point of the proleptic Gregorian calendar
     * @return                                  instance representing the same moment
     */
    public static UniversalTimelinePoint ofProlepticGregorianCalendar(ProlepticGregorianCalendarPoint prolepticGregorianCalendarPoint) {
        return (UniversalTimelinePoint) TimelinePoint.ofProlepticGregorianCalendar(prolepticGregorianCalendarPoint);
    }

    /**
     * Obtains an instance with a given point of the proleptic Julian calendar.
     *
     * @param prolepticJulianCalendarPoint  point of the proleptic Julian calendar
     * @return                              instance representing the same moment
     */
    public static UniversalTimelinePoint ofProlepticJulianCalendar(ProlepticJulianCalendarPoint prolepticJulianCalendarPoint) {
        return (UniversalTimelinePoint) TimelinePoint.ofProlepticJulianCalendar(prolepticJulianCalendarPoint);
    }

    /**
     * Obtains an instance with a given value of {@linkplain #toCenturialT() centurial T}.
     *
     * @param centurialT    {@linkplain #toCenturialT() centurial T}
     * @return              instance
     */
    public static UniversalTimelinePoint ofCenturialT(double centurialT) {
        return (UniversalTimelinePoint) TimelinePoint.ofCenturialT(centurialT);
    }

    /**
     * Obtains an instance with a given value of {@linkplain #toMillenialTau() millenial τ (tau)}.
     *
     * @param millenialTau  {@linkplain #toMillenialTau() millenial τ (tau)}
     * @return              instance
     */
    public static UniversalTimelinePoint ofMillenialTau(double millenialTau) {
        return (UniversalTimelinePoint) TimelinePoint.ofMillenialTau(millenialTau);
    }

    /**
     * Obtains an instance with a given {@link LocalDateTime} object.
     *
     * @param localDateTime     {@link LocalDateTime} object
     * @return                  instance representing the same moment
     */
    public static UniversalTimelinePoint ofLocalDateTime(LocalDateTime localDateTime) {
        return ofProlepticGregorianCalendar(ProlepticGregorianCalendarPoint.ofLocalDateTime(localDateTime));
    }

    /**
     * Obtains an instance with a given {@link LocalDate} object.
     *
     * @param localDate     {@link LocalDate} object
     * @return              instance representing the same moment
     */
    public static UniversalTimelinePoint ofLocalDate(LocalDate localDate) {
        return ofProlepticGregorianCalendar(ProlepticGregorianCalendarPoint.ofLocalDate(localDate));
    }

    /**
     * Obtains an instance with a given {@link java.util.GregorianCalendar} object.
     *
     * Note that if the passed GregorianCalendar object is set to a specific time zone,
     * the timeline point won't keep this time zone's offset anymore.
     *
     * @param gregorianCalendar     {@link java.util.GregorianCalendar} object
     * @return                      instance representing the same moment
     */
    public static UniversalTimelinePoint ofLegacyGregorianCalendar(GregorianCalendar gregorianCalendar) {
        return ofCalendar(CalendarPoint.ofLegacyGregorianCalendar(gregorianCalendar));
    }

    @Override
    public TimeScale getTimeScale() {
        return TIME_SCALE;
    }

    @Override
    public DynamicalTimelinePoint toDynamicalTime() {
        if (inOtherTimeScale == null)
            inOtherTimeScale = new DynamicalTimelinePoint(
                TimeScaleDelta.convertJulianDayToDynamicalTime(julianDay)
            );
        return (DynamicalTimelinePoint) inOtherTimeScale;
    }

    @Override
    public UniversalTimelinePoint toUniversalTime() {
        return this;
    }

    /**
     * Expresses this timeline point as a {@link LocalDateTime} object.
     *
     * @return {@link LocalDateTime} object
     */
    public LocalDateTime toLocalDateTime() {
        return toProlepticGregorianCalendar().toLocalDateTime();
    }

    /**
     * Expresses this timeline point as a {@link LocalDate} object.
     *
     * @return {@link LocalDate} object
     */
    public LocalDate toLocalDate() {
        return toProlepticGregorianCalendar().toLocalDate();
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

    @Override
    public UniversalTimelinePoint add(double addendDays) {
        return new UniversalTimelinePoint(julianDay + addendDays);
    }

    /**
     * Compares this timeline point to the other of the same {@linkplain TimeScale time scale}
     * chronologically (in ascending mode), applying
     * the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit}.
     *
     * {@linkplain Comparable Consistent} with {@linkplain #equals(Object) equivalence-check}.
     *
     * @param point     timeline point to compare to
     * @return          result of chronological comparison applying the
     *                  {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit}
     *                  (in the {@linkplain Comparable#compareTo(Object) parent interface's} format)
     */
    @Override
    public int compareTo(UniversalTimelinePoint point) {
        return NOMINAL_COMPARATOR.compare(this, point);
    }
}
