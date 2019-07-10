package com.logilog.simjava.core;

import java.util.ArrayList;
import java.util.List;
import com.logilog.simjava.action.*;

public class Event {

    public Environment environment;
    public List<Action<Event>> callBackList;
    public Object value;

    public boolean isOK;
    public boolean isAlive;
    public boolean isProcessed;
    public boolean isTriggered;

    public Event(){
        this.environment = null;
        this.value = new Object();
        this.callBackList = new ArrayList<>();
        this.isTriggered = false;
        }

    public Event(Environment env){
        this.environment = env;
        this.value = new Object();
        this.callBackList = new ArrayList<>();
        this.isTriggered = false;

    }

    public void Trigger(Event event, int priority){
        if (isTriggered) {
            throw new ArithmeticException("already triggered");
        }
        this.isOK = event.isOK;
        this.value = event.value;
        this.isTriggered = true;
        this.environment.Schedule(this, priority);
    }

    public void Succeed(Object value, int priority){
        if (isTriggered) {
            throw new ArithmeticException("already triggered");
        }
        this.isOK = true;
        this.value = value;
        this.isTriggered = true;
        this.environment.Schedule(this, priority);
    }

    public void Fail(Object value, int priority){
        if (isTriggered) {
            throw new ArithmeticException("already triggered");
        }
        this.isOK = false;
        this.value = value;
        this.isTriggered = true;
        this.environment.Schedule(this, priority);
    }

    public void AddCallback(Action<Event> callback) {
        if (isProcessed) throw new ArithmeticException("Event is already processed.");
        this.callBackList.add(callback);
    }

    public void RemoveCallback(Action<Event> callback) {
        if (isProcessed) throw new ArithmeticException("Event is already processed.");
        this.callBackList.remove(callback);
    }

    static <E extends Exception> void doThrow(Exception e) throws E {
        throw (E)e;
    }

    public void Process() throws StopSimulationException {
        if (isProcessed) {
            throw new ArithmeticException("already triggered");
        }
        this.isProcessed = true;
        for (int i = 0; i < this.callBackList.size(); i++){
            this.callBackList.get(i).invoke(this);
        }
        this.callBackList = null;
    }
}

