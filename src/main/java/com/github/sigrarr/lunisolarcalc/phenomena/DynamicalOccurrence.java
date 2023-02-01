package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.time.*;

/**
 * An {@link Occurrence occurrence} of a certain type or stage of some astronomical phenomenon
 * in {@link TimeScale#DYNAMICAL Dynamical Time}.
 *
 * @param <T>   {@link Enum} representing a type or stage of astronomical phenomenon whose occurence is stored,
 *              eg. {@link MoonPhase} or {@link SunSeasonPoint}
 * @see         UniversalOccurrence
 */
public class DynamicalOccurrence<T extends Enum<T>> extends Occurrence<T> implements Comparable<DynamicalOccurrence<?>> {
    /**
     * Constructs an instance with a timeline point and a specified type or stage
     * of astronomical phenomenon. If the given timeline point belongs to the
     * {@link TimeScale#UNIVERSAL Universal Time}, it will be
     * {@link TimelinePoint#toDynamicalTime() converted to Dynamical Time} first.
     *
     * @param timelinePoint     time of occurence
     * @param type              type or stage of astronomical phenomenon
     */
    public DynamicalOccurrence(TimelinePoint timelinePoint, T type) {
        super(timelinePoint.toDynamicalTime(), type);
    }

    /**
     * Constructs an instance with a {@link TimelinePoint#julianDay Julian Ephemeris Day}
     * number and a specified type or stage of astronomical phenomenon.
     *
     * @param julianEphemerisDay
     * @param type
     */
    public DynamicalOccurrence(double julianEphemerisDay, T type) {
        super(new DynamicalTimelinePoint(julianEphemerisDay), type);
    }

    @Override
    public DynamicalTimelinePoint getTimelinePoint() {
        return (DynamicalTimelinePoint) timelinePoint;
    }

    @Override
    public UniversalOccurrence<T> toUniversalTime() {
        return new UniversalOccurrence<>(timelinePoint, type);
    }

    @Override
    public DynamicalOccurrence<T> toDynamicalTime() {
        return this;
    }

    /**
     * Compares this occurrence to the other of the same {@link TimeScale time scale}:
     * (1) chronologically, ie. {@link DynamicalTimelinePoint#compareTo(DynamicalTimelinePoint) by timeline point},
     * (2) by {@link #getType() type} class name, (3) by type value.
     *
     * {@link Comparable Consistent} with {@link #equals(Object) equivalence-check}.
     *
     * @param occurrence    occurrence to compare to
     * @return              result of comparison
     *                      (1) {@link DynamicalTimelinePoint#compareTo(DynamicalTimelinePoint) by timeline point},
     *                      (2) by {@link #getType() type} class name, (3) by type value
     *                      (in the {@link Comparable#compareTo(Object) parent interface's} format)
     */
    @Override
    public int compareTo(DynamicalOccurrence<?> occurrence) {
        int chronoCmp = getTimelinePoint().compareTo(occurrence.getTimelinePoint());
        return chronoCmp != 0 ? chronoCmp : BY_TYPE_COMPARATOR.compare(this, occurrence);
    }
}
