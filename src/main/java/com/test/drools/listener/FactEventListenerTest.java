package com.test.drools.listener;

import com.test.drools.pojo.Person;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

/**
 * @author laizhihui
 */
public class FactEventListenerTest {

    @Test
    public void eventListener() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionRuleFact");
        kieSession.addEventListener( new DebugRuleRuntimeEventListener() {
            @Override
            public void objectInserted(ObjectInsertedEvent event) {
                System.out.println("FACT INSERTED: fact= " + event.getObject());
            }
            @Override
            public void objectUpdated(ObjectUpdatedEvent event) {
                System.out.println("FACT UPDATED: " + event.getObject());
            }
            @Override
            public void objectDeleted(ObjectDeletedEvent event) {
                System.out.println("FACT DELETED: " + event.getOldObject());
            }
        });
        Person person = new Person();
        person.setName("来家二少");
        kieSession.insert(person);
        kieSession.fireAllRules();
        person.setAge(30);
        kieSession.dispose();
        System.out.println("Person="+person);
    }

    @Test
    public void myEventListener() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("kieSessionRuleFact");
        kieSession.addEventListener( new MyRuleRuntimeEventListener());
        Person person = new Person();
        person.setName("来家二少");
        kieSession.insert(person);
        kieSession.fireAllRules();
        person.setAge(30);
        kieSession.dispose();
        System.out.println("Person="+person);
    }

    @Test
    public void myStatelessEventListener() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        StatelessKieSession kieSessionStatelessRuleFact = kieContainer.newStatelessKieSession("kieSessionStatelessRuleFact");
        kieSessionStatelessRuleFact.addEventListener( new MyRuleRuntimeEventListener());
        Person person = new Person();
        person.setName("来家二少");
        kieSessionStatelessRuleFact.execute(person);
        System.out.println("Person="+person);
    }
}
