package com.drools.algorithm.reteOO.node;


import com.drools.algorithm.reteOO.fact.Fact;

public interface AlphaConstraint {
    boolean evaluate(Fact fact);
}