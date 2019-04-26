package com.simjava.core;

import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;

public class Interruption extends Event {

    private Environment environment;
    private List<Function> callBackList;
    private Object value;
    private int delay;
    private boolean ok;

    private Process process;

    public Interruption(Process process, String cause) {
        this.environment = process.getEnvironment();
        this.callBackList = new ArrayList<>();
        callBackList.add(this.Interrupt());
        this.value = new InterruptedException(cause);
        this.ok = false;

        if (!process.getValue().equals(PENGDING)) {
            throw new RuntimeException("Process has terminated and cannot be interrupted.");
        }
        if (process.equals(this.environment.getActiveProcess())) {
            throw new RuntimeException("A process is not allowed to interrupt itself.");
        }

        this.process = process;
        this.environment.Schedule(this, URGENT);
    }

    public void Interrupt(Event event) {
        if (!this.process.getValue().equals(PENGDING)) {
            return;
        }
        this.process.getTarget().getCallBackList().remove(this.process.Resume());
        this.process.Resume(this);
    }
}
