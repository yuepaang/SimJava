package com.simjava.core;

public class Timeout extends Event{
    private int delay;

    public Timeout(Environment environment, int delay, Object value, boolean isOK, int priority){
        super(environment);
        this.delay = delay;
        this.isOK = isOK;
        this.value = value;
        this.isTriggered = true;
        environment.Schedule(delay, this, priority);
    }

}