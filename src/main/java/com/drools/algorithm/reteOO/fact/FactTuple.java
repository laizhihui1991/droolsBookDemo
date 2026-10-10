package com.drools.algorithm.reteOO.fact;


// === 4. FactTuple（Beta Join 的组合体） ===
public class FactTuple implements Fact {
    private final Object[] parts;

    public FactTuple(Fact... parts) {
        this.parts = parts;
    }

    public Object[] getParts() {
        return parts;
    }
}