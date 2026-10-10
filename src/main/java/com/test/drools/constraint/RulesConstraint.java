package com.test.drools.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.test.drools.pojo.Behavior;
import com.test.drools.pojo.HouseholdRegister;
import com.test.drools.pojo.Keyword;
import com.test.drools.pojo.Person;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

/**
 * @author laizhihui on 2021/1/31
 */
public class RulesConstraint {

    @Test
    public void testConstraintOperator() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintOperator");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        ks.insert(person);
        ks.insert(householdRegister);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }


    @Test
    public void testConstraintNested() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintOperator");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        Behavior behavior = new Behavior();
        behavior.setRun("我要跑");
        person.setBehavior(behavior);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintSubtype() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintOperator");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        person.setHouseholdRegister(householdRegister);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintCollection() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintOperator");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        person.setHouseholdRegister(householdRegister);
        List list = new ArrayList();
        list.add("小编");
        list.add("小李");
        list.add("小张");
        ks.insert(list);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintFieldContains() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        ks.insert(person);
        ks.insert(householdRegister);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }


    @Test
    public void testConstraintFieldContainsList() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        ks.insert(person);
        List list = new ArrayList();
        list.add("小编");
        list.add("小李");
        list.add("小张");
        ks.insert(list);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintFieldMemberOfList() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        ks.insert(person);
        List list = new ArrayList();
        list.add("小李");
        list.add("小张");
        list.add("小编");
        ks.insert(list);
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        ks.insert(householdRegister);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintFieldMatches() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintFieldSoundslike() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("Jon");
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintFieldStr() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintFieldIn() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintField");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }


    @Test
    public void testConstraintCalculate() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintCalculate");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintObject() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintObject");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        Behavior behavior = new Behavior();
        person.setBehavior(behavior);
        ks.insert(person);
        ks.insert(behavior);
        ks.insert(householdRegister);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintObjectFrom() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintObject");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        Behavior behavior = new Behavior();
        behavior.setRun("我要跑");
        person.setBehavior(behavior);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }


    @Test
    public void testConstraintObjectCollect() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintObject");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");

        Person person1 = new Person();
        person1.setAge(34);
        person1.setName("小李");

        Person person2 = new Person();
        person2.setAge(24);
        person2.setName("小柱");

        ks.insert(person);
        ks.insert(person1);
        ks.insert(person2);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }


    @Test
    public void testConstraintExtends() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintExtension");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        HouseholdRegister householdRegister = new HouseholdRegister();
        householdRegister.setName("小编");
        ks.insert(person);
        ks.insert(householdRegister);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintList() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintExtension");
        List list = new ArrayList();
        list.add("工商银行");
        list.add("招商银行");
        list.add("平安银行");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        person.setPayChecks(list);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintMap() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintExtension");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        Map  map=new HashMap<>(3);
        map.put("小编","北京");
        map.put("小柱","天津");
        map.put("小李","南京");
        person.setAddressMap(map);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintSet() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintExtension");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        Set set=new HashSet<>(3);
        set.add("一班");
        set.add("二班");
        set.add("三班");
        person.setClassNames(set);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintInsetList() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintExtension");
        List list = new ArrayList();
        list.add("工商银行");
        list.add("招商银行");
        list.add("平安银行");
        ks.insert(list);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintEntryPoint() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintEntrypoint");
        EntryPoint atmStream = ks.getEntryPoint("testPoint");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        atmStream.insert(person);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testConstraintEntryPoints() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionConstraintEntrypoint");
        EntryPoint atmStream = ks.getEntryPoint("testPoint");
        EntryPoint pointStream = ks.getEntryPoint("PersonPoint");
        Person person = new Person();
        person.setAge(30);
        person.setName("小编");
        atmStream.insert(person);
        pointStream.insert(person);
        ks.insert(person);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testKeyWord() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionKeyWord");
        Keyword keyword = new Keyword();
        keyword.setThen("关键字then");
        keyword.setWhen("关键字when");
        keyword.setRule("关键字rule");
        keyword.setEnd("关键字end");
        keyword.setAction("关键字action");
        keyword.setGlobal("关键字global");
        keyword.setNot("关键字not");
        keyword.setEval("关键字eval");
        keyword.setInit("关键字init");
        ks.insert(keyword);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testError() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionError");
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testRuleName() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionRuleName");
        //全部执行
        //int count = ks.fireAllRules();
        //指定参数关键字开头的被指定
        //int count = ks.fireAllRules(new RuleNameStartsWithAgendaFilter("starts"));
        //指定参数关键字结尾的被指定
        //int count = ks.fireAllRules(new RuleNameEndsWithAgendaFilter("end"));
        //指定参数关键字相等的被指定
        //int count = ks.fireAllRules(new RuleNameEqualsAgendaFilter("rule name test"));
        //指定参数关键字正则匹配的被指定
//        int count = ks.fireAllRules(new RuleNameMatchesAgendaFilter("[1-9]\\d{5}"));
        int count = ks.fireAllRules(2);
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testCustomRuleName() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionRuleName");
        Set<String> ruleName=new HashSet<>();
        ruleName.add("rule name test");
        ruleName.add("100000");
        int count = ks.fireAllRules(new CustomAgendaFilter(ruleName));
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testKieSessionExplanation() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionExplanation");
        Keyword keyword = new Keyword();
        keyword.setThen("关键字then");
        keyword.setWhen("关键字when");
        keyword.setRule("关键字rule");
        keyword.setEnd("关键字end");
        keyword.setAction("关键字action");
        keyword.setGlobal("关键字global");
        keyword.setNot("关键字not");
        keyword.setEval("关键字eval");
        keyword.setInit("关键字init");
        ks.insert(keyword);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

    @Test
    public void testkieSessionKeyWord() {
        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks = kc.newKieSession("kieSessionDoIf");
        Map  map=new HashMap();
        map.put("status",9);
        map.put("message",8);
        ks.insert(map);
        int count = ks.fireAllRules();
        System.out.println("总执行了" + count + "条规则");
        ks.dispose();
    }

}