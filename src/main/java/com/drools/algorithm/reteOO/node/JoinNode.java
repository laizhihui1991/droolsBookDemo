package com.drools.algorithm.reteOO.node;

import com.drools.algorithm.reteOO.WorkingMemory;
import com.drools.algorithm.reteOO.fact.Fact;
import com.drools.algorithm.reteOO.fact.FactTuple;

import java.util.ArrayList;
import java.util.List;

public class JoinNode implements Node {
    private final List<Fact> leftMemory = new ArrayList<>();
    private final List<Fact> rightMemory = new ArrayList<>();
    private final Class<?> leftType;
    private final Class<?> rightType;
    private final JoinCondition condition;
    private Node sink;

    public JoinNode(Class<?> leftType, Class<?> rightType, JoinCondition condition) {
        this.leftType = leftType;
        this.rightType = rightType;
        this.condition = condition;
    }

    public void setSink(Node sink) {
        this.sink = sink;
    }

    @Override
    public void assertObject(Fact fact, WorkingMemory wm) {
        if (leftType.isAssignableFrom(fact.getClass())) {
            leftMemory.add(fact);
            for (Fact right : rightMemory) {
                if (condition.match(fact, right)) {
                    sink.assertObject(new FactTuple(fact, right), wm);
                }
            }
        } else if (rightType.isAssignableFrom(fact.getClass())) {
            rightMemory.add(fact);
            for (Fact left : leftMemory) {
                if (condition.match(left, fact)) {
                    sink.assertObject(new FactTuple(left, fact), wm);
                }
            }
        }
    }
}
