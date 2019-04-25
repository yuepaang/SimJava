package com.simjava.core;

public class EventValue {

    String data;
    boolean isPending;

    public EventValue(){
        this.data = "";
        this.isPending = true;
    }

    public EventValue(String data, boolean isPending){
        this.data = data;
        this.isPending = isPending;
    }
}
