package com.simjava.core;

import java.util.ArrayList;


public class Timeout extends Event{
    private Environment environment;
    private ArrayList<String> callBacks;
    private EventValue eventValue;
    private int delay;

    public Timeout(Environment environment, int delay, String data){
        super(environment, new ArrayList<String>(), new EventValue(data, false));
        this.delay = delay;
        environment.Schedule(this, super.priorityNormal, this.delay);
    }

    public void Schedule(Environment env){
        env.Schedule(this.Event, super.priorityNormal, this.delay)
    }
}