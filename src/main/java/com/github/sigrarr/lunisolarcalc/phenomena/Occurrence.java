package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;

/**
 * An occurrence of a certain type or stage of some astronomical phenomenon,
 * typically a found instant of specific {@link MoonPhase phase of the Moon} or {@link SunSeasonPoint Equinox/Solstice}.
 *
 * The {@link #compareTo(Occurrence) natural ordering} reflects that {@link TimelinePoint#compareTo(TimelinePoint) of TimelinePoint};
 * note that it may be inconsistent with {@link #equals(Object) equivalence} in case of {@link #type} differences
 * (a {@link #equivalenceConsistentComparator() consistent comparator} is also available).
 *
 * @param <T>   {@link Enum} representing a type or stage of astronomical phenomenon whose occurence is stored,
 *              eg. {@link MoonPhase} or {@link SunSeasonPoint}
 */
public class Occurrence<T extends Enum<T>> implements Comparable<Occurrence<?>> {
    /**
     * Time of occurence.
     */
    public final TimelinePoint timelinePoint;
    /**
     * Type or stage of astronomical phenomenon.
     */
    public final T type;

    /**
     * Constructs an instance representing an occurence of the given type or stage of astronomical phenomenon.
     *
     * @param timelinePoint     time of occurence
     * @param typetype          or stage of astronomical phenomenon
     */
    public Occurrence(TimelinePoint timelinePoint, T type) {
        this.timelinePoint = timelinePoint;
        this.type = type;
    }

    /**
     * Constructs an instance representing an occurence of the given type or stage of astronomical phenomenon.
     *
     * @param julianEphemerisDay    time of occurence, in Julian Ephemeris Day ({@link TimeScale#DYNAMICAL Dynamical Time})
     * @param type                  type or stage of astronomical phenomenon
     */
    public Occurrence(double julianEphemerisDay, T type) {
        this(TimelinePoint.ofJulianEphemerisDay(julianEphemerisDay), type);
    }

    /**
     * Obtains a corresponding instance with a timeline point in {@link TimeScale#UNIVERSAL Universal Time}.
     *
     * If the time scale of this instance's timeline point is the Universal Time,
     * returns this instance, otherwise prepares a new instance in Universal Time.
     *
     * @return  instance with a timeline point in {@link TimeScale#UNIVERSAL Universal Time}:
     *          this instance, if its timeline point's time scale is the Universal Time,
     *          or a new instance otherwise
     */
    public Occurrence<T> toUniversalTime() {
        return toTimeScale(TimeScale.UNIVERSAL);
    }

    /**
     * Obtains a corresponding instance with a timeline point in {@link TimeScale#DYNAMICAL Dynamical Time}.
     *
     * If the time scale of this instance's timeline point is the Dynamical Time,
     * returns this instance, otherwise prepares a new instance in Dynamical Time.
     *
     * @return  instance with a timeline point in {@link TimeScale#DYNAMICAL Dynamical Time}:
     *          this instance, if its timeline point's time scale is the Dynamical Time,
     *          or a new instance otherwise
     */
    public Occurrence<T> toDynamicalTime() {
        return toTimeScale(TimeScale.DYNAMICAL);
    }

    /**
     * Obtains a corresponding instance with a timeline point in a selected time scale.
     *
     * If the time scale of this instance's timeline point is the same as the selected,
     * returns this instance, otherwise prepares a new instance in the other time scale.
     *
     * @return  instance with a timeline point in the selected time scale:
     *          this instance, if its timeline point's time scale is the selected one,
     *          or a new instance otherwise
     */
    public Occurrence<T> toTimeScale(TimeScale targetTimeScale) {
        return timelinePoint.timeScale == targetTimeScale ?
            this : new Occurrence<>(timelinePoint.toTimeScale(targetTimeScale), type);
    }

    /**
     * Equivalence check: checks whether the other object (Occurrence) is an equivalent of this,
     * i.e. whether both have {@link TimelinePoint#equals(Object) equal timeline points}
     * and the same type (of the same class and value).
     *
     * @param o     other object (Occurrence), to check its equivalence with this
     * @return      {@code true} - if the other occurence and this have
     *              {@link TimelinePoint#equals(Object) equal timeline points} and the same type
     *              (of the same class and value);
     *              {@code false } - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Occurrence)) {
            return false;
        }
        Occurrence<?> occurrence = (Occurrence<?>) o;
        return type == occurrence.type && timelinePoint.equals(occurrence.timelinePoint);
    }

    /**
     * Equivalence check: generates a {@link Object#hashCode() hash code} identifying the combination
     * of this occurrence's {@link TimelinePoint#hashCode() timeline point} and type.
     *
     * @return  {@link Object#hashCode() hash code} identifying the combination
     *          of this occurrence's {@link TimelinePoint#hashCode() timeline point} and type
     */
    @Override
    public int hashCode() {
        return Objects.hash(timelinePoint, type);
    }

    /**
     * Compares this occurrence to the other - chronologically,
     * i.e. {@link TimelinePoint#compareTo(TimelinePoint) by timeline point}.
     *
     * Note that if the other occurrence's type is different than this one's
     * but their timeline points are equal, the result will be
     * {@link Comparable inconsistent with equivalence-check}.
     *
     * @param o     the occurrence to compare to
     * @return      result of comparison {@link TimelinePoint#compareTo(TimelinePoint) by timeline point},
     *              in the {@link Comparable#compareTo(Object) parent interface's} format
     * @see         #equivalenceConsistentComparator()
     */
    @Override
    public int compareTo(Occurrence<?> o) {
        return timelinePoint.compareTo(o.timelinePoint);
    }

    /**
     * Obtains the {@link Comparable equivalence-consistent} chronological comparator of occurrences
     * (comparing mainly {@link TimelinePoint#compareTo(TimelinePoint) by timeline point}).
     *
     * It is intented for cases when such consistency is important,
     * e.g. when Occurrence instances are stored in a {@link java.util.TreeMap TreeMap}
     * or a {@link java.util.TreeSet TreeSet}.
     *
     * Unlike the {@link #compareTo(Occurrence) Occurrence's natural ordering},
     * it doesn't equate instances of equal timeline points but different types.
     *
     * Levels of comparing: 1. {@link TimelinePoint#compareTo(TimelinePoint) by timeline point},
     * 2. by type class (name), 3. {@link Enum#compareTo(Enum) by type value},
     * 4. by timeline point's {@link TimelinePoint#timeScale time scale}.
     *
     * @return  {@link Comparable equivalence-consistent} chronological comparator of occurrences
     *          (comparing mainly {@link TimelinePoint#compareTo(TimelinePoint) by timeline point})
     * @see     #equals(Object)
     */
    public static Comparator<Occurrence<?>> equivalenceConsistentComparator() {
        return (a, b) -> {
            int cmp = a.compareTo(b);
            if (cmp != 0)
                return cmp;
            if (a.type.getClass() != b.type.getClass())
                return a.type.getClass().getName().compareTo(b.type.getClass().getName());
            if (a.type != b.type)
                return Integer.compare(a.type.ordinal(), b.type.ordinal());
            return a.timelinePoint.timeScale.compareTo(b.timelinePoint.timeScale);
        };
    }

    @Override
    public String toString() {
        return String.format("(%s) %s.%s @ %s",
            getClass().getSimpleName(),
            type.getClass().getSimpleName(),
            type,
            timelinePoint
        );
    }
}
