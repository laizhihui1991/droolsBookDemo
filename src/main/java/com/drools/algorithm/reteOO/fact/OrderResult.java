package com.drools.algorithm.reteOO.fact;

public class OrderResult implements Fact {
    private String content;
    private int code;

    public OrderResult() {
        this.content = "";
        this.code = 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "content='" + content + '\'' +
                ", code=" + code +
                '}';
    }
}
