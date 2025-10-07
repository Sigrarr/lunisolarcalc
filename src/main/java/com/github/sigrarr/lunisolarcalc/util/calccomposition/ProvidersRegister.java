package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions.*;

class ProvidersRegister<KeyT, InT> {

    private final Map<KeyT, RegisterNode<KeyT, InT>> subjectToNode;
    private final List<RegisterNode<KeyT, InT>> pendingNodes = new LinkedList<>();

    ProvidersRegister(KeyUtil<KeyT> ku) {
        subjectToNode = ku.getMap();
    }

    protected RegisterNode<KeyT, InT> getRequired(KeyT subject) {
        if (!subjectToNode.containsKey(subject)) {
            throw new ProviderLackException(subject);
        }
        return subjectToNode.get(subject);
    }

    protected void add(Provider<KeyT, InT> calculator) {
        KeyT newlyProvidedSubject = calculator.provides();
        if (has(newlyProvidedSubject)) {
            throw new DoubledProviderException(newlyProvidedSubject, subjectToNode.get(newlyProvidedSubject).calculator, calculator);
        }

        RegisterNode<KeyT, InT> newNode = new RegisterNode<>(calculator);

        passToDependers(newNode, newlyProvidedSubject);
        setDependees(newNode);

        subjectToNode.put(newlyProvidedSubject, newNode);
        if (!newNode.hasAllDirectDependees()) {
            pendingNodes.add(newNode);
        }
    }

    protected boolean has(KeyT subject) {
        return subjectToNode.containsKey(subject);
    }

    private void passToDependers(RegisterNode<KeyT, InT> newNode, KeyT newlyProvidedSubject) {
        ListIterator<RegisterNode<KeyT, InT>> iterator = pendingNodes.listIterator();
        while (iterator.hasNext()) {
            RegisterNode<KeyT, InT> pendingNode = iterator.next();
            if (pendingNode.calculator.requires().contains(newlyProvidedSubject)) {
                pendingNode.directDependees.add(newNode);
                if (pendingNode.hasAllDirectDependees()) {
                    iterator.remove();
                }
            }
        }
    }

    private void setDependees(RegisterNode<KeyT, InT> newNode) {
        for (KeyT requiredSubject : newNode.calculator.requires()) {
            if (subjectToNode.containsKey(requiredSubject)) {
                newNode.directDependees.add(subjectToNode.get(requiredSubject));
            }
        }
    }
}
