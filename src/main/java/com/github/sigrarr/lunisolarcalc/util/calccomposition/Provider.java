package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

public interface Provider<SubjectT extends Enum<SubjectT>, InT> {

    public EnumSet<SubjectT> requires();

    public SubjectT provides();

    public Object calculate(InT rootArgument, Map<SubjectT, Object> requiredArguments);

    default public Provider<SubjectT, InT> getInstanceForNewComposition() {
        return this;
    }
}
