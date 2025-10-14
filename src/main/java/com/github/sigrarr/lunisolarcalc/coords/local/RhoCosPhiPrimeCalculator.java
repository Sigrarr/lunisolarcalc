package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@link LocalCoord#RHO_COS_PHI_PRIME ρ*cos(φ′) quantity}.
 * Quick, {@linkplain CalculationComposer composable},
 * pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see Topo
 */
public final class RhoCosPhiPrimeCalculator implements Provider<Key, TimelinePoint>
{

    final GeoPosition geoPosition;

    public RhoCosPhiPrimeCalculator(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    /**
     * Calculates {@link LocalCoord#RHO_COS_PHI_PRIME ρ*cos(φ′)}.
     *
     * @return  {@link LocalCoord#RHO_COS_PHI_PRIME ρ*cos(φ′)}
     */
    public double calculate() {
        return Topo.calculateRhoCosPhiPrime(geoPosition.coords.getLatitude(), geoPosition.elevation);
    }

    @Override
    public Key provides() {
        return LocalCoord.RHO_COS_PHI_PRIME.key();
    }

    @Override
    public Set<Key> requires() {
        return Sets.empty();
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return calculate();
    }
}
