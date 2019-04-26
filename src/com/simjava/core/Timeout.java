package com.simjava.core;

public class Timeout extends Event{
    private int delay;

    public Timeout(Environment environment, int delay, String data, boolean isOK, int priority){
        super(environment);
        this.delay = delay;
        setOK(isOK);
        setValue(data);
        setTrigger(true);
        environment.Schedule(this, priority, delay);
    }

}