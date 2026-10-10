package com.test.drools.filewhole;

import com.test.drools.pojo.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;

/**
 * @author laizhihui
 */
public class RuleFileWholeTest {

    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionWhole");
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void testFunction(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionWhole");
        kieSession.fireAllRules();
        kieSession.dispose();
    }
    @Test
    public void testQueryPersonAgeEquals30() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionQuery");
        Person person1 = new Person("来家大少", 32);
        Person person2 = new Person("来家二少", 30);
        Person person3 = new Person("来家大小姐", 30);
        kieSession.insert(person1);
        kieSession.insert(person2);
        kieSession.insert(person3);
        QueryResults queryResults = kieSession.getQueryResults("query Person age equals 30");
        for (QueryResultsRow q : queryResults) {
            Person p = (Person) q.get("person");
            System.out.println("符合查询条件的 Person 名字：" + p.getName() + "  年龄：" + p.getAge());
        }
        kieSession.dispose();
    }

    @Test
    public void testQueryPersonAgeLessThan25() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionQuery");
        Person person1 = new Person("来家大少", 32);
        Person person2 = new Person("来家二少", 30);
        Person person3 = new Person("来家大小姐", 30);
        kieSession.insert(person1);
        kieSession.insert(person2);
        kieSession.insert(person3);
        QueryResults queryResults = kieSession.getQueryResults("query Person age Less than 25", "来家二少", 25);
        for (QueryResultsRow q : queryResults) {
            Person p = (Person) q.get("person");
            System.out.println("符合查询条件的 Person 名字：" + p.getName() + "  年龄：" + p.getAge());
        }
        kieSession.dispose();
    }

    @Test
    public void testRuleQuery() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionQuery");
        Person person1 = new Person("来家大少", 32);
        Person person2 = new Person("来家二少", 30);
        Person person3 = new Person("来家大小姐", 24);
        kieSession.insert(person1);
        kieSession.insert(person2);
        kieSession.insert(person3);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void testRuleDeclare() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionDeclare");
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void testRuleGlobal() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionGlobal");
        kieSession.setGlobal("names", new ArrayList<>());
        kieSession.setGlobal("globalAge", 30);
        Person person1 = new Person("来家大少", 32);
        Person person2 = new Person("来家二少", 30);
        Person person3 = new Person("来家大小姐", 24);
        Person person4 = new Person("来家小叔叔", 40);
        kieSession.insert(person1);
        kieSession.insert(person2);
        kieSession.insert(person3);
        kieSession.insert(person4);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}