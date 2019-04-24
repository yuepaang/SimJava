package com.simjava.core;

import java.util.ArrayList;
import java.util.List;


public class Timeout extends Event{
    private Environment environment;
    private List<String> callBacks;
    private EventValue eventValue;
    private int delay;

    public Timeout(Environment environment, int delay, EventValue eventValue){
        super(environment, new ArrayList<String>(), eventValue);
        this.delay = delay;
        environment.Schedule(this, super.priorityNormal, delay);

    }

}