package com.test.drools.dmn;

import com.drools.algorithm.rete.fact.User;
import com.test.drools.pojo.Order;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.api.DMNFactory;

public class DroolsDMNTest {

    @Test
    public void testDMN() {
        // 1. 获取KieServices
        KieServices kieServices = KieServices.Factory.get();
        // 2. 创建KieFileSystem
        KieFileSystem kfs = kieServices.newKieFileSystem();
        // 3. 加载DMN资源
        Resource resource = kieServices.getResources().newClassPathResource("dmn/userOrder.dmn");
        kfs.write(resource);
        // 4. 编译DMN
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            kieBuilder.getResults().getMessages()
                    .forEach(System.out::println);
            return;
        }
        // 5. 创建KieContainer
        KieContainer kieContainer = kieServices.newKieContainer(
                kieServices.getRepository().getDefaultReleaseId());
        // 6. 获取KieBase
        KieBase kieBase = kieContainer.getKieBase();
        // 7. 获取DMNRuntime
        DMNRuntime dmnRuntime = KieRuntimeFactory
                .of(kieBase)
                .get(DMNRuntime.class);
        // 8. 获取DMN模型
        DMNModel dmnModel = dmnRuntime.getModel(
                "https://rule.tojoy.com/dmn/order",
                "UserOrderDMN"
        );
        if (dmnModel == null) {
            System.out.println("DMN模型不存在！");
            return;
        }
        // 9. 创建上下文
        DMNContext context = DMNFactory.newContext();
        User user = new User(
                10000,
                "来家二少",
                30
        );
        Order order = new Order(10000, 300.0);
        context.set("user", user);
        context.set("order", order);
        // 10. 执行DMN
        DMNResult result = dmnRuntime.evaluateAll(dmnModel, context);
        // 11. 输出结果
        System.out.println("执行结果：");
        System.out.println(result.getContext().get("message"));
    }
}