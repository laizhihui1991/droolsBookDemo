package com.drools.algorithm.rete.fact;

/**
 * @Author laizhihui
 * @Date 2025/1/21 11:43
 * @PackageName:com.test.drools.pojo
 * @ClassName: OrderResult
 * @Description: TODO
 * @Version 1.0
 */
public class OrderResult {

    private String content;

    private Integer code;

    public OrderResult() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
