package com.github.sigrarr.lunisolarcalc.phenomena;

public class FoundPhenomenon<T extends Enum<T>> implements Comparable<FoundPhenomenon<T>> {

    public final double julianEphemerisDay;
    public final T instant;

    public FoundPhenomenon(double julianEphemerisDay, T instant) {
        this.julianEphemerisDay = julianEphemerisDay;
        this.instant = instant;
    }

    @Override
    public int compareTo(FoundPhenomenon<T> o) {
        return Double.compare(julianEphemerisDay, o.julianEphemerisDay);
    }
}
