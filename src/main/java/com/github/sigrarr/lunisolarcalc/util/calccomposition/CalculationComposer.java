package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * A composer of calculations.
 *
 * Gathers {@linkplain Provider providers} of the needed quantities
 * and composes them into a {@linkplain CalcComposition calculation of a selected quantity}
 * or {@linkplain MultiCalcComposition of a set of quantities}.
 * Resolves dependencies (detecting circles), avoids redundancy.
 *
 * To utilze this tool optimally, {@linkplain #register(Provider) registered} providers
 * should be 'atomic': each being a simple calculator of a single value.
 *
 * @param <KeyT>    type of keys identifying the quantities under calculation
 * @param <InT>     type of a root input passed to a composed calculation
 */
public class CalculationComposer<KeyT, InT> {

    protected final ProvidersRegister<KeyT, InT> register;

    /**
     * Constructs a new calculation composer.
     */
    public CalculationComposer() {
        register = new ProvidersRegister<>();
    }

    /**
     * Registers a new provider of one quantity (a calculator).
     *
     * @param provider  a new provider of one quantity (a calculator)
     */
    public void register(Provider<KeyT, InT> provider) {
        register.add(provider);
    }

    /**
     * Checks whether a provider of the specified quantity has already been registered
     * in this composer.
     *
     * @param quantityKey      key of the quantity
     * @return          {@code true} - if a provider of the quantity has already
     *                  been registered in this composer; {@code false} - otherwise
     */
    public boolean hasProvider(KeyT quantityKey) {
        return register.has(quantityKey);
    }

    /**
     * Composes a new calculation which will yield values of
     * the specified quantity (called "target").
     *
     * @param target    key of the requested target quantity
     * @return          newly composed calculation which will yield values
     *                  of the target quantity
     */
    public CalcComposition<KeyT, InT> compose(KeyT target) {
        Set<KeyT> targets = new HashSet<>();
        targets.add(target);
        return getNewCompositionBuilder(targets).buildSingleOutputComposition();
    }

    /**
     * Composes a new calculation which will yield values
     * of the specified quantities (called "targets").
     * Values will be returned in the form of a map.
     *
     * @param targets   set of keys of the requested quantities
     * @return          newly composed calculation which will yield values
     *                  of the target quantities (in the form of a map)
     */
    public MultiCalcComposition<KeyT, InT> compose(Set<KeyT> targets) {
        return getNewCompositionBuilder(targets).buildMultiOutputComposition();
    }

    private CompositionBuilder<KeyT, InT> getNewCompositionBuilder(Set<KeyT> targets) {
        return new CompositionBuilder<KeyT, InT>(this, targets);
    }
}
