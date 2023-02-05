package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * A provider of one subject, i.e. a calculator of values of a quantity
 * represented by one subject.
 *
 * It should be 'atomic', i.e. it should {@linkplain #calculate(Object, Map) calculate}
 * a single value, rather not a complex one (the distinction depending on the context).
 *
 * @param <SubjectT>    {@linkplain Enum enumeration type} of the quantities under calculation,
 *                      containing all available "subjects"
 * @param <InT>         type of a root input passed to a composed calculation
 * @see                 CalculationComposer
 */
public interface Provider<SubjectT extends Enum<SubjectT>, InT> {
    /**
     * Specifies the set of subjects whose values are required by this provider
     * in order to perform its own {@linkplain #calculate(Object, Map) calculation}.
     *
     * If this provider operates solely on a root input and does not require
     * values of any subjects, returns {@linkplain EnumSet#noneOf(Class) an empty set}, not null.
     *
     * @return  set of subjects whose values are required by this provider
     *          in order to perform its own {@linkplain #calculate(Object, Map) calculation}
     *          (may be empty, must not be null)
     */
    public EnumSet<SubjectT> requires();

    /**
     * Specifies the subject of calculation provided by this object.
     *
     * @return  subject of calculation provided by this object
     */
    public SubjectT provides();

    /**
     * Calculates a value of the subject {@linkplain #provides() provided} by this object.
     * It is strongly recommended that the output type of this method
     * be specified in a concrete provider.
     *
     * Uses a root input, which is the same for each provider in the composition,
     * and a subject-value map which must contain a value for each subject
     * {@linkplain #requires() required} by this provider. An object retrieved from the map
     * may require casting; the object should be the same as returned by this method
     * called on the provider of its key subject.
     *
     * @param rootInput             root input passed to the whole composed calculation
     * @param precalculatedValues   subject-value map containing a value for each subject
     *                              {@linkplain #requires() required} by this provider
     * @return                      value of the subject {@linkplain #provides() provided} by this object
     */
    public Object calculate(InT rootInput, Map<SubjectT, Object> precalculatedValues);

    /**
     * Obtains an instance of this provider class to be used in a new composition.
     *
     * In the case of a stateless calculator it is presumably this object itself.
     *
     * @return  an instance of this provider class to be used in a new composition
     */
    default public Provider<SubjectT, InT> getInstanceForNewComposition() {
        return this;
    }
}
