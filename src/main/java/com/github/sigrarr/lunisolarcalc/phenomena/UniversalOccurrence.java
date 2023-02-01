package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.time.*;

/**
 * An {@link Occurrence occurrence} of a certain type or stage of some astronomical phenomenon
 * in {@link TimeScale#UNIVERSAL Universal Time}.
 *
 * @param <T>   {@link Enum} representing a type or stage of astronomical phenomenon whose occurence is stored,
 *              eg. {@link MoonPhase} or {@link SunSeasonPoint}
 * @see         DynamicalOccurrence
 */
public class UniversalOccurrence<T extends Enum<T>> extends Occurrence<T> implements Comparable<UniversalOccurrence<?>> {
    /**
     * Constructs an instance with a timeline point and a specified type or stage
     * of astronomical phenomenon. If the given timeline point belongs to the
     * {@link TimeScale#DYNAMICAL Dynamical Time}, it will be
     * {@link TimelinePoint#toUniversalTime() converted to Universal Time} first.
     *
     * @param timelinePoint     time of occurence
     * @param type              type or stage of astronomical phenomenon
     */
    public UniversalOccurrence(TimelinePoint timelinePoint, T type) {
        super(timelinePoint.toUniversalTime(), type);
    }

    /**
     * Constructs an instance with a {@link TimelinePoint#julianDay Julian Day} number
     * and a specified type or stage of astronomical phenomenon.
     *
     * @param julianEphemerisDay
     * @param type
     */
    public UniversalOccurrence(double julianDay, T type) {
        super(new UniversalTimelinePoint(julianDay), type);
    }

    @Override
    public UniversalTimelinePoint getTimelinePoint() {
        return (UniversalTimelinePoint) timelinePoint;
    }

    @Override
    public UniversalOccurrence<T> toUniversalTime() {
        return this;
    }

    @Override
    public DynamicalOccurrence<T> toDynamicalTime() {
        return new DynamicalOccurrence<>(timelinePoint, type);
    }

    /**
     * Compares this occurrence to the other of the same {@link TimeScale time scale}:
     * (1) chronologically, ie. {@link UniversalTimelinePoint#compareTo(UniversalTimelinePoint) by timeline point},
     * (2) by {@link #getType() type} class name, (3) by type value.
     *
     * {@link Comparable Consistent} with {@link #equals(Object) equivalence-check}.
     *
     * @param occurrence    occurrence to compare to
     * @return              result of comparison
     *                      (1) {@link UniversalTimelinePoint#compareTo(UniversalTimelinePoint) by timeline point},
     *                      (2) by {@link #getType() type} class name, (3) by type value
     *                      (in the {@link Comparable#compareTo(Object) parent interface's} format)
     */
    @Override
    public int compareTo(UniversalOccurrence<?> occurrence) {
        int chronoCmp = getTimelinePoint().compareTo(occurrence.getTimelinePoint());
        return chronoCmp != 0 ? chronoCmp : BY_TYPE_COMPARATOR.compare(this, occurrence);
    }
}
