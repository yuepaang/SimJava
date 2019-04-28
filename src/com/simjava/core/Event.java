package com.simjava.core;

import com.simjava.action.Action;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;


public class Event {

    public static final int URGENT = 0;
    public static final int NORMAL = 1;
    public static final Object PENGDING = new Object();

    public Environment environment;
    public List<Action> callBackList;
    public Object value;
    public boolean ok;

    public Event(){
        this.environment = null;
        this.value = PENGDING;
        this.callBackList = new ArrayList<>();
        }

    public Event(Environment env){
        this.environment = env;
        this.value = PENGDING;
        this.callBackList = new ArrayList<>();

    }

    public boolean Triggered(){
        return !this.value.equals(PENGDING);
    }

    public boolean Processed(){
        return this.callBackList == null;
    }

    public boolean OK(){
        return this.ok;
    }

    public Object Value(){
        if (this.value.equals(PENGDING)) {
            throw new RuntimeException("Value is not yet available");
        }
        return this.value;
    }

    public void Trigger(Event event){
        this.ok = event.OK();
        value = event.Value();
        this.environment.Schedule(this);
    }

    public Event Succeed(Object value){
        if (!this.value.equals(PENGDING)) {
            throw new RuntimeException("Value is not yet available");
        }
        ok = true;
        this.value = value;
        this.environment.Schedule(this);
        return this;
    }

    public Event Fail(Exception exc){
        if (!this.value.equals(PENGDING)) {
            throw new RuntimeException("Value is not yet available");
        }
        if (!(exc instanceof Exception)){
            throw new ValueException("It is not an exception");
        }

        ok = false;
        value = exc;
        this.environment.Schedule(this);
        return this;
    }

}

