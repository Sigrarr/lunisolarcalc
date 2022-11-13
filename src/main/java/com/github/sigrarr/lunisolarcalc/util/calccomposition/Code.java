package com.github.sigrarr.lunisolarcalc.util.calccomposition;

class Code<SubjectT extends Enum<SubjectT>, InT> {

    private final SubjectT[] subjects;

    Code(Class<SubjectT> subjectEnumClass) {
        subjects = subjectEnumClass.getEnumConstants();
    }

    protected char encode(SubjectT subject) {
        return (char) subject.ordinal();
    }

    protected char encode(Node<SubjectT, InT> node) {
        return encode(node.calculator.provides());
    }

    protected SubjectT decode(char code) {
        return decode((int) code);
    }

    protected SubjectT decode(int codeValue) {
        return subjects[codeValue];
    }
}
