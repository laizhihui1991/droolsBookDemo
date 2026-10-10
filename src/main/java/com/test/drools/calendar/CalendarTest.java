package com.test.drools.calendar;

import com.test.drools.pojo.Order;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class CalendarTest {

    @Test
    public void calendar() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionRuleCalendar");
        Order order = new Order();
        order.setAmount(2000.0);
        kieSession.insert(order);
        int i = kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("规则成功被激活 "+ i + "条 Order=" + order);
    }
}
