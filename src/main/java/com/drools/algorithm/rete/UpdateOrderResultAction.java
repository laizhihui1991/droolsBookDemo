package com.drools.algorithm.rete;

import com.drools.algorithm.rete.fact.OrderResult;

import java.util.List;

public class UpdateOrderResultAction implements Action {
    @Override
    public void execute(List<Object> facts) {
        for (Object fact : facts) {
            if (fact instanceof OrderResult) {
                OrderResult orderResult = (OrderResult) fact;
                orderResult.setContent("余额不足，无法完成支付");
                orderResult.setCode(12001);
                System.out.println("订单结果更新: " + orderResult.getCode() + " " + orderResult.getContent());
            }
        }
    }
}
