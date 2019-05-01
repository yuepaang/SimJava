package com.simjava.core;

import com.simjava.action.Action;

public class ContainerGet extends Event {
    public double amount;
    public int time;
    public Process owner;

    public ContainerGet(Environment environment, Action<Event> callback, double amount) {
        super(environment);
        if (amount < 0) throw new IllegalArgumentException("Amount must be larger than 0.");
        this.amount = amount;
        callBackList.add(callback);
        time = environment.now;
        owner = environment.activeProcess;
    }
}
