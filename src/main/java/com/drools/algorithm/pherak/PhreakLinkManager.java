package com.drools.algorithm.pherak;

import java.util.HashMap;
import java.util.Map;

public class PhreakLinkManager {
    private final Map<String, String[]> ruleToSegments = new HashMap<>();
    private final Map<String, String[]> segmentToNodes = new HashMap<>();

    // 注册规则与分段的关系
    public void registerRuleSegments(String ruleId, String[] segmentIds) {
        ruleToSegments.put(ruleId, segmentIds);
    }

    // 注册分段与节点的关系
    public void registerSegmentNodes(String segmentId, String[] nodeIds) {
        segmentToNodes.put(segmentId, nodeIds);
    }

    // 获取规则对应的分段
    public String[] getSegmentsForRule(String ruleId) {
        return ruleToSegments.get(ruleId);
    }

    // 获取分段对应的节点
    public String[] getNodesForSegment(String segmentId) {
        return segmentToNodes.get(segmentId);
    }
}