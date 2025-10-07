package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

final class RegisterNode<KeyT, InT> extends Node<KeyT, InT> {

    protected final List<RegisterNode<KeyT, InT>> directDependees;

    RegisterNode(Provider<KeyT, InT> calculator) {
        super(calculator);
        directDependees = new ArrayList<>(calculator.requires().size());
    }

    protected boolean hasAllDirectDependees() {
        return calculator.requires().size() == directDependees.size();
    }
}
