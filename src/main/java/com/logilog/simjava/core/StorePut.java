package com.logilog.simjava.core;

import com.logilog.simjava.action.Action;

public class StorePut extends Event {
    public int time;
    public Process owner;

    public StorePut(Environment environment, Action<Event> callback, Object value) {
        super(environment);
        if (value == null) throw new IllegalArgumentException("value to put in a store cannot be null");
        callBackList.add(callback);
        this.value = value;
        time = environment.now;
        owner = environment.activeProcess;
    }
}
