package com.simjava.core;

import java.util.ArrayList;

public class Event {

    private Environment environment;
    private String value;
    private boolean isOK;
    private boolean isAlive;
    private boolean isProcessed;
    private boolean isTrigger;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isTrigger() {
        return isTrigger;
    }

    public void setTrigger(boolean trigger) {
        isTrigger = trigger;
    }

    //    private EventValue eventValue;
//    private ArrayList<String> callBacks;

    public Event(){
        this.environment = null;
        this.value = "";
//        this.eventValue = new EventValue("", true);
//        this.callBacks = new ArrayList<String>();

    }

    public Event(Environment env){
        this.environment = env;
        this.value = "";
//        this.callBacks = new ArrayList<String>();
//        this.eventValue = new EventValue("", true);
    }

    public Event(Environment env, String value){
        this.environment = env;
        this.value = value;
//        this.callBacks = callBacks;
//        this.eventValue = value;
    }

    public void Trigger(Event event, int priority){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setOK(event.isOK());
        setValue(event.getValue());
        setTrigger(true);
        this.environment.Schedule(this, priority, 0);
    }

    public void Succeed(String data, int priority){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setOK(true);
        setValue(data);
        setTrigger(true);
        this.environment.Schedule(this, priority, 0);
    }

    public void Fail(String err, int priority){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setOK(false);
        setValue(err);
        setTrigger(true);
        this.environment.Schedule(this, priority, 0);
    }

    public void Process(){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setProcessed(true);
    }
}

