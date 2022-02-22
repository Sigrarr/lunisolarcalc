package com.github.sigrarr.lunisolarcalc.util.calccomposition;

abstract class Node<SubjectT extends Enum<SubjectT>, InT> {

    protected final Provider<SubjectT, InT> calculator;

    Node(Provider<SubjectT, InT> calculator) {
        this.calculator = calculator;
    }
}
