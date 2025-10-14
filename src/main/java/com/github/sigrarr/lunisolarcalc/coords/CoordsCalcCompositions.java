package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.global.*;
import com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord;
import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Shortcut for {@linkplain CalculationComposer composing calculations} of quantities supported by this package.
 * Use it to conveniently compose a calculation for a {@link GlobalCoord}, a {@link LocalCoord} or a combination,
 * instead of managing calculators manually. It may help in resolving dependencies and avoinding redundancy.
 */
public abstract class CoordsCalcCompositions {

    private static final CalculationComposer<GlobalCoord, TimelinePoint> closedGlobalComposer = prepareClosedGlobalComposer();

    /**
     * Prepare a calc. composition which will calculate values
     * of the requested quantity ("target").
     *
     * @param   target  requested quantity
     * @return          calc. composition
     */
    public static CalcComposition<GlobalCoord, TimelinePoint> compose(GlobalCoord target) {
        return closedGlobalComposer.compose(target);
    }

    /**
     * Prepare a calc. composition which will calculate values
     * of the requested quantities ("targets").
     *
     * @param targets   set of requested quantities
     * @return          calc. composition
     */
    public static MultiCalcComposition<GlobalCoord, TimelinePoint> compose(Set<GlobalCoord> targets) {
        return closedGlobalComposer.compose(targets);
    }

    /**
     * Prepare a calc. composition which will calculate values
     * of the requested quantity ("target"),
     * for given observer's position on Earth (if applicable).
     *
     * @param target        key identifying the requested quantity
     *                      (e.g. obtained with {@link GlobalCoord#key()} or {@link LocalCoord#key()})
     * @param geoPosition   the observer's position on Earth
     * @return              calc. composition
     */
    public static CalcComposition<Key, TimelinePoint> compose(Key target, GeoPosition geoPosition) {
        return prepareGeneralComposer(geoPosition).compose(target);
    }

    /**
     * Prepare a calc. composition which will calculate values
     * of the requested quantities ("targets"),
     * for given observer's position on Earth (if applicable).
     *
     * @param targets       set of keys identifying the requested quantities
     *                      (e.g. obtained with {@link GlobalCoord#key()} or {@link LocalCoord#key()})
     * @param geoPosition   the observer's position on Earth
     * @return              calc. composition
     */
    public static MultiCalcComposition<Key, TimelinePoint> compose(Set<Key> targets, GeoPosition geoPosition) {
        return prepareGeneralComposer(geoPosition).compose(targets);
    }

    private static CalculationComposer<Key, TimelinePoint> prepareGeneralComposer(GeoPosition geoPosition) {
        CalculationComposer<Key, TimelinePoint> composer = new CalculationComposer<>();
        Arrays.stream(GlobalCoord.values()).map(GlobalCoord::getProvider).map(GlobalCoordCalcKeyAdapter::new).forEach(composer::register);
        Arrays.stream(LocalCoord.values()).map(lc -> lc.getProvider(geoPosition)).forEach(composer::register);
        return composer;
    }

    private static CalculationComposer<GlobalCoord, TimelinePoint> prepareClosedGlobalComposer() {
        CalculationComposer<GlobalCoord, TimelinePoint> composer = new CalculationComposer<>(new GCKeyUtil());
        Arrays.stream(GlobalCoord.values()).map(GlobalCoord::getProvider).forEach(composer::register);
        return composer;
    }

    private static class GCKeyUtil implements KeyUtil<GlobalCoord> {
        @Override public Set<GlobalCoord> getSet() {
            return EnumSet.noneOf(GlobalCoord.class);
        }
        @Override public <V> Map<GlobalCoord, V> getMap() {
            return new EnumMap<>(GlobalCoord.class);
        }
    }
}
