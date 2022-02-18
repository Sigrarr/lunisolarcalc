package com.github.sigrarr.lunisolarcalc.util.calccomposition;

class Code<E extends Enum<E>, InT, OutT> {
    
    private final E[] subjects;

    Code(Class<E> subjectEnumClass) {
        subjects = subjectEnumClass.getEnumConstants();
    }

    protected char encode(E subject) {
        return (char) subject.ordinal();
    }

    protected char encode(Node<E, InT, OutT> node) {
        return encode(node.calculator.provides());
    }

    protected E decode(char code) {
        return decode((int) code);
    }

    protected E decode(int codeValue) {
        return subjects[codeValue];
    }
}
