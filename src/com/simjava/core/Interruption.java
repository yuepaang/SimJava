package com.simjava.core;

import com.simjava.action.Action;
import com.simjava.action.ActionImpl;

import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;

public class Interruption extends Event {

    public Environment environment;
    public List<Action> callBackList;
    public Object value;
    public int delay;
    public boolean ok;
    private Process process;

    public Interruption(Process process, String cause) {
        this.environment = process.environment;
        this.callBackList = new ArrayList<>();
        callBackList.add(new ActionImpl<Event>(e -> Interrupt(e)));
        this.value = new InterruptedException(cause);
        this.ok = false;

        if (!process.value.equals(PENGDING)) {
            throw new RuntimeException("Process has terminated and cannot be interrupted.");
        }
        if (process.equals(this.environment.activeProcess)) {
            throw new RuntimeException("A process is not allowed to interrupt itself.");
        }

        this.process = process;
        this.environment.Schedule(this, URGENT);
    }

    public void Interrupt(Event event) {
        if (!this.process.value.equals(PENGDING)) {
            return;
        }
        this.process.target.callBackList.remove(new ActionImpl<Event>(e -> process.Resume(e)));
        this.process.Resume(this);
    }
}
