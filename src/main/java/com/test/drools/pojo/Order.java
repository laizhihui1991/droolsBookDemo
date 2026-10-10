package com.test.drools.pojo;

/**
 * @Author laizhihui
 * @Date 2025/1/21 11:40
 * @PackageName:com.test.drools.pojo
 * @ClassName: Order
 * @Description: TODO
 * @Version 1.0
 */
public class Order {

    private Integer userId;

    private Double amount;

    public Order() {
    }

    public Order(Integer userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
