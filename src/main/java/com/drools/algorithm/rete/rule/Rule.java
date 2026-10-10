package com.drools.algorithm.rete.rule;

import com.drools.algorithm.rete.Action;
import com.drools.algorithm.rete.fact.Condition;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private final List<Condition> conditions;
    private Action action;

    public Rule() {
        this.conditions = new ArrayList<>();
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean match(List<Object> facts) {
        for (Condition condition : conditions) {
            boolean matched = false;
            for (Object fact : facts) {
                if (condition != null && condition.evaluate(fact)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    public void execute(List<Object> facts) {
        if (action != null) {
            action.execute(facts);
        }
    }
}
