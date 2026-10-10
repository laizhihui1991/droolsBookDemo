package com.drools.algorithm.rete;

import java.util.List;


public interface Action {
    void execute(List<Object> facts);
}
