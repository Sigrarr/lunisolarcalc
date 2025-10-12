package com.github.sigrarr.lunisolarcalc.coords.local;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

public class RhoCosPhiPrimCalculator implements Provider<Key, TimelinePoint>
{

    final GeoPosition geoPosition;

    public RhoCosPhiPrimCalculator(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public double calculate() {
        return Math.cos(Topo.calculateU(geoPosition.coords.getLatitude()))
            + geoPosition.elevation/ConstantsAndUnits.EARTH_EQUATORIAL_RADIUS_METERS
                * Math.cos(geoPosition.coords.getLatitude());
    }

    @Override
    public Key provides() {
        return LocalCoord.RHO_COS_PHI_PRIM.key();
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
