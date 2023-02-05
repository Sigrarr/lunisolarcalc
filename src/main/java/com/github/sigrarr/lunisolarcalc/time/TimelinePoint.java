package com.github.sigrarr.lunisolarcalc.time;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.calendar.*;

/**
 * A point of the {@linkplain Timeline timeline}, belonging to one of the {@linkplain TimeScale time scales}.
 *
 * Represented primary as {@linkplain #julianDay Julian Day}.
 * Several secondary representations are supported
 * (see methods named "to..." and static methods named "of...").
 *
 * The Julian Day number of an instance is immutable.
 * A representation in the {@linkplain CalendarPoint main calendar}
 * and a corresponding point in the other time scale are cached
 * - a reference is stored by an instance, when known.
 *
 * Natural ordering for this abstract class is not defined;
 * in order to sort an array or a collection of timeline points,
 * it is recommended to {@linkplain #toDynamicalTime() project or convert them all to DynamicalTime}
 * or to {@linkplain #toUniversalTime() Universal Time}.
 *
 * @see UniversalTimelinePoint
 * @see DynamicalTimelinePoint
 */
abstract public class TimelinePoint {

    protected static final Comparator<TimelinePoint> NOMINAL_COMPARATOR = (a, b) -> Timeline.compare(a.julianDay, b.julianDay);

    /**
     * The Julian Day number, i.e. the number of days (with a fraction) from the beginning
     * of the current Julian Period.
     * It is just "Julian Day" in {@linkplain TimeScale#UNIVERSAL Universal Time} and
     * "Julian Ephemeris Day" in {@linkplain TimeScale#DYNAMICAL Dynamical Time}.
     */
    public final double julianDay;
    protected TimelinePoint inOtherTimeScale = null;
    protected CalendarPoint calendarPoint = null;

    TimelinePoint(double julianDay) {
        this.julianDay = julianDay;
        Timeline.validateJulianDayVersusSupportedScope(julianDay);
    }

    TimelinePoint(CalendarPoint calendarPoint) {
        this(Timeline.normalCalendarToJulianDay(calendarPoint));
        this.calendarPoint = calendarPoint;
    }

    protected static TimelinePoint ofJulianDayByTimeScale(double julianDay, TimeScale timeScale) {
        switch (timeScale) {
            case DYNAMICAL:
                return new DynamicalTimelinePoint(julianDay);
            case UNIVERSAL:
            default:
                return new UniversalTimelinePoint(julianDay);
        }
    }

    /**
     * Obtains an instance with a given calendar point (of the main calendar;
     * in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param calendarPoint     calendar point (of the main calendar)
     * @return                  instance representing the same moment (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofCalendar(CalendarPoint calendarPoint) {
        return new UniversalTimelinePoint(calendarPoint);
    }

    /**
     * Obtains an instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     * described by given parameters (in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param y     {@linkplain NormalCalendaricExpression#y year number}
     * @param m     {@linkplain NormalCalendaricExpression#m month code number}
     * @param dt    {@linkplain NormalCalendaricExpression#dt day-with-time number}
     * @return      instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     *              described by given parameters (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofCalendaricParameters(int y, int m, double dt) {
        return ofCalendar(new CalendarPoint(y, m, dt));
    }

    /**
     * Obtains an instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     * described by given parameters (in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param y     {@linkplain NormalCalendaricExpression#y year number}
     * @param m     {@linkplain NormalCalendaricExpression#m month code number}
     * @param d     day of month number (between 1 and the month's maximum)
     * @param h     hour of day number (0-23)
     * @param min   minute of hour number (0-59)
     * @param s     second of minute number (0-59)
     * @return      instance representing the same moment as a {@linkplain CalendarPoint calendar point}
     *              described by given parameters (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofCalendaricParameters(int y, int m, int d, int h, int min, int s) {
        return ofCalendar(new CalendarPoint(y, m, d, h, min, s));
    }

    /**
     * Obtains an instance with a given point of the proleptic Gregorian calendar
     * (in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param prolepticGregorianCalendarPoint   point of the proleptic Gregorian calendar
     * @return                                  instance representing the same moment
     *                                          (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofProlepticGregorianCalendar(ProlepticGregorianCalendarPoint prolepticGregorianCalendarPoint) {
        return new UniversalTimelinePoint(Timeline.normalCalendarToJulianDay(prolepticGregorianCalendarPoint));
    }

    /**
     * Obtains an instance with a given point of the proleptic Julian calendar
     * (in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param prolepticJulianCalendarPoint  point of the proleptic Julian calendar
     * @return                              instance representing the same moment
     *                                      (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofProlepticJulianCalendar(ProlepticJulianCalendarPoint prolepticJulianCalendarPoint) {
        return new UniversalTimelinePoint(Timeline.normalCalendarToJulianDay(prolepticJulianCalendarPoint));
    }

    /**
     * Obtains an instance with a given value of {@linkplain #toCenturialT() centurial T}
     * (in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param centurialT    {@linkplain #toCenturialT() centurial T}
     * @return              instance (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofCenturialT(double centurialT) {
        return new UniversalTimelinePoint(Timeline.centurialTToJulianDay(centurialT));
    }

    /**
     * Obtains an instance with a given value of {@linkplain #toMillenialTau() millenial τ (tau)}
     * (in {@linkplain TimeScale#UNIVERSAL UT}).
     *
     * @param millenialTau  {@linkplain #toMillenialTau() millenial τ (tau)}
     * @return              instance (in {@linkplain TimeScale#UNIVERSAL UT})
     */
    public static TimelinePoint ofMillenialTau(double millenialTau) {
        return new UniversalTimelinePoint(Timeline.millenialTauToCenturialT(millenialTau));
    }

    /**
     * Gets the time scale this point belongs to.
     *
     * @return time scale this point belongs to
     */
    abstract public TimeScale getTimeScale();

    /**
     * Obtains an instance in {@linkplain TimeScale#DYNAMICAL Dynamical Time}.
     *
     * If the time scale of this instance is the Dynamical Time, returns this instance,
     * otherwise prepares a corresponding instance in Dynamical Time.
     *
     * @return  instance in {@linkplain TimeScale#DYNAMICAL Dynamical Time}:
     *          this instance, if its time scale is the Dynamical Time,
     *          or a corresponding instance otherwise
     */
    abstract public DynamicalTimelinePoint toDynamicalTime();

    /**
     * Obtains an instance in {@linkplain TimeScale#UNIVERSAL Universal Time}.
     *
     * If the time scale of this instance is the Universal Time, returns this instance,
     * otherwise prepares a corresponding instance in Universal Time.
     *
     * @return  instance in {@linkplain TimeScale#UNIVERSAL Universal Time}:
     *          this instance, if its time scale is the Universal Time,
     *          or a corresponding instance otherwise
     */
    abstract public UniversalTimelinePoint toUniversalTime();

    /**
     * Expresses this timeline point in the main calendar.
     *
     * @return  calendar point (of the main calendar)
     */
    public CalendarPoint toCalendarPoint() {
        if (calendarPoint == null)
            calendarPoint = Timeline.julianDayToCalendar(julianDay);
        return calendarPoint;
    }

    /**
     * Expresses this timeline point of the proleptic Gregorian calendar.
     *
     * @return  calendar point of the proleptic Gregorian calendar
     */
    public ProlepticGregorianCalendarPoint toProlepticGregorianCalendar() {
        return Timeline.julianDayToProlepticGregorianCalendar(julianDay);
    }

    /**
     * Expresses this timeline point of the proleptic Julian calendar.
     *
     * @return  calendar point of the proleptic Julian calendar
     */
    public ProlepticJulianCalendarPoint toProlepticJulianCalendar() {
        return Timeline.julianDayToProlepticJulianCalendar(julianDay);
    }

    /**
     * Expresses this timeline point as centurial T:
     * number of {@linkplain Timeline#JULIAN_CENTURY_DAYS Julian centuries}
     * from {@linkplain Timeline#EPOCH_2000_JD Epoch 2000}.
     *
     * @return  centurial T, the number of
     *          {@linkplain Timeline#JULIAN_CENTURY_DAYS Julian centuries}
     *          from {@linkplain Timeline#EPOCH_2000_JD Epoch 2000}
     */
    public double toCenturialT() {
        return Timeline.julianDayToCenturialT(julianDay);
    }

    /**
     * Expresses this timeline point as millenial τ (tau):
     * number of {@linkplain Timeline#JULIAN_MILLENIUM_DAYS Julian millenia}
     * from {@linkplain Timeline#EPOCH_2000_JD Epoch 2000}.
     *
     * @return  millenial τ (tau), the number of
     *          {@linkplain Timeline#JULIAN_MILLENIUM_DAYS Julian millenia}
     *          from {@linkplain Timeline#EPOCH_2000_JD Epoch 2000}
     */
    public double toMillenialTau() {
        return Timeline.julianDayToMillenialTau(julianDay);
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
        return ofJulianDayByTimeScale(julianDay + addendDays, getTimeScale());
    }

    /**
     * Equivalence check applying the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit}:
     * checks whether the other object (timeline point) is an equivalent of this, i.e. whether
     * they represent the same moment (equivalence unit) of time and belong to the same
     * {@linkplain TimeScale time scale}.
     *
     * @param o     other object (timeline point), to check its equivalence with this
     * @return      {@code true} - if the other point belongs to the same {@linkplain TimeScale time scale}
     *              and their {@linkplain #julianDay Julian Days} are close enough to be considered equal
     *              (belong to the same {@linkplain Timeline#getEquivUnitDays() equivalence unit});
     *              {@code false } - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimelinePoint)) {
            return false;
        }
        TimelinePoint point = (TimelinePoint) o;
        return getTimeScale() == point.getTimeScale()
            && NOMINAL_COMPARATOR.compare(this, point) == 0;
    }

    /**
     * Equivalence check applying {@linkplain Timeline#getEquivUnitDays() the timeline's equivalence unit}:
     * generates a {@linkplain Object#hashCode() hash code} identifying the combination
     * of this timeline point's {@linkplain TimeScale time scale} and of the equivalence unit
     * its {@linkplain #julianDay Julian Day} belongs to.
     *
     * @return  {@linkplain Object#hashCode() hash code} identifying the combination
     *          of this timeline point's {@linkplain TimeScale time scale} and of
     *          the equivalence unit its {@linkplain #julianDay Julian Day} belongs to
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getTimeScale(),
            Timeline.roundToEquivUnit(julianDay)
        );
    }

    /**
     * Obtains the comparator of timeline points ordering them
     * by nominal Julian Day value (regardless of whether it is Julian Day
     * in {@linkplain TimeScale#UNIVERSAL Universal Time} or Julian Ephemeris Day
     * in {@linkplain TimeScale#DYNAMICAL Dynamical Time}, and so with no regard
     * to relation of the actual time points they represent),
     * applying the {@linkplain Timeline#getEquivUnitDays() timeline's equivalence unit}.
     *
     * @return  comparator of timeline points by nominal Julian Day value
     *          (with no regard to their {@linkplain TimeScale time scales}),
     *          applying {@linkplain Timeline#getEquivUnitDays() the timeline's equivalence unit}
     * @see     #equals(Object)
     */
    public static Comparator<TimelinePoint> nominalComparator() {
        return NOMINAL_COMPARATOR;
    }

    /**
     * Expresses this timeline point textually in
     * a {@linkplain CalendarPoint#formatDateTimeToMinutes() calendaric format},
     * adding the {@linkplain TimeScale#mainAbbreviation time scale determiner}.
     *
     * @return  textual expression in a
     *          {@linkplain CalendarPoint#formatDateTimeToMinutes() calendaric format},
     *          with the {@linkplain TimeScale#mainAbbreviation time scale determiner} added
     */
    public String formatCalendrically() {
        return toCalendarPoint().formatDateTimeToMinutes() + " " + getTimeScale().mainAbbreviation;
    }

    @Override
    public String toString() {
        return String.format("(%s) %f %s",
            TimelinePoint.class.getSimpleName(),
            julianDay,
            getTimeScale().julianDayAbbreviation
        );
    }
}
