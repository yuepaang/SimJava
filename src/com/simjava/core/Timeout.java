package com.simjava.core;

public class Timeout extends Event{

    public Timeout(Environment environment, int delay, String data, boolean isOK, int priority){
        setOK(isOK);
        setValue(data);
        setTrigger(true);
        environment.Schedule(this, priority, 0);
    }

}