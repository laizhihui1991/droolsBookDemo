package com.drools.algorithm.rete;


import com.drools.algorithm.rete.fact.*;
import com.drools.algorithm.rete.node.AlphaNode;
import com.drools.algorithm.rete.node.BetaNode;
import com.drools.algorithm.rete.node.ObjectTypeNode;
import com.drools.algorithm.rete.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class ReteExample {
    public static void main(String[] args) {
        ReteNetwork reteNetwork = new ReteNetwork();
        // 创建对象类型节点
        ObjectTypeNode userObjectTypeNode = new ObjectTypeNode("User");
        ObjectTypeNode orderObjectTypeNode = new ObjectTypeNode("Order");
        ObjectTypeNode accountObjectTypeNode = new ObjectTypeNode("Account");
        ObjectTypeNode orderResultObjectTypeNode = new ObjectTypeNode("OrderResult");
        // 为 User 对象添加 Alpha 节点
        userObjectTypeNode.addAlphaNode(new AlphaNode(new Condition("id", 10000, "==")));
        userObjectTypeNode.addAlphaNode(new AlphaNode(new Condition("name", "来家二少", "==")));
        userObjectTypeNode.addAlphaNode(new AlphaNode(new Condition("age", 30, "==")));
        // 为 Order 对象添加 Alpha 节点
        orderObjectTypeNode.addAlphaNode(new AlphaNode(new Condition("userId", 10000, "==")));
        // 将对象类型节点添加到 Rete 网络中
        reteNetwork.addObjectTypeNode(userObjectTypeNode);
        reteNetwork.addObjectTypeNode(orderObjectTypeNode);
        reteNetwork.addObjectTypeNode(accountObjectTypeNode);
        reteNetwork.addObjectTypeNode(orderResultObjectTypeNode);
        // 创建 Beta 节点，用于连接不同对象的条件
        BetaNode userOrderBetaNode = new BetaNode();
        userOrderBetaNode.addJoinCondition(new Condition("userId", 10000, "=="));
        reteNetwork.addBetaNode(userOrderBetaNode);

        BetaNode orderAccountBetaNode = new BetaNode();
        orderAccountBetaNode.addJoinCondition(new Condition("userId", 10000, "=="));
        reteNetwork.addBetaNode(orderAccountBetaNode);
        // 创建规则
        Rule rule = new Rule();
        // 添加用户信息的条件
        rule.addCondition(new Condition("id", 10000, "=="));
        rule.addCondition(new Condition("name", "来家二少", "=="));
        rule.addCondition(new Condition("age", 30, "=="));
        // 添加订单信息的条件
        rule.addCondition(new Condition("userId", 10000, "=="));
        // 订单金额和账户余额的条件将在插入对象时动态处理
        // 设置规则的动作
        rule.setAction(new UpdateOrderResultAction());
        reteNetwork.addRule(rule);
        // 插入模块
        User user = new User(10000, "来家二少", 30);
        Order order = new Order(10000, 500.0);
        Account account = new Account(10000, 300.0);
        OrderResult orderResult = new OrderResult();
        // 将对象插入 Rete 网络
        reteNetwork.insert(user);
        reteNetwork.insert(order);
        reteNetwork.insert(account);
        reteNetwork.insert(orderResult);
        // 确保规则的条件在插入对象后动态处理订单金额和账户余额
        List<Object> matchedFacts = new ArrayList<>();
        matchedFacts.add(user);
        matchedFacts.add(order);
        matchedFacts.add(account);
        matchedFacts.add(orderResult);
        // 为 Account 添加动态条件，确保 balance < $orderAmount
        Condition accountBalanceCondition = new Condition("balance", order.getAmount(), "<");
        rule.addCondition(accountBalanceCondition);
        // 重新评估规则
        if (rule.match(matchedFacts)) {
            rule.execute(matchedFacts);
        }
    }
}