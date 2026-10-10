package com.test.drools.channel;

import com.test.drools.pojo.Order;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class ChannelTest {

    @Test
    public void channel() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionRuleChannel");
        Order order = new Order();
        order.setAmount(2000.0);
        kieSession.insert(order);
        int count = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("规则成功被激活 " + count + " 条");
    }
}
