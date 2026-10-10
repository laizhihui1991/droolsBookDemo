package com.drools.algorithm.pherak;

import java.util.HashSet;
import java.util.Set;

public class PhreakLazyEvaluator {
    private final PhreakLinkManager linkManager;
    private final Set<String> pendingRules = new HashSet<>();

    public PhreakLazyEvaluator(PhreakLinkManager linkManager) {
        this.linkManager = linkManager;
    }

    // 添加待评估的规则
    public void addPendingRule(String ruleId) {
        System.out.println("[评估器] 添加待评估规则: " + ruleId);
        pendingRules.add(ruleId);
    }

    // 检查触发条件并评估规则
    public void checkAndEvaluate(PhreakMemory.RuleMemory ruleMemory, String triggerEvent) {
        if ("orderSubmitted".equals(triggerEvent) && ruleMemory.hasPendingFacts()) {
            System.out.println("[评估器] 检测到订单提交，开始评估待处理规则...");
            // 仅评估当前规则
            if (pendingRules.contains(ruleMemory == null ? null : ruleMemory.getContext("rule_id"))) {
                evaluateRule((String) ruleMemory.getContext("rule_id"), ruleMemory);
            }
            pendingRules.remove(ruleMemory == null ? null : ruleMemory.getContext("rule_id"));
        } else {
            System.out.println("[评估器] 未满足评估条件，跳过评估");
        }
    }

    // 评估指定规则
    private void evaluateRule(String ruleId, PhreakMemory.RuleMemory ruleMemory) {
        System.out.println("[评估器] 开始评估规则: " + ruleId);

        // 获取规则对应的分段
        String[] segments = linkManager.getSegmentsForRule(ruleId);
        if (segments == null || segments.length == 0) {
            System.out.println("[评估器] 规则 " + ruleId + " 没有关联的分段");
            ruleMemory.setContext("rule_execution_result", "FAILURE");
            ruleMemory.setContext("pending_fact_count", 0);
            return;
        }

        // 规则评估结果，默认为成功
        boolean ruleResult = true;

        // 遍历每个分段进行评估
        for (String segmentId : segments) {
            System.out.println("[评估器] 处理分段: " + segmentId);

            // 从规则内存获取分段内存
            PhreakMemory.SegmentMemory segmentMemory = (PhreakMemory.SegmentMemory) ruleMemory.getContext("segment_memory_" + segmentId);

            if (segmentMemory == null) {
                System.out.println("[评估器] 分段内存 " + segmentId + " 不存在");
                ruleResult = false;
                continue;
            }

            // 获取分段对应的节点
            String[] nodes = linkManager.getNodesForSegment(segmentId);
            if (nodes == null || nodes.length == 0) {
                System.out.println("[评估器] 分段 " + segmentId + " 没有关联的节点");
                ruleResult = false;
                continue;
            }

            // 分段评估结果，默认为成功
            boolean segmentResult = true;

            // 遍历每个节点进行评估
            for (String nodeId : nodes) {
                System.out.println("[评估器] 处理节点: " + nodeId);

                // 获取节点内存
                PhreakMemory.NodeMemory nodeMemory = segmentMemory.getNodeMemory();

                // 执行节点评估逻辑
                boolean nodeResult = evaluateNode(nodeId, nodeMemory);
                nodeMemory.updateState("evaluated_fact", nodeResult);
                System.out.println("[评估器] 节点 " + nodeId + " 评估结果: " + nodeResult);

                if (!nodeResult) {
                    segmentResult = false;
                    System.out.println("[评估器] 节点 " + nodeId + " 未通过，分段评估失败");
                    break; // 短路逻辑：一个节点失败则整个分段失败
                }
            }

            // 设置分段评估结果
            segmentMemory.setResult("evaluation_completed", segmentResult);
            System.out.println("[评估器] 分段 " + segmentId + " 评估完成: " + segmentResult);

            // 如果任何分段失败，整个规则失败
            if (!segmentResult) {
                ruleResult = false;
                System.out.println("[评估器] 分段 " + segmentId + " 失败，跳过规则的其他分段");
                break; // 短路逻辑：一个分段失败则整个规则失败
            }
        }

        // 设置规则执行结果
        String result = ruleResult ? "SUCCESS" : "FAILURE";
        ruleMemory.setContext("rule_execution_result", result);
        ruleMemory.setContext("pending_fact_count", 0); // 处理完毕，重置计数
        System.out.println("[评估器] 规则 " + ruleId + " 评估完成，结果: " + result);
    }

    // 评估单个节点
    private boolean evaluateNode(String nodeId, PhreakMemory.NodeMemory nodeMemory) {
        Object fact = nodeMemory.getState("new_fact");
        System.out.println("[评估器] 节点 " + nodeId + " 评估事实: " + fact);

        // 节点1: 检查订单金额是否满1000
        if ("node_amount".equals(nodeId)) {
            return fact != null && fact.toString().contains("amount=")
                    && Integer.parseInt(fact.toString().split("=")[1]) >= 1000;
        }
        // 节点2: 检查是否为VIP用户
        else if ("node_vip".equals(nodeId)) {
            return fact != null && fact.toString().contains("isVip=true");
        }
        // 节点3: 检查是否为新用户
        else if ("node_newUser".equals(nodeId)) {
            return fact != null && fact.toString().contains("isNewUser=true");
        }

        return false;
    }
}