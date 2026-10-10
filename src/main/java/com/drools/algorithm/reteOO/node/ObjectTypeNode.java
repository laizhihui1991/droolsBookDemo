package com.drools.algorithm.reteOO.node;

import com.drools.algorithm.reteOO.WorkingMemory;
import com.drools.algorithm.reteOO.fact.Fact;

import java.util.ArrayList;
import java.util.List;

public class ObjectTypeNode implements Node {
    private final Class<?> objectType;
    private final List<Node> sinks = new ArrayList<>();

    public ObjectTypeNode(Class<?> objectType) {
        this.objectType = objectType;
    }

    public void addSink(Node sink) {
        sinks.add(sink);
    }

    @Override
    public void assertObject(Fact fact, WorkingMemory wm) {
        if (objectType.isAssignableFrom(fact.getClass())) {
            for (Node sink : sinks) {
                sink.assertObject(fact, wm);
            }
        }
    }
}