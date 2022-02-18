package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

class CompositionBuilder<E extends Enum<E>, InT, OutT> {

    private final CalculationComposer<E, InT, OutT> composer;
    private final EnumSet<E> targets;
    private final Map<RegisterNode<E, InT, OutT>, CompositionNode<E, InT, OutT>> nodeMap = new HashMap<>();
    private final Code<E, InT, OutT> code;

    CompositionBuilder(CalculationComposer<E, InT, OutT> composer, EnumSet<E> targets) {
        this.composer = composer;
        this.targets = targets;
        this.code = new Code<>(composer.subjectEnumClass);
    }

    protected SingleOutputComposition<E, InT, OutT> buildSingleOutputComposition() {
        return new SingleOutputComposition<>(targets.iterator().next(), resolveOrderedNodes(), composer.outputClass);
    }

    protected MultiOutputComposition<E, InT, OutT> buildMultiOutputComposition() {
        return new MultiOutputComposition<>(targets, resolveOrderedNodes(), composer.outputClass);
    }

    private TreeSet<CompositionNode<E, InT, OutT>> resolveOrderedNodes() {
        for(E target : targets) {
            RegisterNode<E, InT, OutT> headRegisterNode = composer.register.getRequired(target);
            CompositionNode<E, InT, OutT> headNode = getCompositionNode(headRegisterNode);
            fillGraphFragmentRecursively(headRegisterNode, headNode, "");
        }
        return new TreeSet<>(nodeMap.values());
    }

    private void fillGraphFragmentRecursively(RegisterNode<E, InT, OutT> registerNode, CompositionNode<E, InT, OutT> node, String dependersPath) {
        char nodeCode = code.encode(registerNode);
        validateRegisterNode(registerNode, dependersPath, nodeCode);
        dependersPath += nodeCode;
        for (RegisterNode<E, InT, OutT> registerDependeeNode : registerNode.directDependees) {
            CompositionNode<E, InT, OutT> dependeeNode = getCompositionNode(registerDependeeNode);
            dependeeNode.weight += node.weight;
            fillGraphFragmentRecursively(registerDependeeNode, dependeeNode, dependersPath);
        }
    }

    private CompositionNode<E, InT, OutT> getCompositionNode(RegisterNode<E, InT, OutT> registerNode) {
        if (nodeMap.containsKey(registerNode)) {
            return nodeMap.get(registerNode);
        }

        CompositionNode<E, InT, OutT> newNode = new CompositionNode<>(registerNode.calculator.getInstanceForNewComposition(), nodeMap.size());
        nodeMap.put(registerNode, newNode);
        return newNode;
    }

    private void validateRegisterNode(RegisterNode<E, InT, OutT> registerNode, String dependersPath, char nodeCode) {
        if (!registerNode.hasAllDependees()) {
            throw new IllegalStateException(buildMissingDependeesMessage(registerNode));
        }
        if (dependersPath.indexOf(nodeCode) > 0) {
            throw new IllegalStateException(buildCircularDependecyMessage(registerNode, dependersPath, nodeCode));
        }
    }

    private String buildMissingDependeesMessage(RegisterNode<E, InT, OutT> brokenNode) {
        Set<?> okSubjects = brokenNode.directDependees.stream().map(rdn -> rdn.calculator.provides()).collect(Collectors.toSet());
        return "No providers have been registered for the following subjects: "
            + brokenNode.calculator.requires().stream().filter(s -> !okSubjects.contains(s)).map(Object::toString).collect(Collectors.joining(", "));
    }

    private String buildCircularDependecyMessage(RegisterNode<E, InT, OutT> brokenNode, String dependersPath, char nodeCode) {
        String cyclePath = dependersPath.substring(dependersPath.indexOf(nodeCode)) + nodeCode;
        return "Cannot compose calculation: Circular dependency detected: "
            + cyclePath.chars().mapToObj(cv -> code.decode(cv).toString()).collect(Collectors.joining(" -> "));
    }
}
