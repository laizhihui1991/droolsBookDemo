package com.drools.algorithm.rete.node;


import com.drools.algorithm.rete.fact.Condition;

import java.util.ArrayList;
import java.util.List;

public class BetaNode {
    private final List<Object[]> joinedFacts;
    private final List<Condition> joinConditions;

    public BetaNode() {
        this.joinedFacts = new ArrayList<>();
        this.joinConditions = new ArrayList<>();
    }

    public void addJoinCondition(Condition condition) {
        joinConditions.add(condition);
    }

    public boolean join(Object fact1, Object fact2) {
        for (Condition condition : joinConditions) {
            if (!condition.evaluate(fact1) || !condition.evaluate(fact2)) {
                return false;
            }
        }
        joinedFacts.add(new Object[]{fact1, fact2});
        return true;
    }

    public List<Object[]> getJoinedFacts() {
        return joinedFacts;
    }
}
