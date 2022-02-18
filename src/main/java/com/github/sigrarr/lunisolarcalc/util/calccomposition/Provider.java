package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

public interface Provider<Subject extends Enum<Subject>, InT, OutT> {

    public EnumSet<Subject> requires();

    public Subject provides();

    public Object calculate(InT rootArgument, Map<Subject, Object> arguments);

    default public Provider<Subject, InT, OutT> getInstanceForNewComposition() {
        return this;
    }
}
