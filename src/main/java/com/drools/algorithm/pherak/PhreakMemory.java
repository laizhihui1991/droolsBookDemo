package com.drools.algorithm.pherak;

import java.util.HashMap;
import java.util.Map;

public class PhreakMemory {
    // 节点内存：存储单个节点的状态和数据
    public static class NodeMemory {
        private final Map<String, Object> state = new HashMap<>();

        // 更新节点状态
        public void updateState(String key, Object value) {
            System.out.println("[节点内存] 更新状态: " + key + " = " + value);
            state.put(key, value);
        }

        // 获取节点状态
        public Object getState(String key) {
            return state.get(key);
        }
    }

    // 分段内存：管理一组节点的计算结果
    public static class SegmentMemory {
        private final NodeMemory primaryNodeMemory;
        private final Map<String, Object> results = new HashMap<>();

        public SegmentMemory(NodeMemory primaryNodeMemory) {
            this.primaryNodeMemory = primaryNodeMemory;
        }

        // 获取主节点内存
        public NodeMemory getNodeMemory() {
            return primaryNodeMemory;
        }

        // 设置分段计算结果
        public void setResult(String key, Object value) {
            results.put(key, value);
        }

        // 获取分段计算结果
        public Object getResult(String key) {
            return results.get(key);
        }
    }

    // 规则内存：存储整个规则的上下文信息
    public static class RuleMemory {
        private final SegmentMemory segmentMemory;
        private final Map<String, Object> context = new HashMap<>();

        public RuleMemory(SegmentMemory segmentMemory) {
            this.segmentMemory = segmentMemory;
        }

        // 设置规则上下文
        public void setContext(String key, Object value) {
            System.out.println("[规则内存] 设置上下文: " + key + " = " + value);
            context.put(key, value);
        }

        // 获取规则上下文
        public Object getContext(String key) {
            return context.get(key);
        }

        // 检查是否有待处理的事实
        public boolean hasPendingFacts() {
            Integer count = (Integer) context.get("pending_fact_count");
            return count != null && count > 0;
        }
    }
}