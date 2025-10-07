package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * Supplier of initial (empty) sets and maps
 * to use in process of composing and calculating.
 * Choice of right implementations for given key type
 * (e.g. {@link EnumSet} and {@link EnumMap} for an enum type)
 * may slightly improve performance.
 *
 * @param <KeyT>    type of keys identifying the quantities under calculation
 */
public interface KeyUtil<KeyT> {

    public default Set<KeyT> getSet() {
        return new HashSet<>();
    }

    public default <V> Map<KeyT, V> getMap() {
        return new HashMap<>();
    }
}
