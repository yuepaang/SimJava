package com.simjava.core;

import com.simjava.action.Action;

public class StoreGet extends Event {
    public int time;
    public Process owner;

    public StoreGet(Environment environment, Action<Event> callback) {
        super(environment);
        callBackList.add(callback);
        time = environment.now;
        owner = environment.activeProcess;
    }
}
