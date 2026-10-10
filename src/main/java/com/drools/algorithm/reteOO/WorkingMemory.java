package com.drools.algorithm.reteOO;

import com.drools.algorithm.reteOO.fact.Fact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkingMemory {
    public List<Fact> facts = new ArrayList<>();
    public Map<String, Object> context = new HashMap<>();

    public void insert(Fact fact) {
        facts.add(fact);
    }
}