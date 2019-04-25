package com.simjava.core;

public class Event {

    Environment environment;
    EventValue eventValue;


    public Event(){
        this.environment = null;
        this.eventValue = new EventValue();
    }

    public Event(Environment env){
        this.environment = env;
       this.eventValue = new EventValue();
    }

    public boolean IsOK(){
        return this.eventValue.data.equals("");
    }

    public void Succeed(String data, int priority){
        if (!this.eventValue.isPending) {
            throw new ArithmeticException("already triggered");
        }
        this.eventValue.data = data;
        this.environment.Schedule(this, priority, 0);
    }

    public Event Fail(EventValue eventValue){
        if (!this.eventValue.isPending) {
            throw new ArithmeticException("already triggered");
        }
        errVal = eventValue.data;
        this.eventValue.data = errVal;
        this.environment.Schedule(this, priority, 0);
        return this;
    }
}

