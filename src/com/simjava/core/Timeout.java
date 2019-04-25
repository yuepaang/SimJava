package com.simjava.core;

public class Timeout extends Event{

    public Timeout(Environment environment, int delay, String data, boolean isOK, int priority){
        environment.Schedule(this, priority, 0);
    }

}