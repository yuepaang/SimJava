package com.simjava.core;

import java.util.ArrayList;
import java.util.Iterator;

public class Process extends Event{

    private Event target;
    private Iterator<Event> generator;

    public Process(Environment environment, Iterable<Event> generator, int priority){
        this.generator = generator.iterator();
        target = new Initialize(environment, this, priority);
    }

    private class Initialize extends Event {
      public Initialize(Environment environment, Process process, int priority) {
            environment.Schedule(this, priority, 0);
        }
    }

}