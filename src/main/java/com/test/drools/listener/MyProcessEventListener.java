package com.test.drools.listener;

import org.kie.api.event.process.*;

public class MyProcessEventListener implements ProcessEventListener {

    @Override
    public void beforeProcessStarted(ProcessStartedEvent event) {
        System.out.println("[PROCESS STARTING] " + event.getProcessInstance().getProcessName());
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
        System.out.println("[PROCESS STARTED] " + event.getProcessInstance().getProcessName());
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent event) {
        System.out.println("[PROCESS COMPLETING] " + event.getProcessInstance().getProcessName());
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
        System.out.println("[PROCESS COMPLETED] " + event.getProcessInstance().getProcessName());
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        System.out.println("[NODE TRIGGERED] " + event.getNodeInstance().getNodeName());
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
        System.out.println("[NODE TRIGGERED COMPLETED] " + event.getNodeInstance().getNodeName());
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        System.out.println("[NODE LEAVING] " + event.getNodeInstance().getNodeName());
    }

    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent event) {
        System.out.println("[NODE LEFT] " + event.getNodeInstance().getNodeName());
    }

    @Override
    public void beforeVariableChanged(ProcessVariableChangedEvent event) {
        System.out.println("[VARIABLE CHANGING] " + event.getVariableId() + " = " + event.getOldValue());
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent event) {
        System.out.println("[VARIABLE CHANGED] " + event.getVariableId() + " = " + event.getNewValue());
    }

    @Override
    public void beforeSLAViolated(SLAViolatedEvent event) {}
    @Override
    public void afterSLAViolated(SLAViolatedEvent event) {}
}
