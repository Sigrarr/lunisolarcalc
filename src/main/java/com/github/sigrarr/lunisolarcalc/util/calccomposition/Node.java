package com.github.sigrarr.lunisolarcalc.util.calccomposition;

abstract class Node<KeyT, InT> {

    protected final Provider<KeyT, InT> calculator;

    Node(Provider<KeyT, InT> calculator) {
        this.calculator = calculator;
    }
}
