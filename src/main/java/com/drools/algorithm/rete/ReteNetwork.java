package com.drools.algorithm.rete;

import com.drools.algorithm.rete.node.AlphaNode;
import com.drools.algorithm.rete.node.BetaNode;
import com.drools.algorithm.rete.node.ObjectTypeNode;
import com.drools.algorithm.rete.rule.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReteNetwork {
    private final Map<String, ObjectTypeNode> objectTypeNodes;
    private final List<BetaNode> betaNodes;
    private final List<Rule> rules;

    public ReteNetwork() {
        this.objectTypeNodes = new HashMap<>();
        this.betaNodes = new ArrayList<>();
        this.rules = new ArrayList<>();
    }

    public void addObjectTypeNode(ObjectTypeNode objectTypeNode) {
        objectTypeNodes.put(objectTypeNode.getType(), objectTypeNode);
    }

    public void addBetaNode(BetaNode betaNode) {
        betaNodes.add(betaNode);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void insert(Object fact) {
        List<Object> matchedFacts = new ArrayList<>();
        matchedFacts.add(fact);
        ObjectTypeNode objectTypeNode = objectTypeNodes.get(fact.getClass().getSimpleName());
        if (objectTypeNode != null) {
            if (objectTypeNode.evaluate(fact)) {
                for (AlphaNode alphaNode : objectTypeNode.getAlphaNodesByAttribute().values()) {
                    matchedFacts.addAll(alphaNode.getFacts());
                }
            }
        }
        for (BetaNode betaNode : betaNodes) {
            for (Object[] joinedFacts : betaNode.getJoinedFacts()) {
                if (betaNode.join(fact, joinedFacts[0])) {
                    matchedFacts.add(fact);
                    matchedFacts.add(joinedFacts[0]);
                }
            }
        }
        for (Rule rule : rules) {
            if (rule != null && rule.match(matchedFacts)) {
                rule.execute(matchedFacts);
            }
        }
    }
}