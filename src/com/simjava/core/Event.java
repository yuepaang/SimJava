package com.simjava.core;

import java.util.ArrayList;
import java.util.List;

public class Event {

    protected int priorityUrgent = 0;
    protected int priorityNormal = 1;

    private Environment environment;
    private EventValue eventValue;
    private List<String> callBacks;

    public Event(){
        this.environment = null;
        this.eventValue = new EventValue("", true);
        this.callBacks = new ArrayList<String>();

    }

    public Event(Environment env, List<String> callBacks, EventValue value){
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

    public void Trigger(Event event){
        this.eventValue.setData(event.eventValue.getData());
//        this.environment.schedule(this);
    }

    public Event Succeed(String data){
        this.eventValue.setData(data);
//        this.environment.schedule(this);
        return this;
    }

    public Event Fail(String err){
        this.eventValue.setData(err);
//        this.environment.schedule(this);
        return this;
    }
}





//class Interruption extends Event{
//
//}

