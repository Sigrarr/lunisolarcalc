package com.github.sigrarr.lunisolarcalc.phenomena;

public class FoundCyclicPhenomenon<T extends Enum<T>> implements Comparable<FoundCyclicPhenomenon<T>> {

    public final double julianEphemerisDay;
    public final T stage;

    public FoundCyclicPhenomenon(double julianEphemerisDay, T stage) {
        this.julianEphemerisDay = julianEphemerisDay;
        this.stage = stage;
    }

    @Override
    public int compareTo(FoundCyclicPhenomenon<T> o) {
        return Double.compare(julianEphemerisDay, o.julianEphemerisDay);
    }
}
