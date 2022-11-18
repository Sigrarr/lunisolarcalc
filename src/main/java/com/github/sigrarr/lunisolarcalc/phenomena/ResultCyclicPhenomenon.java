package com.github.sigrarr.lunisolarcalc.phenomena;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;

public class ResultCyclicPhenomenon<T extends Enum<T>> implements Comparable<ResultCyclicPhenomenon<T>> {

    public final TimelinePoint ephemerisTimelinePoint;
    public final T stage;

    public ResultCyclicPhenomenon(double julianEphemerisDay, T stage) {
        this(TimelinePoint.ofJulianEphemerisDay(julianEphemerisDay), stage);
    }

    public ResultCyclicPhenomenon(TimelinePoint timelinePoint, T stage) {
        this.ephemerisTimelinePoint = timelinePoint.toDynamicalTime();
        this.stage = stage;
    }

    @Override
    public int compareTo(ResultCyclicPhenomenon<T> o) {
        return ephemerisTimelinePoint.compareTo(o.ephemerisTimelinePoint);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResultCyclicPhenomenon)) {
            return false;
        }
        ResultCyclicPhenomenon<?> cph = (ResultCyclicPhenomenon<?>) o;
        return stage == cph.stage && ephemerisTimelinePoint.equals(cph.ephemerisTimelinePoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage, ephemerisTimelinePoint);
    }
}
