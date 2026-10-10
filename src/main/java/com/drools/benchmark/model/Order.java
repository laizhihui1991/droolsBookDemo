package com.drools.benchmark.model;

/**
 * 用于 Drools 并发测试的订单事实。
 */
public class Order {
    /**
     * 订单唯一标识，用于并发污染追踪。
     */
    private final String orderId;
    /**
     * 不可变金额。
     */
    private final double amount;
    /**
     * 不可变客户等级，只允许 1、2、3。
     */
    private final int customerLevel;
    /**
     * 规则后果写入的可变结果。
     */
    private String result;

    /**
     * 创建订单事实。
     */
    public Order(String orderId, double amount, int customerLevel) {
        this.orderId = orderId;
        this.amount = amount;
        this.customerLevel = customerLevel;
    }

    /**
     * @return 订单标识
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @return 订单金额
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return 客户等级
     */
    public int getCustomerLevel() {
        return customerLevel;
    }

    /**
     * @return 规则处理结果
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result 规则处理结果
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 清理历史结果以支持重复插入。
     */
    public void clearResult() {
        result = null;
    }

    /**
     * @return 含所有字段的可诊断文本
     */
    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', amount=" + amount + ", customerLevel=" + customerLevel + ", result='" + result + "'}";
    }
}
