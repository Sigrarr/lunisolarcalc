package com.github.sigrarr.lunisolarcalc.util.calccomposition;

abstract class Node<E extends Enum<E>, InT, OutT> {

    protected final Provider<E, InT, OutT> calculator;

    Node(Provider<E, InT, OutT> calculator) {
        this.calculator = calculator;
    }
}
