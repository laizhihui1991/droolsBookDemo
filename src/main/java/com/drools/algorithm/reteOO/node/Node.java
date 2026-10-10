package com.drools.algorithm.reteOO.node;


import com.drools.algorithm.reteOO.WorkingMemory;
import com.drools.algorithm.reteOO.fact.Fact;

public interface Node {
    void assertObject(Fact fact, WorkingMemory wm);
}