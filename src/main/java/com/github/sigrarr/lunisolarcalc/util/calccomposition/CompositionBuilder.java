package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

class CompositionBuilder<SubjectT extends Enum<SubjectT>, InT> {

    private final CalculationComposer<SubjectT, InT> composer;
    private final EnumSet<SubjectT> targets;
    private final Map<RegisterNode<SubjectT, InT>, CompositionNode<SubjectT, InT>> nodeMap = new HashMap<>();
    private final Code<SubjectT, InT> code;

    CompositionBuilder(CalculationComposer<SubjectT, InT> composer, EnumSet<SubjectT> targets) {
        this.composer = composer;
        this.targets = targets;
        this.code = new Code<>(composer.subjectEnumClass);
    }

    protected SingleOutputComposition<SubjectT, InT> buildSingleOutputComposition() {
        return new SingleOutputComposition<>(resolveOrderedNodes(), composer.subjectEnumClass);
    }

    protected MultiOutputComposition<SubjectT, InT> buildMultiOutputComposition() {
        return new MultiOutputComposition<>(resolveOrderedNodes(), composer.subjectEnumClass);
    }

    private Collection<CompositionNode<SubjectT, InT>> resolveOrderedNodes() {
        for(SubjectT target : targets) {
            RegisterNode<SubjectT, InT> headRegisterNode = composer.register.getRequired(target);
            CompositionNode<SubjectT, InT> headNode = getCompositionNode(headRegisterNode, true);
            fillGraphFragmentRecursively(headRegisterNode, headNode, "");
        }
        return nodeMap.values().stream().sorted().collect(Collectors.toCollection(() -> new ArrayList<>(nodeMap.size())));
    }

    private void fillGraphFragmentRecursively(RegisterNode<SubjectT, InT> registerNode, CompositionNode<SubjectT, InT> node, String dependersPath) {
        char nodeCode = code.encode(registerNode);
        validateRegisterNode(registerNode, dependersPath, nodeCode);
        dependersPath += nodeCode;
        for (RegisterNode<SubjectT, InT> registerDependeeNode : registerNode.directDependees) {
            CompositionNode<SubjectT, InT> dependeeNode = getCompositionNode(registerDependeeNode);
            dependeeNode.weight += node.weight;
            fillGraphFragmentRecursively(registerDependeeNode, dependeeNode, dependersPath);
        }
    }

    private CompositionNode<SubjectT, InT> getCompositionNode(RegisterNode<SubjectT, InT> registerNode) {
        return getCompositionNode(registerNode, false);
    }

    private CompositionNode<SubjectT, InT> getCompositionNode(RegisterNode<SubjectT, InT> registerNode, boolean isTarget) {
        if (nodeMap.containsKey(registerNode)) {
            return nodeMap.get(registerNode);
        }

        CompositionNode<SubjectT, InT> newNode = new CompositionNode<>(registerNode.calculator.getInstanceForNewComposition(), nodeMap.size(), isTarget);
        nodeMap.put(registerNode, newNode);
        return newNode;
    }

    private void validateRegisterNode(RegisterNode<SubjectT, InT> registerNode, String dependersPath, char nodeCode) {
        if (!registerNode.hasAllDependees()) {
            throw new IllegalStateException(buildMissingDependeesMessage(registerNode));
        }
        if (dependersPath.indexOf(nodeCode) > 0) {
            throw new IllegalStateException(buildCircularDependecyMessage(registerNode, dependersPath, nodeCode));
        }
    }

    private String buildMissingDependeesMessage(RegisterNode<SubjectT, InT> brokenNode) {
        Set<?> okSubjects = brokenNode.directDependees.stream().map(rdn -> rdn.calculator.provides()).collect(Collectors.toSet());
        return "No providers have been registered for the following subjects: "
            + brokenNode.calculator.requires().stream().filter(s -> !okSubjects.contains(s)).map(Object::toString).collect(Collectors.joining(", "));
    }

    private String buildCircularDependecyMessage(RegisterNode<SubjectT, InT> brokenNode, String dependersPath, char nodeCode) {
        String cyclePath = dependersPath.substring(dependersPath.indexOf(nodeCode)) + nodeCode;
        return "Cannot compose calculation: Circular dependency detected: "
            + cyclePath.chars().mapToObj(cv -> code.decode(cv).toString()).collect(Collectors.joining(" -> "));
    }
}
