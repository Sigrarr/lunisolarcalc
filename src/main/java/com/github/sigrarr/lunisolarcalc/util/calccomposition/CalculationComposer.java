package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

public class CalculationComposer<Subject extends Enum<Subject>, InT, OutT> {

    protected final ProvidersRegister<Subject, InT, OutT> register = new ProvidersRegister<>();
    protected final Class<Subject> subjectEnumClass;
    protected final Class<OutT> outputClass;

    public CalculationComposer(Class<Subject> subjectEnumClass, Class<OutT> outputClass) {
        this.subjectEnumClass = subjectEnumClass;
        this.outputClass = outputClass;
    }

    public void register(Provider<Subject, InT, OutT> calculator) {
        register.add(calculator);
    }

    public boolean hasProvider(Subject subject) {
        return register.has(subject);
    }

    public SingleOutputComposition<Subject, InT, OutT> compose(Subject target) {
        EnumSet<Subject> targets = EnumSet.of(target);
        return getNewCompositionBuilder(targets).buildSingleOutputComposition();
    }

    public MultiOutputComposition<Subject, InT, OutT> compose(EnumSet<Subject> targets) {
        return getNewCompositionBuilder(targets).buildMultiOutputComposition();
    }

    private CompositionBuilder<Subject, InT, OutT> getNewCompositionBuilder(EnumSet<Subject> targets) {
        return new CompositionBuilder<Subject, InT, OutT>(this, targets);
    }
}
