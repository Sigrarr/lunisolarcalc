package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;

/**
 * An occurrence of a certain type or stage of some astronomical phenomenon,
 * typically an instant found by a finding tool.
 * Belongs to one of the {@linkplain TimeScale time scales}.
 *
 * Natural ordering for this abstract class is not defined;
 * in order to sort an array or a collection of occurrences,
 * it is recommended to {@linkplain #toDynamicalTime() project or convert them all to DynamicalTime}
 * or to {@linkplain #toUniversalTime() Universal Time}.
 *
 * @param <T>   {@link Enum} representing a type or stage of astronomical phenomenon whose occurence is stored,
 *              eg. {@link com.github.sigrarr.lunisolarcalc.phenomena.global.MoonPhase MoonPhase}
 *              or {@link com.github.sigrarr.lunisolarcalc.phenomena.global.SunSeasonPoint SunSeasonPoint}
 * @see         DynamicalOccurrence
 * @see         UniversalOccurrence
 */
public abstract class Occurrence<T extends Enum<T>> {

    protected final static Comparator<Occurrence<?>> BY_TYPE_COMPARATOR = (a, b) -> {
        int typeClassCmp = a.type.getClass().getName().compareTo(b.type.getClass().getName());
        return typeClassCmp != 0 ? typeClassCmp : Integer.compare(a.type.ordinal(), b.type.ordinal());
    };

    protected final TimelinePoint timelinePoint;
    protected final T type;

    Occurrence(TimelinePoint timelinePoint, T type) {
        this.timelinePoint = timelinePoint;
        this.type = type;
    }

    /**
     * Gets the time of occurence.
     */
    public TimelinePoint getTimelinePoint() {
        return timelinePoint;
    }

    /**
     * Gets the type or stage of astronomical phenomenon.
     */
    public T getType() {
        return type;
    }

    /**
     * Obtains an instance with a {@linkplain UniversalTimelinePoint timeline point in Universal Time}.
     *
     * If this instance belongs to the Universal Time, returns this instance,
     * otherwise prepares a corresponding instance in Universal Time.
     *
     * @return  instance with a {@linkplain UniversalTimelinePoint timeline point in Universal Time}:
     *          this instance, if it belongs to the Universal Time,
     *          or a corresponding instance otherwise
     */
    abstract public UniversalOccurrence<T> toUniversalTime();

    /**
     * Obtains an instance with a {@linkplain DynamicalTimelinePoint timeline point in Dynamical Time}.
     *
     * If this instance belongs to the Dynamical Time, returns this instance,
     * otherwise prepares a corresponding instance in Dynamical Time.
     *
     * @return  instance with a {@linkplain DynamicalTimelinePoint timeline point in Dynamical Time}:
     *          this instance, if it belongs the Dynamical Time,
     *          or a corresponding instance otherwise
     */
    abstract public DynamicalOccurrence<T> toDynamicalTime();

    /**
     * Equivalence check: checks whether the other object (Occurrence) is an equivalent of this,
     * i.e. whether they both store the same {@linkplain #getType() type/stage} of the same phenomenon
     * and have {@linkplain TimelinePoint#equals(Object) equal timeline points}
     * (belonging to the same {@linkplain TimeScale time scale} is necessary).
     *
     * @param o     other object (Occurrence), to check its equivalence with this
     * @return      {@code true} - if the other occurence and this
     *              have the same {@linkplain #getType() type} (of the same class and equal value)
     *              and {@linkplain TimelinePoint#equals(Object) equal timeline points}
     *              (belonging to the same {@linkplain TimeScale time scale} is necessary);
     *              {@code false } - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Occurrence)) {
            return false;
        }
        Occurrence<?> occurrence = (Occurrence<?>) o;
        return type.equals(occurrence.type) && timelinePoint.equals(occurrence.timelinePoint);
    }

    /**
     * Equivalence check: generates a {@linkplain Object#hashCode() hash code} identifying the combination
     * of this occurrence's {@linkplain TimelinePoint#hashCode() timeline point} and {@linkplain #getType() type}.
     *
     * @return  {@linkplain Object#hashCode() hash code} identifying the combination
     *          of this occurrence's {@linkplain TimelinePoint#hashCode() timeline point}
     *          and {@linkplain #getType() type}
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, timelinePoint);
    }

    @Override
    public String toString() {
        return String.format("(%s) %s.%s @ %s",
            Occurrence.class.getSimpleName(),
            type.getClass().getSimpleName(),
            type,
            timelinePoint
        );
    }
}
