package com.test.drools.commonly;

import java.util.ArrayList;
import com.test.drools.pojo.Students;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author laizhihui on 2022/2/5
 */
public class RuleFlowTest {

    @Test
    public void ruleFlowTest01() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionRuleFlow");
        Students s1=new Students("来家大少",600,new ArrayList<>());
        Students s2=new Students("来家二少",690,new ArrayList<>());
        Students s3=new Students("来家三少",568,new ArrayList<>());
        Students s4=new Students("来家大小姐",700,new ArrayList<>());
        ks.insert(s1);
        ks.insert(s2);
        ks.insert(s3);
        ks.insert(s4);
        //流程启动
        ks.startProcess("gotoSchool");
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }
}
