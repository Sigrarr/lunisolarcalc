package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

final class RegisterNode<SubjectT extends Enum<SubjectT>, InT> extends Node<SubjectT, InT> {

    protected final List<RegisterNode<SubjectT, InT>> directDependees;

    RegisterNode(Provider<SubjectT, InT> calculator) {
        super(calculator);
        directDependees = new ArrayList<>(calculator.requires().size());
    }

    protected boolean hasAllDependees() {
        return calculator.requires().size() == directDependees.size();
    }
}
