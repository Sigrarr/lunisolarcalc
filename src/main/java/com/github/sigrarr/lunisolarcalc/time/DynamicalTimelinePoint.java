package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

/**
 * A {@link TimelinePoint timeline point} in the {@link TimeScale#DYNAMICAL Dynamical Time}.
 *
 * @see UniversalTimelinePoint
 */
public final class DynamicalTimelinePoint extends TimelinePoint implements Comparable<DynamicalTimelinePoint> {
    /**
     * The time scale of timeline points represented by this class.
     */
    public static final TimeScale TIME_SCALE = TimeScale.DYNAMICAL;

    /**
     * Constructs an instance with a given {@link #julianDay Julian Ephemeris Day} number.
     *
     * @param julianEphemerisDay    {@link #julianDay Julian Ephemeris Day} number
     */
    public DynamicalTimelinePoint(double julianEphemerisDay) {
        super(julianEphemerisDay);
    }

    /**
     * Constructs an instance with a given calendar point (of the main calendar).
     *
     * @param calendarPoint     calendar point (of the main calendar)
     * @return                  new instance
     */
    public DynamicalTimelinePoint(CalendarPoint calendarPoint) {
        super(calendarPoint);
    }

    /**
     * Obtains an instance with a given {@link #julianDay Julian Ephemeris Day} number.
     *
     * @param julianEphemerisDay    {@link #julianDay Julian Ephemeris Day} number
     * @return                      instance
     */
    public static DynamicalTimelinePoint ofJulianEphemerisDay(double julianEphemerisDay) {
        return new DynamicalTimelinePoint(julianEphemerisDay);
    }

    /**
     * Obtains an instance with a given calendar point (of the main calendar).
     *
     * @param calendarPoint     calendar point (of the main calendar)
     * @return                  instance representing the same moment
     */
    public static DynamicalTimelinePoint ofCalendarPoint(CalendarPoint calendarPoint) {
        return new DynamicalTimelinePoint(calendarPoint);
    }

    /**
     * Obtains an instance representing the same moment as a {@link CalendarPoint calendar point}
     * described by given parameters.
     *
     * @param y     {@link NormalCalendaricExpression#y year number}
     * @param m     {@link NormalCalendaricExpression#m month code number}
     * @param dt    {@link NormalCalendaricExpression#dt day-with-time number}
     * @return      instance representing the same moment as a {@link CalendarPoint calendar point}
     *              described by given parameters
     */
    public static DynamicalTimelinePoint ofCalendaricParameters(int y, int m, double dt) {
        return ofCalendarPoint(new CalendarPoint(y, m, dt));
    }

    /**
     * Obtains an instance representing the same moment as a {@link CalendarPoint calendar point}
     * described by given parameters.
     *
     * @param y     {@link NormalCalendaricExpression#y year number}
     * @param m     {@link NormalCalendaricExpression#m month code number}
     * @param d     day of month number (between 1 and the month's maximum)
     * @param h     hour of day number (0-23)
     * @param min   minute of hour number (0-59)
     * @param s     second of minute number (0-59)
     * @return      instance representing the same moment as a {@link CalendarPoint calendar point}
     *              described by given parameters
     */
    public static DynamicalTimelinePoint ofCalendaricParameters(int y, int m, int d, int h, int min, int s) {
        return ofCalendarPoint(new CalendarPoint(y, m, d, h, min, s));
    }

    /**
     * Obtains an instance with a given point of the proleptic Gregorian calendar.
     *
     * @param calendarPoint     point of the proleptic Gregorian calendar
     * @return                  instance representing the same moment
     */
    public static DynamicalTimelinePoint ofProlepticGregorianCalendarPoint(ProlepticGregorianCalendarPoint prolepticGregorianCalendarPoint) {
        return new DynamicalTimelinePoint(Timeline.normalCalendarToJulianDay(prolepticGregorianCalendarPoint));
    }

    /**
     * Obtains an instance with a given point of the proleptic Julian calendar.
     *
     * @param calendarPoint     point of the proleptic Julian calendar
     * @return                  instance representing the same moment
     */
    public static DynamicalTimelinePoint ofProlepticJulianCalendarPoint(ProlepticJulianCalendarPoint prolepticGregorianCalendarPoint) {
        return new DynamicalTimelinePoint(Timeline.normalCalendarToJulianDay(prolepticGregorianCalendarPoint));
    }

    /**
     * Obtains an instance with a given value of {@link #toCenturialT() centurial T}.
     *
     * @param centurialT    {@link #toCenturialT() centurial T}
     * @return              instance
     */
    public static DynamicalTimelinePoint ofCenturialT(double centurialT) {
        return new DynamicalTimelinePoint(Timeline.centurialTToJulianDay(centurialT));
    }

    /**
     * Obtains an instance with a given value of {@link #toMillenialTau() millenial τ (tau)}.
     *
     * @param millenialTau  {@link #toMillenialTau() millenial τ (tau)}
     * @return              instance
     */
    public static DynamicalTimelinePoint ofMillenialTau(double millenialTau) {
        return new DynamicalTimelinePoint(Timeline.millenialTauToCenturialT(millenialTau));
    }

    @Override
    public TimeScale getTimeScale() {
        return TIME_SCALE;
    }

    @Override
    public DynamicalTimelinePoint toDynamicalTime() {
        return this;
    }

    @Override
    public UniversalTimelinePoint toUniversalTime() {
        if (inOtherTimeScale == null)
            inOtherTimeScale = new UniversalTimelinePoint(
                TimeScaleDelta.convertJulianEphemerisDayToUniversalTime(julianDay)
            );
        return (UniversalTimelinePoint) inOtherTimeScale;
    }

    @Override
    public DynamicalTimelinePoint add(double addendDays) {
        return new DynamicalTimelinePoint(julianDay + addendDays);
    }

    /**
     * Compares this timeline point to the other of the same {@link TimeScale time scale}
     * chronologically (in ascending mode), applying
     * the {@link Timeline#getEquivUnitDays() timeline's equivalence unit}.
     *
     * {@link Comparable Consistent} with {@link #equals(Object) equivalence-check}.
     *
     * @param point     timeline point to compare to
     * @return          result of chronological comparison applying the
     *                  {@link Timeline#getEquivUnitDays() timeline's equivalence unit}
     *                  (in the {@link Comparable#compareTo(Object) parent interface's} format)
     */
    @Override
    public int compareTo(DynamicalTimelinePoint point) {
        return NOMINAL_COMPARATOR.compare(this, point);
    }
}
