package com.github.sigrarr.lunisolarcalc.coords;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord;
import com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord;

/**
 * A key identyfing a calculable quantity.
 *
 * This class is primarily meant to serve as a kind of union of {@link GlobalCoord} and {@link LocalCoord}
 * and is used as a generic key parameter for
 * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer composing calculations}
 * (it is not restricted to adapting these coordinate classes though).
 *
 * Typically an isntance is to obtain with the method {@link GlobalCoord#key()} or {@link LocalCoord#key()}.
 *
 * @see CoordsCalcCompositions
 */
public class Key {
    /**
     * An instance is meant to identify a calculable quantity.
     */
    public static interface QuantityIndetifier {
        /**
         * Convert to a general key which may identify this quantity
         * alongside other ones of different types.
         *
         * @return  general key
         */
        public Key key();
        /**
         * Get a unique name of this quantity.
         *
         * @return unique name of this quantity
         */
        public String name();
    }

    private final String name;

    private Key(String name) {
        this.name = name;
    }

    /**
     * Obtain a general key of given global coord type.
     *
     * @param coord     global coord (enum value)
     * @return          key instance
     */
    public static Key ofGlobalCoord(GlobalCoord coord) {
        return ofInterface(coord);
    }

    /**
     * Obtain a general key of given local coord type.
     *
     * @param coord     local coord (enum value)
     * @return          key instance
     */
    public static Key ofLocalCoord(LocalCoord coord) {
        return ofInterface(coord);
    }

    /**
     * Obtain a general key of an instance of any class
     * meant to identify a calculable quantity
     * (possibly but not necessarily an enum).
     *
     * Note that you can't use custom quantities (or representations)
     * in {@link CoordsCalcCompositions}, you have to register their providers in your own
     * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer calculation composer}.
     *
     * @param quantityIdentifier    object identifying a calculable quantity
     * @return                      key instance
     */
    public static Key ofAnyQuantityIdentifier(QuantityIndetifier quantityIdentifier) {
        return ofInterface(quantityIdentifier);
    }

    private static Key ofInterface(QuantityIndetifier qi) {
        return new Key(qi.name());
    }

    /**
     * Obtain a general key of a unique name of a calculable quantity.
     *
     * Note that you can't use custom quantities (or representations)
     * in {@link CoordsCalcCompositions}, you have to register their providers in your own
     * {@linkplain com.github.sigrarr.lunisolarcalc.util.calccomposition.CalculationComposer calculation composer}.
     *
     * @param name  unique name of a calculable quantity
     * @return      key instance
     */
    public static Key ofCustomName(String name) {
        return new Key(name);
    }

    /**
     * Get name of the quantity represented by this key.
     *
     * @return name of the quantity represented by this key
     */
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Key && ((Key) o).name.equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
