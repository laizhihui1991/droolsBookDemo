package com.test.drools.helloworld;

import com.test.drools.pojo.Person;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

public class DroolsHelloWorld {

    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("testhelloworld");
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void testImportPerson(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("testhelloworld");
        Person person=new Person();
        kieSession.insert(person);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void testIdentityPerson() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kc = kieServices.getKieClasspathContainer();
        KieSession kieSession = kc.newKieSession("kieSessionEquals");
        Person person1 = new Person();
        person1.setName("张三");
        kieSession.insert(person1);
        kieSession.fireAllRules();
        //再次insert Person
        Person person2 = new Person();
        person2.setName("张三");
        kieSession.insert(person2);
        kieSession.fireAllRules();
        FactHandle factHandle1 = kieSession.getFactHandle(person1);
        FactHandle factHandle2 = kieSession.getFactHandle(person2);
        System.out.println("factHandle1 与 factHandle2 的equals结果是：" + factHandle1.equals(factHandle2));
        kieSession.dispose();
    }

    @Test
    public void testSamePerson() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kc = kieServices.getKieClasspathContainer();
        KieSession kieSession = kc.newKieSession("kieSessionEquals");
        Person person1 = new Person();
        person1.setName("张三");
        kieSession.insert(person1);
        kieSession.fireAllRules();
        //再次insert Person
        Person person2 = person1;
        person2.setName("张三");
        kieSession.insert(person2);
        kieSession.fireAllRules();
        FactHandle factHandle1 = kieSession.getFactHandle(person1);
        FactHandle factHandle2 = kieSession.getFactHandle(person2);
        System.out.println("factHandle1 与 factHandle2 的equals结果是：" +factHandle1.equals(factHandle2));
        System.out.println("这是 factHandle1 的值："+factHandle1);
        System.out.println("这是 factHandle2 的值："+factHandle2);
        kieSession.dispose();
    }

    @Test
    public void testIdentityPersonExtension() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kc = kieServices.getKieClasspathContainer();
        KieBaseConfiguration kieBaseConf = kieServices.newKieBaseConfiguration();
        kieBaseConf.setOption(EqualityBehaviorOption.IDENTITY);
        KieBase kieBase = kc.newKieBase(kieBaseConf);
        KieSession kieSession = kieBase.newKieSession();
        Person person1 = new Person();
        person1.setName("张三");
        kieSession.insert(person1);
        kieSession.fireAllRules();
        //再次insert Person
        Person person2 = new Person();
        person2.setName("张三");
        kieSession.insert(person2);
        kieSession.fireAllRules();
        FactHandle factHandle1 = kieSession.getFactHandle(person1);
        FactHandle factHandle2 = kieSession.getFactHandle(person2);
        System.out.println("factHandle1 与 factHandle2 的equals结果是：" +factHandle1.equals(factHandle2));
        System.out.println("这是 factHandle1 的值："+factHandle1);
        System.out.println("这是 factHandle2 的值："+factHandle2);
        kieSession.dispose();
    }



}