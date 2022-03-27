package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.time.TimeType;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

public class FoundCyclicPhenomenon<T extends Enum<T>> implements Comparable<FoundCyclicPhenomenon<T>> {

    public final TimelinePoint ephemerisTimelinePoint;
    public final T stage;

    public FoundCyclicPhenomenon(double julianEphemerisDay, T stage) {
        this(new TimelinePoint(julianEphemerisDay, TimeType.DYNAMICAL), stage);
    }

    public FoundCyclicPhenomenon(TimelinePoint timelinePoint, T stage) {
        this.ephemerisTimelinePoint = timelinePoint.timeType == TimeType.DYNAMICAL ? timelinePoint : timelinePoint.convertToDynamicalTime();
        this.stage = stage;
    }

    @Override
    public int compareTo(FoundCyclicPhenomenon<T> o) {
        return ephemerisTimelinePoint.compareTo(o.ephemerisTimelinePoint);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FoundCyclicPhenomenon)) {
            return false;
        }
        FoundCyclicPhenomenon<?> cph = (FoundCyclicPhenomenon<?>) o;
        return stage == cph.stage && ephemerisTimelinePoint.equals(cph.ephemerisTimelinePoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage, ephemerisTimelinePoint);
    }
}
