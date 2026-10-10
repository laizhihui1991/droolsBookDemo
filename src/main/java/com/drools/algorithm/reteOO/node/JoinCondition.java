package com.drools.algorithm.reteOO.node;

import com.drools.algorithm.reteOO.fact.Fact;

public interface JoinCondition {
    boolean match(Fact left, Fact right);
}
