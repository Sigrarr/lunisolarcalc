package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * A composer of {@link Composition calculations}
 * which yield values of enumerated quantities called "subjects".
 *
 * Gathers {@link Provider providers} of the subjects and composes them
 * into a calculation of a selected subject or a set of subjects.
 * Resolves dependencies (detecting circles), avoids redundancy.
 *
 * To utilze this tool optimally, {@link #register(Provider) registered} providers
 * should be 'atomic': each being a simple calculator of a single value.
 *
 * @param <SubjectT>    {@link Enum enumeration type} of the quantities under calculation,
 *                      containing all available "subjects"
 * @param <InT>         type of a root input passed to a composed calculation
 */
public class CalculationComposer<SubjectT extends Enum<SubjectT>, InT> {

    protected final ProvidersRegister<SubjectT, InT> register;
    protected final Class<SubjectT> subjectEnumClass;

    /**
     * Constructs a new calculation composer.
     *
     * @param subjectEnumClass  class of the {@link Enum enumeration} of the quantities under
     *                          calculation, containing all available "subjects"
     */
    public CalculationComposer(Class<SubjectT> subjectEnumClass) {
        register = new ProvidersRegister<>(subjectEnumClass);
        this.subjectEnumClass = subjectEnumClass;
    }

    /**
     * Registers a new provider of one subject of calculation (a calculator).
     *
     * @param provider  a new provider of one subject of calculation (a calculator)
     */
    public void register(Provider<SubjectT, InT> provider) {
        register.add(provider);
    }

    /**
     * Checks whether a provider of a specified subject has been registered
     * in this composer.
     *
     * @param subject   subject of calculation
     * @return          {@code true} - if a provider of the subject has been already
     *                  registered in this composer; {@code false} - otherwise
     */
    public boolean hasProvider(SubjectT subject) {
        return register.has(subject);
    }

    /**
     * Composes a new calculation which will yield values of a quantity
     * represented by a specified subject (called "target").
     *
     * @param target    requested target subject
     * @return          newly composed calculation which will yield values
     *                  of the quantity represented by the target subject
     */
    public SingleOutputComposition<SubjectT, InT> compose(SubjectT target) {
        EnumSet<SubjectT> targets = EnumSet.of(target);
        return getNewCompositionBuilder(targets).buildSingleOutputComposition();
    }

    /**
     * Composes a new calculation which will yield values of quantities
     * represented by a specified set of subjects (called "targets" or "target subjects").
     * Values will be returned in the form of a subject-value map.
     *
     * @param targets   requested set of target subjects
     * @return          newly composed calculation which will yield values
     *                  of quantities represented by the target subjects
     *                  (in the form of a subject-value map)
     */
    public MultiOutputComposition<SubjectT, InT> compose(EnumSet<SubjectT> targets) {
        return getNewCompositionBuilder(targets).buildMultiOutputComposition();
    }

    private CompositionBuilder<SubjectT, InT> getNewCompositionBuilder(EnumSet<SubjectT> targets) {
        return new CompositionBuilder<SubjectT, InT>(this, targets);
    }
}
