package com.drools.algorithm.reteOO.node;

import com.drools.algorithm.reteOO.WorkingMemory;
import com.drools.algorithm.reteOO.fact.Fact;

import java.util.ArrayList;
import java.util.List;

public class AlphaNode implements Node {
    private final AlphaConstraint constraint;
    private final List<Node> sinks = new ArrayList<>();

    public AlphaNode(AlphaConstraint constraint) {
        this.constraint = constraint;
    }

    public void addSink(Node sink) {
        sinks.add(sink);
    }

    @Override
    public void assertObject(Fact fact, WorkingMemory wm) {
        if (constraint.evaluate(fact)) {
            for (Node sink : sinks) {
                sink.assertObject(fact, wm);
            }
        }
    }
}