package com.test.drools.listener;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

public class MyRuleRuntimeEventListener implements RuleRuntimeEventListener {

    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        System.out.println("[FACT INSERTED] " + event.getObject());
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        System.out.println("[FACT UPDATED] " + event.getObject());
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        System.out.println("[FACT DELETED] " + event.getOldObject());
    }
}
