package com.simjava.core;

public class Initialize extends Event {

    public Initialize(Environment environment, Process process){
//        super(environment, new ArrayList<String>(process.resume), new EventValue());
        environment.Schedule(this, super.priorityUrgent, 0);
    }
}