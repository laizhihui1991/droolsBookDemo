package com.drools.algorithm.rete.node;


import com.drools.algorithm.rete.fact.Condition;

import java.util.ArrayList;
import java.util.List;

public class AlphaNode {
    private final Condition condition;
    private final List<Object> facts;

    public AlphaNode(Condition condition) {
        this.condition = condition;
        this.facts = new ArrayList<>();
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean evaluate(Object fact) {
        if (condition.evaluate(fact)) {
            facts.add(fact);
            return true;
        }
        return false;
    }

    public List<Object> getFacts() {
        return facts;
    }
}