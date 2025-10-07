package com.github.sigrarr.lunisolarcalc.coords.global;

import java.util.*;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.Provider;

/**
 * Adapter of the provider of the {@link GlobalCoord} allowing its use in
 * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer calc. compositions}
 * working on the general {@linkplain Key Keys}.
 *
 * @see CoordsCalcCompositions
 */
public class GlobalCoordCalcKeyAdapter implements Provider<Key, TimelinePoint> {

    private final Provider<GlobalCoord, TimelinePoint> core;

    public GlobalCoordCalcKeyAdapter(Provider<GlobalCoord, TimelinePoint> core) {
        this.core = core;
    }

    @Override
    public Key provides() {
        return core.provides().key();
    }

    @Override
    public Set<Key> requires() {
        return core.requires().stream().map(GlobalCoord::key).collect(Collectors.toSet());
    }

    @Override
    public Object calculate(TimelinePoint tx, Map<Key, Object> precalculatedValues) {
        return core.calculate(
            tx,
            core.requires().stream().collect(Collectors.toMap(
                c -> c,
                c -> precalculatedValues.get(c.key())
            ))
        );
    }

    @Override
    public Provider<Key, TimelinePoint> getInstanceForNewComposition() {
        Provider<GlobalCoord, TimelinePoint> coreCopy = core.getInstanceForNewComposition();
        return core == coreCopy ? this : new GlobalCoordCalcKeyAdapter(coreCopy);
    }
}
