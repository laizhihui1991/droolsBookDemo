package com.test.drools.channel;

import org.kie.api.runtime.Channel;

public class OrderChannel implements Channel {

    @Override
    public void send(Object object) {
        System.out.println("接收到规则输出：" + object);
    }
}
