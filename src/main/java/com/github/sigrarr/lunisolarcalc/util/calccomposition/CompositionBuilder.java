package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions.*;

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
        for (SubjectT target : targets) {
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
        if (registerNode.calculator.requires().contains(registerNode.calculator.provides())) {
            throw new CircularDependencyException(buildSubjectDependencyPath(registerNode, "" + nodeCode, nodeCode));
        }
        if (!registerNode.hasAllDirectDependees()) {
            throw new ProviderLackException(buildMissingSubjectList(registerNode));
        }
        if (dependersPath.indexOf(nodeCode) > 0) {
            throw new CircularDependencyException(buildSubjectDependencyPath(registerNode, dependersPath, nodeCode));
        }
    }

    private Collection<Enum<?>> buildMissingSubjectList(RegisterNode<SubjectT, InT> brokenNode) {
        Set<Enum<?>> okSubjects = brokenNode.directDependees.stream().map(n -> n.calculator.provides()).collect(Collectors.toSet());
        return brokenNode.calculator.requires().stream()
            .filter(s -> !okSubjects.contains(s))
            .collect(Collectors.toList());
    }

    private List<Enum<?>> buildSubjectDependencyPath(RegisterNode<SubjectT, InT> brokenNode, String dependersPath, char nodeCode) {
        String cyclePath = dependersPath.substring(dependersPath.indexOf(nodeCode)) + nodeCode;
        return cyclePath.chars()
            .mapToObj(c -> code.decode(c))
            .collect(Collectors.toList());
    }
}
