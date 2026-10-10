package com.drools.algorithm.reteOO;

import com.drools.algorithm.reteOO.fact.*;
import com.drools.algorithm.reteOO.node.AlphaNode;
import com.drools.algorithm.reteOO.node.JoinNode;
import com.drools.algorithm.reteOO.node.ObjectTypeNode;
import com.drools.algorithm.reteOO.node.TerminalNode;

public class ReteExample {

    public static void main(String[] args) {
        WorkingMemory wm = new WorkingMemory();

        // 构建节点
        ObjectTypeNode userNode = new ObjectTypeNode(User.class);
        AlphaNode userFilter = new AlphaNode(f -> {
            User u = (User) f;
            return u.getId() == 10000 && u.getName().equals("来家二少") && u.getAge() == 30;
        });
        userNode.addSink(userFilter);

        ObjectTypeNode orderNode = new ObjectTypeNode(Order.class);
        ObjectTypeNode accountNode = new ObjectTypeNode(Account.class);
        ObjectTypeNode resultNode = new ObjectTypeNode(OrderResult.class);

        // Join: User + Order
        JoinNode join1 = new JoinNode(User.class, Order.class, (l, r) -> {
            User user = (User) l;
            Order order = (Order) r;
            return user.getId() == order.getUserId();
        });

        // Join: (User+Order) + Account
        JoinNode join2 = new JoinNode(FactTuple.class, Account.class, (l, r) -> {
            FactTuple tuple = (FactTuple) l;
            User user = (User) tuple.getParts()[0];
            Order order = (Order) tuple.getParts()[1];
            Account account = (Account) r;
            return account.getUserId() == user.getId() && account.getBalance() < order.getAmount();
        });

        // Join: (User+Order+Account) + Result
        JoinNode join3 = new JoinNode(FactTuple.class, OrderResult.class, (l, r) -> {
            FactTuple tuple = (FactTuple) l;
            return true;
        });

        TerminalNode terminal = new TerminalNode();

        // 链接节点
        userFilter.addSink(join1);
        orderNode.addSink(join1);
        join1.setSink(join2);
        accountNode.addSink(join2);
        join2.setSink(join3);
        resultNode.addSink(join3);
        join3.setSink(terminal);

        // 创建事实并插入
        User user = new User(10000, "来家二少", 30);
        Order order = new Order(10000, 200);
        Account account = new Account(10000, 200.01);
        OrderResult result = new OrderResult();

        userNode.assertObject(user, wm);
        orderNode.assertObject(order, wm);
        accountNode.assertObject(account, wm);
        resultNode.assertObject(result, wm);
    }
}