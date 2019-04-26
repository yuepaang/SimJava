package com.simjava.core;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class Event {

    public static final int URGENT = 0;
    public static final int NORMAL = 1;
    public static final Object PENGDING = new Object();

    private Environment environment;
    private List<Function> callBackList;
    private Object value;
    private boolean ok;

    public List<Function> getCallBackList() {
        return callBackList;
    }

    public void setCallBackList(List<Function> callBackList) {
        this.callBackList = callBackList;
    }

    public void addCallBack(Function callback) {
        this.callBackList.add(callback);
    }

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

    public void setValue(Object value) {
        this.value = value;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Object Value(){
        if (this.value.equals(PENGDING)) {
            throw new RuntimeException("Value is not yet available");
        }
        return this.value;
    }

    public void Trigger(Event event){
        this.ok = event.OK();
        setValue(event.Value());
        this.environment.Schedule(this);
    }

    public Event Succeed(Object value){
        if (!this.value.equals(PENGDING)) {
            throw new RuntimeException("Value is not yet available");
        }
        setOk(true);
        setValue(value);
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

        setOk(false);
        setValue(exc);
        this.environment.Schedule(this);
        return this;
    }

}

