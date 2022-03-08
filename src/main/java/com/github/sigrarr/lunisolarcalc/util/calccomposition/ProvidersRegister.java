package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions.*;

class ProvidersRegister<SubjectT extends Enum<SubjectT>, InT> {

    private final Map<SubjectT, RegisterNode<SubjectT, InT>> subjectToNode = new HashMap<>();
    private final List<RegisterNode<SubjectT, InT>> pendingNodes = new LinkedList<>();    

    protected RegisterNode<SubjectT, InT> getRequired(SubjectT subject) {
        if (!subjectToNode.containsKey(subject)) {
            throw new ProviderLackException(subject);
        }
        return subjectToNode.get(subject);
    }

    protected void add(Provider<SubjectT, InT> calculator) {
        SubjectT newlyProvidedSubject = calculator.provides();
        if (has(newlyProvidedSubject)) {
            throw new DoubledProviderException(newlyProvidedSubject, subjectToNode.get(newlyProvidedSubject).calculator, calculator);
        }

        RegisterNode<SubjectT, InT> newNode = new RegisterNode<>(calculator);

        passToDependers(newNode, newlyProvidedSubject);
        setDependees(newNode);

        subjectToNode.put(newlyProvidedSubject, newNode);
        if (!newNode.hasAllDependees()) {
            pendingNodes.add(newNode);
        }
    }

    protected boolean has(SubjectT subject) {
        return subjectToNode.containsKey(subject);
    }

    private void passToDependers(RegisterNode<SubjectT, InT> newNode, SubjectT newlyProvidedSubject) {
        ListIterator<RegisterNode<SubjectT, InT>> iterator = pendingNodes.listIterator();
        while (iterator.hasNext()) {
            RegisterNode<SubjectT, InT> pendingNode = iterator.next();
            if (pendingNode.calculator.requires().contains(newlyProvidedSubject)) {
                pendingNode.directDependees.add(newNode);
                if (pendingNode.hasAllDependees()) {
                    iterator.remove();
                }
            }
        }
    }

    private void setDependees(RegisterNode<SubjectT, InT> newNode) {
        for (SubjectT requiredSubject : newNode.calculator.requires()) {
            if (subjectToNode.containsKey(requiredSubject)) {
                newNode.directDependees.add(subjectToNode.get(requiredSubject));
            }
        }
    }
}
