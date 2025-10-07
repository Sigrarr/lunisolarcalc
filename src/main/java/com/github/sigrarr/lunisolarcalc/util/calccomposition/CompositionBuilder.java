package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions.*;

class CompositionBuilder<KeyT, InT> {

    private final CalculationComposer<KeyT, InT> composer;
    private final Set<KeyT> targets;
    private final Map<RegisterNode<KeyT, InT>, CompositionNode<KeyT, InT>> nodeMap = new HashMap<>();

    CompositionBuilder(CalculationComposer<KeyT, InT> composer, Set<KeyT> targets) {
        this.composer = composer;
        this.targets = targets;
    }

    protected CalcComposition<KeyT, InT> buildSingleOutputComposition() {
        return new CalcComposition<>(resolveOrderedNodes(), composer.ku);
    }

    protected MultiCalcComposition<KeyT, InT> buildMultiOutputComposition() {
        return new MultiCalcComposition<>(resolveOrderedNodes(), composer.ku);
    }

    private List<CompositionNode<KeyT, InT>> resolveOrderedNodes() {
        for (KeyT target : targets) {
            RegisterNode<KeyT, InT> headRegisterNode = composer.register.getRequired(target);
            CompositionNode<KeyT, InT> headNode = getCompositionNode(headRegisterNode, true);
            fillGraphFragmentRecursively(headRegisterNode, headNode, KeyPaths.empty());
        }
        return nodeMap.values().stream().sorted().collect(Collectors.toCollection(() -> new ArrayList<>(nodeMap.size())));
    }

    private void fillGraphFragmentRecursively(RegisterNode<KeyT, InT> registerNode, CompositionNode<KeyT, InT> node, KeyT[] dependersPath) {
        KeyT key = node.calculator.provides();
        validateRegisterNode(registerNode, dependersPath, key);
        dependersPath = KeyPaths.add(dependersPath, key);
        for (RegisterNode<KeyT, InT> registerDependeeNode : registerNode.directDependees) {
            CompositionNode<KeyT, InT> dependeeNode = getCompositionNode(registerDependeeNode);
            dependeeNode.weight += node.weight;
            fillGraphFragmentRecursively(registerDependeeNode, dependeeNode, dependersPath);
        }
    }

    private CompositionNode<KeyT, InT> getCompositionNode(RegisterNode<KeyT, InT> registerNode) {
        return getCompositionNode(registerNode, false);
    }

    private CompositionNode<KeyT, InT> getCompositionNode(RegisterNode<KeyT, InT> registerNode, boolean markTarget) {
        CompositionNode<KeyT, InT> node;
        if (nodeMap.containsKey(registerNode)) {
            node = nodeMap.get(registerNode);
            if (markTarget)
                node.isTarget = true;
        } else {
            node = new CompositionNode<>(registerNode.calculator.getInstanceForNewComposition(), nodeMap.size(), markTarget);
            nodeMap.put(registerNode, node);
        }
        return node;
    }

    private void validateRegisterNode(RegisterNode<KeyT, InT> registerNode, KeyT[] dependersPath, KeyT key) {
        if (registerNode.calculator.requires().contains(registerNode.calculator.provides()))
            throw new CircularDependencyException(buildSubjectDependencyPath(registerNode, KeyPaths.of(key), key));
        if (!registerNode.hasAllDirectDependees())
            throw new ProviderLackException(buildMissingSubjectList(registerNode));
        if (KeyPaths.contains(dependersPath, key))
            throw new CircularDependencyException(buildSubjectDependencyPath(registerNode, dependersPath, key));
    }

    private Collection<?> buildMissingSubjectList(RegisterNode<KeyT, InT> brokenNode) {
        Set<?> okSubjects = brokenNode.directDependees.stream().map(n -> n.calculator.provides()).collect(Collectors.toSet());
        return brokenNode.calculator.requires().stream()
            .filter(s -> !okSubjects.contains(s))
            .collect(Collectors.toList());
    }

    private KeyT[] buildSubjectDependencyPath(RegisterNode<KeyT, InT> brokenNode, KeyT[] dependersPath, KeyT key) {
        return KeyPaths.add(KeyPaths.takeFrom(dependersPath, key), key);
    }
}
