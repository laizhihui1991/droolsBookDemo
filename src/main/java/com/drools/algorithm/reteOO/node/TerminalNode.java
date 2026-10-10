package com.drools.algorithm.reteOO.node;

import com.drools.algorithm.rete.fact.OrderResult;
import com.drools.algorithm.reteOO.WorkingMemory;
import com.drools.algorithm.reteOO.fact.*;

import java.util.ArrayList;
import java.util.List;

public class TerminalNode implements Node {
    @Override
    public void assertObject(Fact fact, WorkingMemory wm) {
        List<Object> flatParts = new ArrayList<>();
        flatten(fact, flatParts);

        if (flatParts.size() == 4 &&
                flatParts.get(0) instanceof User &&
                flatParts.get(1) instanceof Order &&
                flatParts.get(2) instanceof Account &&
                flatParts.get(3) instanceof OrderResult) {

            User user = (User) flatParts.get(0);
            Order order = (Order) flatParts.get(1);
            Account account = (Account) flatParts.get(2);
            OrderResult result = (OrderResult) flatParts.get(3);

            result.setContent("余额不足，无法完成支付");
            result.setCode(12001);

            System.out.println("✔ 规则匹配成功 -> " + result);
        }
    }

    private void flatten(Fact fact, List<Object> out) {
        if (fact instanceof FactTuple) {
            for (Object o : ((FactTuple) fact).getParts()) {
                flatten((Fact) o, out);
            }
        } else {
            out.add(fact);
        }
    }
}


