package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * A provider of one quantity, i.e. a calculator of values of one quantity.
 *
 * It should be 'atomic', i.e. it should {@linkplain #calculate(Object, Map) calculate}
 * a single value, rather than a complex one (the distinction depending on the context).
 *
 * @param <KeyT>    type of keys identifying the quantities under calculation
 * @param <InT>     type of a root input passed to a composed calculation
 * @see             CalculationComposer
 */
public interface Provider<KeyT, InT> {
    /**
     * Specifies the quantity provided by this object.
     *
     * @return  quantity provided by this object
     */
    public KeyT provides();

    /**
     * Specifies the set of quantities whose values are required by this provider
     * in order to perform its own {@linkplain #calculate(Object, Map) calculation}.
     *
     * If this provider operates solely on a root input and does not require
     * other values, returns {@linkplain EnumSet#noneOf(Class) an empty set}, not null.
     *
     * @return  set of quantities whose values are required by this provider
     *          in order to perform its own {@linkplain #calculate(Object, Map) calculation}
     *          (may be empty, must not be null)
     */
    public Set<KeyT> requires();

    /**
     * Calculates a value of the {@linkplain #provides() provided} quantity.
     * It is strongly recommended that the output type of this method
     * be specified in a concrete provider.
     *
     * Uses a root input, which is the same for each provider in the composition,
     * and a map which must contain a value for each {@linkplain #requires() required} quantity.
     * An object retrieved from the map may require casting; it should be the same as returned
     * by this method called on the provider of its key quantity.
     *
     * @param rootInput             root input passed to the whole composed calculation
     * @param precalculatedValues   a map containing a value for each
     *                              {@linkplain #requires() required} quantity
     * @return                      value of the {@linkplain #provides() provided} quantity
     */
    public Object calculate(InT rootInput, Map<KeyT, Object> precalculatedValues);

    /**
     * Obtains an instance of this provider class to be used in a new composition.
     *
     * In the case of a stateless calculator it is presumably this object itself.
     *
     * @return  an instance of this provider class to be used in a new composition
     */
    default public Provider<KeyT, InT> getInstanceForNewComposition() {
        return this;
    }
}
