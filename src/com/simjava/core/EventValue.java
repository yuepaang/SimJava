package com.simjava.core;

public class EventValue {

    private String data;
    private boolean isPending;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.setPending(false);
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public EventValue(){
        this.data = "";
        this.isPending = true;
    }

    public EventValue(String data, boolean isPending){
        this.data = data;
        this.isPending = isPending;
    }
}
