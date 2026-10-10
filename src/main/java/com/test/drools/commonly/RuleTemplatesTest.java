package com.test.drools.commonly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import com.test.drools.pojo.Person;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.template.ObjectDataCompiler;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

/**
 * @author laizhihui on 2022/2/5
 */
public class RuleTemplatesTest {

    @Test
    public void testTemplates() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionCommonlyTemplates");
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void verificationDT() throws FileNotFoundException {
        File file = new File(
            "/Users/laizhihui/IdeaProjects/DroolsActualCombat/src/main/resources/rules/commonly/templates/templates_05.xlsx");
        InputStream is = new FileInputStream(file);
        SpreadsheetCompiler converter = new SpreadsheetCompiler();
        String drl = converter.compile(is, InputType.XLS);
        System.out.println(drl);
    }

    @Test
    public void testKieHelper() {
        Resource dis =
            ResourceFactory.newClassPathResource("rules/commonly/templates/templates_03.xlsx",
                RuleTemplatesTest.class);
        KieHelper helper = new KieHelper();
        helper.addResource(dis, ResourceType.DTABLE);
        KieSession ksession = helper.build().newKieSession();
        int i = ksession.fireAllRules();
        System.out.println(" " + i + "次");
        ksession.dispose();
    }

    @Test
    public void testConditionAction() {
        Resource dis =
            ResourceFactory.newClassPathResource("rules/commonly/templates/templates_04.xlsx",
                RuleTemplatesTest.class);
        KieHelper helper = new KieHelper();
        helper.addResource(dis, ResourceType.DTABLE);
        KieSession ksession = helper.build().newKieSession();
        int i = ksession.fireAllRules();
        System.out.println(" " + i + "次");
        ksession.dispose();
    }

    @Test
    public void testRuleTemplate01() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionRuleTemplates");
        Person person1 = new Person("来家大少", 32, "看书");
        kieSession.insert(person1);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void testRuleTemplate02() {
        ObjectDataCompiler converter = new ObjectDataCompiler();
        //赋值给模板属性
        Collection<Person> cfl = new ArrayList<>();
        cfl.add(new Person("张不二",30,"看书"));
        cfl.add(new Person("科比",28,"打篮球"));
        cfl.add(new Person("李小三",20,"看书"));
        ///每add一次，就代码一条规则
        InputStream dis = null;
        try {
            dis = ResourceFactory.newClassPathResource(
                "rules/commonly/templates/drts/ruleTemplates_02.drt",
                RuleTemplatesTest.class)
                .getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String drl = converter.compile(cfl, dis);
        System.out.println(drl);
        KieHelper helper = new KieHelper();
        helper.addContent(drl, ResourceType.DRL);
        KieSession kieSession = helper.build().newKieSession();
        Person person1 = new Person("来家大少", 32, "看书");
        kieSession.insert(person1);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
