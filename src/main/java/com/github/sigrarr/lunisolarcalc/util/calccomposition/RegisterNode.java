package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

class RegisterNode<E extends Enum<E>, InT, OutT> extends Node<E, InT, OutT> {

    protected final List<RegisterNode<E, InT, OutT>> directDependees;

    RegisterNode(Provider<E, InT, OutT> calculator) {
        super(calculator);
        directDependees = new ArrayList<>(calculator.requires().size());
    }

    protected boolean hasAllDependees() {
        return calculator.requires().size() == directDependees.size();
    }
}
