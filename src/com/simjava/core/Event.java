package com.simjava.core;

import java.util.ArrayList;

public class Event {

    final int priorityUrgent = 0;
    final int priorityNormal = 1;

    private Environment environment;
    private EventValue eventValue;
    private ArrayList<String> callBacks;

    public EventValue getEventValue() {
        return eventValue;
    }

    public void setEventValue(EventValue eventValue) {
        this.eventValue = eventValue;
    }

    public Event(){
        this.environment = null;
        this.eventValue = new EventValue("", true);
        this.callBacks = new ArrayList<String>();

    }

    public Event(Environment env){
        this.environment = env;
        this.callBacks = new ArrayList<String>();
        this.eventValue = new EventValue("", true);
    }

    public Event(Environment env, ArrayList<String> callBacks, EventValue value){
        this.environment = env;
        this.callBacks = callBacks;
        this.eventValue = value;
    }

    public boolean Triggered(){
        return !this.eventValue.getData().equals("");
    }

    public boolean Processed(){
        return this.callBacks.size() == 0;
    }

    public String Value(){
        if (this.eventValue.getData().equals("")) {
            System.out.println("Value is not yet available");
        }
        return this.eventValue.getData();
    }

    public Event Succeed(String data){
        this.eventValue.setData(data);
        this.environment.Schedule(this, this.priorityNormal, 0);
        return this;
    }

    public Event Fail(String err){
        this.eventValue.setData(err);
        this.environment.Schedule(this, this.priorityNormal, 0);
        return this;
    }
}

