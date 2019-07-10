package com.logilog.simjava.core;

import com.logilog.simjava.action.Action;

public class ContainerPut extends Event {
    public double amount;
    public int time;
    public Process owner;

    public ContainerPut(Environment environment, Action<Event> callback, double amount) {
        super(environment);
        if (amount < 0) throw new IllegalArgumentException("Amount must be larger than 0.");
        this.amount = amount;
        callBackList.add(callback);
        time = environment.now;
        owner = environment.activeProcess;
    }
}
