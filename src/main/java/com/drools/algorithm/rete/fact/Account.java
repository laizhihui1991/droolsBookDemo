package com.drools.algorithm.rete.fact;

/**
 * @Author laizhihui
 * @Date 2025/1/21 11:41
 * @PackageName:com.test.drools.pojo
 * @ClassName: Account
 * @Description: TODO
 * @Version 1.0
 */
public class Account {

    private Integer userId;

    private Double balance;

    public Account() {
    }

    public Account(Integer userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
