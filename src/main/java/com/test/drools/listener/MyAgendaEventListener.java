package com.test.drools.listener;

import org.kie.api.event.rule.*;

public class MyAgendaEventListener implements AgendaEventListener {
    @Override
    public void matchCreated(MatchCreatedEvent event) {
        System.out.println("[MATCH CREATED] Rule: " + event.getMatch().getRule().getName());
    }

    @Override
    public void matchCancelled(MatchCancelledEvent event) {
        System.out.println("[MATCH CANCELLED] Rule: " + event.getMatch().getRule().getName());
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        System.out.println("[BEFORE MATCH FIRED] Rule: " + event.getMatch().getRule().getName());
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        System.out.println("[AFTER MATCH FIRED] Rule: " + event.getMatch().getRule().getName());
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
        System.out.println("[AGENDA GROUP POPPED] " + event.getAgendaGroup().getName());
    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent event) {
        System.out.println("[AGENDA GROUP PUSHED] " + event.getAgendaGroup().getName());
    }
    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {}
    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {}
    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {}
    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {}
}
