package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

class ProvidersRegister<E extends Enum<E>, InT, OutT> {

    private final Map<E, RegisterNode<E, InT, OutT>> subjectToNode = new HashMap<>();
    private final List<RegisterNode<E, InT, OutT>> pendingNodes = new LinkedList<>();    

    protected RegisterNode<E, InT, OutT> getRequired(E subject) {
        if (!subjectToNode.containsKey(subject)) {
            throw new IllegalStateException("No provider of " + subject + " has been registered.");
        }
        return subjectToNode.get(subject);
    }

    protected void add(Provider<E, InT, OutT> calculator) {
        E newlyProvidedSubject = calculator.provides();
        if (has(newlyProvidedSubject)) {
            throw new UnsupportedOperationException("Provider of " + newlyProvidedSubject + " has been already registered.");
        }

        RegisterNode<E, InT, OutT> newNode = new RegisterNode<>(calculator);

        passToDependers(newNode, newlyProvidedSubject);
        setDependees(newNode);

        subjectToNode.put(newlyProvidedSubject, newNode);
        if (!newNode.hasAllDependees()) {
            pendingNodes.add(newNode);
        }
    }

    protected boolean has(E subject) {
        return subjectToNode.containsKey(subject);
    }

    private void passToDependers(RegisterNode<E, InT, OutT> newNode, E newlyProvidedSubject) {
        ListIterator<RegisterNode<E, InT, OutT>> iterator = pendingNodes.listIterator();
        while (iterator.hasNext()) {
            RegisterNode<E, InT, OutT> pendingNode = iterator.next();
            if (pendingNode.calculator.requires().contains(newlyProvidedSubject)) {
                pendingNode.directDependees.add(newNode);
                if (pendingNode.hasAllDependees()) {
                    iterator.remove();
                }
            }
        }
    }

    private void setDependees(RegisterNode<E, InT, OutT> newNode) {
        for (E requiredSubject : newNode.calculator.requires()) {
            if (subjectToNode.containsKey(requiredSubject)) {
                newNode.directDependees.add(subjectToNode.get(requiredSubject));
            }
        }
    }
}
