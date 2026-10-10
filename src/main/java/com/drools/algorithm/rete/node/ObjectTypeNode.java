package com.drools.algorithm.rete.node;

import java.util.HashMap;
import java.util.Map;

public class ObjectTypeNode {
    private final String type;
    private final Map<String, AlphaNode> alphaNodesByAttribute;

    public ObjectTypeNode(String type) {
        this.type = type;
        this.alphaNodesByAttribute = new HashMap<>();
    }

    public void addAlphaNode(AlphaNode alphaNode) {
        alphaNodesByAttribute.put(alphaNode.getCondition().getAttribute(), alphaNode);
    }

    public boolean evaluate(Object fact) {
        if (fact.getClass().getSimpleName().equals(type)) {
            for (AlphaNode alphaNode : alphaNodesByAttribute.values()) {
                if (alphaNode.evaluate(fact)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getType() {
        return type;
    }

    public Map<String, AlphaNode> getAlphaNodesByAttribute() {
        return alphaNodesByAttribute;
    }
}
