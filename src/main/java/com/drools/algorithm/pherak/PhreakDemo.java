package com.drools.algorithm.pherak;

public class PhreakDemo {
    public static void main(String[] args) {
        System.out.println("==== 电商促销规则引擎演示 ====");

        // 1. 创建规则链接管理器
        PhreakLinkManager linkManager = new PhreakLinkManager();

        // 注册三个规则
        linkManager.registerRuleSegments("rule_discount", new String[]{"segment_amount"});
        linkManager.registerRuleSegments("rule_vip", new String[]{"segment_vip"});
        linkManager.registerRuleSegments("rule_newUser", new String[]{"segment_newUser"});

        // 注册分段和节点关系
        linkManager.registerSegmentNodes("segment_amount", new String[]{"node_amount"});
        linkManager.registerSegmentNodes("segment_vip", new String[]{"node_vip"});
        linkManager.registerSegmentNodes("segment_newUser", new String[]{"node_newUser"});

        // 2. 创建内存结构
        // 订单金额规则内存
        PhreakMemory.NodeMemory amountNode = new PhreakMemory.NodeMemory();
        PhreakMemory.SegmentMemory amountSegment = new PhreakMemory.SegmentMemory(amountNode);
        PhreakMemory.RuleMemory discountRuleMemory = new PhreakMemory.RuleMemory(amountSegment);
        discountRuleMemory.setContext("segment_memory_segment_amount", amountSegment);
        discountRuleMemory.setContext("rule_id", "rule_discount");

        // VIP规则内存
        PhreakMemory.NodeMemory vipNode = new PhreakMemory.NodeMemory();
        PhreakMemory.SegmentMemory vipSegment = new PhreakMemory.SegmentMemory(vipNode);
        PhreakMemory.RuleMemory vipRuleMemory = new PhreakMemory.RuleMemory(vipSegment);
        vipRuleMemory.setContext("segment_memory_segment_vip", vipSegment);
        vipRuleMemory.setContext("rule_id", "rule_vip");

        // 新用户规则内存
        PhreakMemory.NodeMemory newUserNode = new PhreakMemory.NodeMemory();
        PhreakMemory.SegmentMemory newUserSegment = new PhreakMemory.SegmentMemory(newUserNode);
        PhreakMemory.RuleMemory newUserRuleMemory = new PhreakMemory.RuleMemory(newUserSegment);
        newUserRuleMemory.setContext("segment_memory_segment_newUser", newUserSegment);
        newUserRuleMemory.setContext("rule_id", "rule_newUser");

        // 3. 创建惰性评估器
        PhreakLazyEvaluator evaluator = new PhreakLazyEvaluator(linkManager);

        // ==== 场景1: 普通用户，订单金额800元 ====
        System.out.println("\n\n==== 场景1: 普通用户，订单金额800元 ====");
        simulateOrder(evaluator, discountRuleMemory, vipRuleMemory, newUserRuleMemory,
                amountNode, vipNode, newUserNode,
                800, false, false);

        // ==== 场景2: VIP用户，订单金额1200元 ====
        System.out.println("\n\n==== 场景2: VIP用户，订单金额1200元 ====");
        simulateOrder(evaluator, discountRuleMemory, vipRuleMemory, newUserRuleMemory,
                amountNode, vipNode, newUserNode,
                1200, true, false);

        // ==== 场景3: 新用户，订单金额500元 ====
        System.out.println("\n\n==== 场景3: 新用户，订单金额500元 ====");
        simulateOrder(evaluator, discountRuleMemory, vipRuleMemory, newUserRuleMemory,
                amountNode, vipNode, newUserNode,
                500, false, true);
    }

    private static void simulateOrder(PhreakLazyEvaluator evaluator,
                                      PhreakMemory.RuleMemory discountRuleMemory,
                                      PhreakMemory.RuleMemory vipRuleMemory,
                                      PhreakMemory.RuleMemory newUserRuleMemory,
                                      PhreakMemory.NodeMemory amountNode,
                                      PhreakMemory.NodeMemory vipNode,
                                      PhreakMemory.NodeMemory newUserNode,
                                      int amount, boolean isVip, boolean isNewUser) {

        // 1. 重置规则状态（修正点1：添加状态重置）
        System.out.println("\n--- 步骤1: 重置规则状态 ---");
        discountRuleMemory.setContext("pending_fact_count", 0);
        vipRuleMemory.setContext("pending_fact_count", 0);
        newUserRuleMemory.setContext("pending_fact_count", 0);
        amountNode.updateState("new_fact", null);
        vipNode.updateState("new_fact", null);
        newUserNode.updateState("new_fact", null);

        // 2. 设置订单信息（不立即评估）
        System.out.println("\n--- 步骤2: 设置订单信息 ---");

        // 设置金额规则的事实
        discountRuleMemory.setContext("pending_fact_count", 1);
        amountNode.updateState("new_fact", "amount=" + amount);

        // 设置VIP规则的事实
        vipRuleMemory.setContext("pending_fact_count", 1);
        vipNode.updateState("new_fact", "isVip=" + isVip);

        // 设置新用户规则的事实
        newUserRuleMemory.setContext("pending_fact_count", 1);
        newUserNode.updateState("new_fact", "isNewUser=" + isNewUser);

        // 3. 添加待评估规则（仍不评估）
        System.out.println("\n--- 步骤3: 添加待评估规则 ---");
        evaluator.addPendingRule("rule_discount");
        evaluator.addPendingRule("rule_vip");
        evaluator.addPendingRule("rule_newUser");

        // 4. 执行其他业务逻辑（订单校验等）
        System.out.println("\n--- 步骤4: 执行其他业务逻辑 ---");
        System.out.println("[系统] 执行订单校验...");
        System.out.println("[系统] 检查库存...");

        // 5. 用户提交订单，显式触发规则评估（修正点2：统一使用主规则内存）
        System.out.println("\n--- 步骤5: 用户提交订单，触发规则评估 ---");
        evaluator.checkAndEvaluate(discountRuleMemory, "orderSubmitted");
        evaluator.checkAndEvaluate(vipRuleMemory, "orderSubmitted");
        evaluator.checkAndEvaluate(newUserRuleMemory, "orderSubmitted");

        // 6. 应用评估结果
        System.out.println("\n--- 步骤6: 应用促销结果 ---");
        double finalPrice = amount;

        // 应用满减折扣
        if ("SUCCESS".equals(discountRuleMemory.getContext("rule_execution_result"))) {
            finalPrice = amount * 0.9;  // 9折
            System.out.println("✓ 应用满减折扣: 原价" + amount + "元 → 折扣后" + finalPrice + "元");
        }

        // 应用VIP优惠
        if ("SUCCESS".equals(vipRuleMemory.getContext("rule_execution_result"))) {
            finalPrice -= 50;
            System.out.println("✓ 应用VIP优惠: 再减50元 → " + finalPrice + "元");
        }

        // 应用新用户优惠券
        if ("SUCCESS".equals(newUserRuleMemory.getContext("rule_execution_result"))) {
            System.out.println("✓ 新用户优惠券已发放到账户");
        }

        System.out.println("最终支付金额: " + finalPrice + "元");
    }
}