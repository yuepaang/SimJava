package com.simjava.core;

import java.util.ArrayList;
import java.util.List;
import com.simjava.action.*;

public class Event {

    private Environment environment;
    private List<Action<Event>> callBackList;

    private String value;
    private boolean isOK;
    private boolean isAlive;
    private boolean isProcessed;
    private boolean isTrigger;

    public Environment getEnvironment() {
        return environment;
    }

    private void setEnvironment(Environment environment) {
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

    protected void setOK(boolean OK) {
        isOK = OK;
    }

    public boolean isAlive() {
        return !isTrigger() && !isProcessed();
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    protected void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isTrigger() {
        return isTrigger;
    }

    protected void setTrigger(boolean trigger) {
        isTrigger = trigger;
    }

    public List<Action<Event>> getCallBackList() {
        return callBackList;
    }

    public void setCallBackList(List<Action<Event>> callBackList) {
        this.callBackList = callBackList;
    }

    public Event(){
        this.environment = null;
        this.value = "";
        this.callBackList = new ArrayList<>();
        }

    public Event(Environment env){
        this.environment = env;
        this.value = "";
        this.callBackList = new ArrayList<>();

    }

    public void Trigger(Event event, int priority){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setOK(event.isOK());
        setValue(event.getValue());
        setTrigger(true);
        this.environment.Schedule(this, priority);
    }

    public void Succeed(String data, int priority){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setOK(true);
        setValue(data);
        setTrigger(true);
        this.environment.Schedule(this, priority);
    }

    public void Fail(String err, int priority){
        if (isTrigger()) {
            throw new ArithmeticException("already triggered");
        }
        setOK(false);
        setValue(err);
        setTrigger(true);
        this.environment.Schedule(this, priority);
    }

    public void AddCallback(Action<Event> callback) {
        if (isProcessed()) throw new ArithmeticException("Event is already processed.");
        this.callBackList.add(callback);
    }

    public void RemoveCallback(Action<Event> callback) {
        if (isProcessed()) throw new ArithmeticException("Event is already processed.");
        this.callBackList.remove(callback);
    }

    public void Process(){
        if (isProcessed()) {
            throw new ArithmeticException("already triggered");
        }
        setProcessed(true);
        for (int i = 0; i < this.callBackList.size(); i++){
            this.callBackList.get(i).invoke(this);
        }
        setCallBackList(null);
    }
}

