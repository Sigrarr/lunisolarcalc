package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

public class CalculationComposer<SubjectT extends Enum<SubjectT>, InT> {

    protected final ProvidersRegister<SubjectT, InT> register = new ProvidersRegister<>();
    protected final Class<SubjectT> subjectEnumClass;

    public CalculationComposer(Class<SubjectT> subjectEnumClass) {
        this.subjectEnumClass = subjectEnumClass;
    }

    public void register(Provider<SubjectT, InT> calculator) {
        register.add(calculator);
    }

    public boolean hasProvider(SubjectT subject) {
        return register.has(subject);
    }

    public SingleOutputComposition<SubjectT, InT> compose(SubjectT target) {
        EnumSet<SubjectT> targets = EnumSet.of(target);
        return getNewCompositionBuilder(targets).buildSingleOutputComposition();
    }

    public MultiOutputComposition<SubjectT, InT> compose(EnumSet<SubjectT> targets) {
        return getNewCompositionBuilder(targets).buildMultiOutputComposition();
    }

    private CompositionBuilder<SubjectT, InT> getNewCompositionBuilder(EnumSet<SubjectT> targets) {
        return new CompositionBuilder<SubjectT, InT>(this, targets);
    }
}
